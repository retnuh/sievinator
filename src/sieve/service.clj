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
            the-sievinator (sieve/sievinator)]
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

(defmacro def-sieve-component
  "Defines a new sievinator component."
  [name]
  ; 'configuration' must be provided during initialization
  `(defrecord ~name [~(symbol "configuration")]
     component/Lifecycle

     (start [this#]
       (start-component this#))

     (stop [this#]
       (stop-component this#))))

(def-sieve-component SIEVE)

(def default-sieve-configuration {})
