(ns sieve.core
  (:require [com.stuartsierra.component :refer [using system-map]]
            [org.zalando.stups.friboo.config :as config]
            [org.zalando.stups.friboo.system :as system]
            [org.zalando.stups.friboo.log :as log]
            [sieve.api :as api]
            [sieve.service :as svc]
            )
  (:gen-class))

(defn run
  "Initializes and starts the whole system."
  [default-configuration]
  (let [configuration (config/load-configuration
                       [:sieve :http]
                       [svc/default-sieve-configuration
                        api/default-http-configuration
                        default-configuration])
        _ (log/info "Starting with configuration: %s" (config/mask configuration))
        system (system-map
                :sieve (svc/map->SIEVE {:configuration (:sieve configuration)})
                :api (using
                      (api/map->API {:configuration (:http configuration)})
                      [:sieve]))]
    
    (system/run configuration system)))

(defn -main
  "The actual main for our uberjar."
  [& args]
  (try
    (run {})
    (catch Exception e
      (log/error e "Could not start system because of %s." (str e))
      (System/exit 1))))
