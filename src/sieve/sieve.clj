(ns sieve.sieve)

(defn- pthread [& args]
  (apply println (.getName (Thread/currentThread)) args))

(defn- vec-extend [v size val]
  (let [vb (transient v)]
    (while (< (count vb) size) 
      (conj! vb val))
    vb))

;; Introducing - the sievinator!!

(defn sievinator
  ([] (sievinator {:primes [] :odd-numbers-seen [] :block-size 1000}))
  ([initial-state] 
   (let [the-agent (agent initial-state)]
     (letfn [(state [] @the-agent)
             (number-for-index [^long c] (inc (* 2 (inc c))))
             (ensure-n [n wait?]
               (let [{:keys [odd-numbers-seen]} @the-agent]
                 (when (< (count odd-numbers-seen) (quot n 2))
                   (send the-agent fill-up-to n)
                   (and wait? (await the-agent)))))
             ;; this is not pretty
             (fill-up-to [{:keys [odd-numbers-seen primes block-size] :as current-state} ^long n]
               (if-not (< (count odd-numbers-seen) (quot n 2))
                 current-state
                 (let [numbers (vec-extend odd-numbers-seen (quot n 2) 0)
                       next-index (count odd-numbers-seen)
                       s (range next-index (inc (quot (int (Math/sqrt n)) 2)))
                       next-number (number-for-index next-index)]
                   (dorun (for [p primes
                                :let [d (quot next-number p) nd (if (even? d) (inc d) d)]
                                m (range (dec (quot (* p nd) 2)) (count numbers) p)]
                            (and (> nd 1) (assoc! numbers m p))))
                   (dorun (for [i s
                                :when (zero? (get numbers i))
                                :let [c (number-for-index i)]
                                m (rest (range
                                         (max i (quot (* i (quot next-number c)) 2))
                                         (count numbers)
                                         c))]
                            (assoc! numbers m c)))
                   (let [odd-numbers-seen (persistent! numbers)
                         new-primes (reduce (fn [np i] (conj np (number-for-index i)))
                                            primes
                                            (filter #(zero? (get odd-numbers-seen %))
                                                    (range next-index (count odd-numbers-seen))))]
                     {:odd-numbers-seen odd-numbers-seen :primes new-primes :block-size block-size}))))
             (primes-up-to [^long n]
               (ensure-n n false)
               (take-while #(<= % n) (cons 2 (lazy-seq (do (await the-agent)
                                                           (:primes @the-agent))))))
             (factors [^long n]
               (ensure-n n true)
               (loop [n n f [] numbers (:odd-numbers-seen @the-agent)]
                 (cond
                   (even? n) (recur (unsigned-bit-shift-right n 1) (conj f 2) numbers)
                   (= 1 n) f
                   :else (let [i (get numbers (dec (quot n 2)))]
                           (if (zero? i)
                             (sort (conj f n))
                             (recur (/ n i) (conj f i) numbers))))))
             (blocked-seq
               ([f]
                (ensure-n (:block-size (state)) false)
                (lazy-seq (blocked-seq f 0)))
               ([f start-index]
                (await the-agent)
                (let [{:keys [primes odd-numbers-seen] :as s} @the-agent
                      block-size (:block-size (state))
                      size (* 2 (count odd-numbers-seen))
                      [sq nsize] (f s start-index)]
                  (ensure-n (+ block-size size) false)
                  (lazy-cat sq (blocked-seq f nsize)))))
             (primes-seq []
               (cons 2 (blocked-seq (fn [{primes :primes} start-index]
                                      [(map primes (range start-index (count primes))) (count primes)]))))
             (composites-seq []
               (let [odd-composites 
                     (blocked-seq (fn [{numbers :odd-numbers-seen} start-index]
                                    [(->> (range start-index (count numbers))
                                          (filter #(pos? (numbers %)))
                                          (map number-for-index))
                                     (count numbers)]))
                     even-composites (iterate #(+ 2 %) 4)]
                 (letfn [(sfn [e o] 
                           (if (< (first e) (first o))
                             (cons (first e) (lazy-seq (sfn (rest e) o)))
                             (cons (first o) (lazy-seq (sfn e (rest o))))))]
                   (lazy-seq (sfn even-composites odd-composites)))))
             (factors-seq
               ([] (factors-seq (composites-seq)))
               ([cs] (map #(vector % (factors %)) cs)))
             (prime? [n]
               (if (even? n)
                 (= n 2)
                 (do 
                   (ensure-n n true)
                   (let [{:keys [odd-numbers-seen]} @the-agent]
                     (= 0 (odd-numbers-seen (dec (quot n 2))))))))
             (block-size
               ([] (:block-size (state)))
               ([bs] (send the-agent assoc :block-size bs)))
             ]
       {:wait #(await the-agent) :state state :primes-up-to primes-up-to
        :primes-seq primes-seq :composites-seq composites-seq :factors-seq factors-seq
        :factors factors :prime? prime? :block-size block-size}))))

