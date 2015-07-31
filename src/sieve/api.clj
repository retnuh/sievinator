(ns sieve.api
  (:require [org.zalando.stups.friboo.system.http :refer [def-http-component]]
            [ring.util.response :refer :all]
            [org.zalando.stups.friboo.ring :refer :all]
            [org.zalando.stups.friboo.log :as log]
            [io.sarnowski.swagger1st.util.api :as api]))

; define the API component and its dependencies (a sieve component)
(def-http-component API "api/sieve-api.yaml" [sieve])

(def default-http-configuration
  {:http-port 8080})

;; numbers

(defn numbers-is-prime [{:keys [n] :as params} request {:keys [prime?]}]
  (log/debug "params: '%s'" params)
  (log/debug (str n))
  (-> n
      prime?
      response
      content-type-json))

(defn numbers-factors [{:keys [n] :as params} request {:keys [factors]}]
  (log/debug "params: '%s'" params)
  (log/debug (str n))
  (-> n
      factors
      response
      content-type-json))

;; primes

(defn primes-up-to [{:keys [n] :as params} request {:keys [primes-up-to]}]
  (log/debug "params: '%s'" params)
  (log/debug (str n))
  (-> n
      primes-up-to
      response
      content-type-json))

(defn primes-from-to [{:keys [from to] :as params} request {:keys [primes-up-to]}]
  (log/debug "params: '%s'" params)
  (-> (drop-while #(< % from) (primes-up-to to))
      response
      content-type-json))
  
(defn primes-nth [{:keys [n]} request {:keys [primes]}]
  (log/debug (str "Looking for nth " n " prime"))
  (-> (nth primes n)
      response
      content-type-json))

;; the sieve itself

(defn sieve-block-size
  ([{:keys [size]} request {:keys [block-size]}]
   (let [old (block-size)]
     (log/debug (str "old-size: " old " new-size: " size))
     (when size
       (block-size size))
     (-> old
         response
         content-type-json))))

(defn sieve-state
  ([params request {:keys [state]}]
   (log/debug (str "state: " (state)))
   (-> (state)
       response
       content-type-json)))

;; (defn approve-version! [{:keys [application_id version_id approval]} request db]
;;   (if-let [application (load-application application_id db)]
;;     (do
;;       (u/require-internal-team (:team_id application) request)
;;       (let [defaults {:notes nil}]
;;         (sql/cmd-approve-version!
;;           (merge defaults approval {:version_id     version_id
;;                                     :application_id application_id
;;                                     :user_id        (get-in request [:tokeninfo "uid"])})
;;           {:connection db}))
;;       (log/audit "Approved version %s for application %s." version_id application_id)
;;       (response nil))
;;     (api/error 404 "application not found")))
