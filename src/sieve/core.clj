(ns sieve.core
  (:require [com.stuartsierra.component :refer [using system-map]]
            [org.zalando.stups.friboo.config :as config]
            [org.zalando.stups.friboo.system :as system]
            [org.zalando.stups.friboo.log :as log]
            [sieve.api :as api]
            [sieve.service :as svc]
            [sieve.storage.fs :as fs-ss]
            [sieve.storage.sqlite :as sqlite-ss]
            )
  (:gen-class))

(defn run
  "Initializes and starts the whole system."
  [default-configuration]
  (let [configuration (config/load-configuration
                       [:sieve :http :fs-ss :sqlite-ss]
                       [svc/default-sieve-configuration
                        api/default-http-configuration
                        fs-ss/default-fs-sieve-storage-configuration
                        sqlite-ss/default-sqlite-sieve-storage-configuration
                        default-configuration])
        sieve-ss (get-in configuration [:sieve :ss])
        _ (log/info "Starting with configuration: %s %s" sieve-ss (config/mask configuration))
        system (system-map
                :fs-ss (fs-ss/map->FS-SS {:configuration (:fs-ss configuration)})
                :sqlite-ss (sqlite-ss/map->SQLITE-SS
                            {:configuration (:sqlite-ss configuration)})
                :sieve (using
                        (svc/map->SIEVE {:configuration (:sieve configuration)})
                        {:ss (if (= sieve-ss "FS") :fs-ss :sqlite-ss)})
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
