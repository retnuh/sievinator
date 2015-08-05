(ns sieve.api
  (:require [org.zalando.stups.friboo.system.http :refer [def-http-component]]
            [ring.util.response :refer :all]
            [org.zalando.stups.friboo.ring :refer :all]
            [org.zalando.stups.friboo.log :as log]
            [io.sarnowski.swagger1st.util.api :as api]
            ))

; define the API component and its dependencies (a sieve component)
(def-http-component API "api/sieve-api.yaml" [sieve])

(def default-http-configuration
  {:http-port 8080})


;; primes

(defn primes-nth [{:keys [n]} request {:keys [primes]}]
  (log/debug (str "Looking for nth " n " prime " (type primes)))
  (-> (nth primes (dec n))
      response
      content-type-json))

