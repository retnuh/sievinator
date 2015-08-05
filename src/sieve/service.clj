(ns sieve.service
  (:require [com.stuartsierra.component :as component]
            [org.zalando.stups.friboo.log :as log]
            [org.zalando.stups.friboo.config :refer [require-config]]
            [sieve.sieve :as sieve]
            ))

(defn start-component [component]
  (if (:sievinator component)
    (do
      (log/debug "Skipping start of SIEVE; already created.")
      component)
    
    (do
      (let [configuration (:configuration component)
            state (if-let [bs (:block-size configuration)]
                    {:block-size bs}
                    {})
            the-sievinator (sieve/sievinator state)]
        (log/debug "Starting sievinator with state: %s" state)
        (assoc component
               :sievinator the-sievinator
               :state (:state the-sievinator)
               :primes ((:primes-seq the-sievinator))
               :primes-up-to (:primes-up-to the-sievinator)
               :prime? (:prime? the-sievinator)
               :factors (:factors the-sievinator)
               :composites (:composites-seq the-sievinator)
               :factors-seq (:factors-seq the-sievinator)
               :block-size (:block-size the-sievinator)
               )))))

(defn stop-component [component]
  (if-not (:sievinator component)
    (do
      (log/debug "Skipping stop of SIEVE; already stopped.")
      component)

    (do
      (log/info "Stopping SIEVE.")
      (dissoc component :sievinator))))

(defrecord SIEVE [configuration ss]
   component/Lifecycle
   (start [this]
     (start-component this))
   (stop [this]
     (stop-component this)))

(def default-sieve-configuration {:sieve-block-size 100})
