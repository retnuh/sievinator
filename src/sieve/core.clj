(ns sieve.core
  (:require [com.stuartsierra.component :refer [using system-map]]
            [org.zalando.stups.friboo.config :as config]
            [org.zalando.stups.friboo.system :as system]
            [org.zalando.stups.friboo.log :as log]
            [sieve.api :as api]
            [sieve.service :as svc]
            [sieve.storage.fs :as fs-ss]
            )
  (:gen-class))

(defn run
  "Initializes and starts the whole system."
  [default-configuration]
  (let [configuration (config/load-configuration
                       [:sieve :http :fs-ss]
                       [svc/default-sieve-configuration
                        api/default-http-configuration
                        fs-ss/default-fs-sieve-storage-configuration
                        default-configuration])
        _ (log/info "Starting with configuration: %s" (config/mask configuration))
        system (system-map
                :fs-ss (fs-ss/map->FS-SS {:configuration (:fs-ss configuration)})
                :sieve (using
                        (svc/map->SIEVE {:configuration (:sieve configuration)})
                        {:ss :fs-ss})
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
