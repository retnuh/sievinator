(ns sieve.storage.sqlite
  (:require [yesql.core :refer [defqueries]]
            [clojure.java.jdbc :as jdbc]
            [sieve.storage :as storage]
            [com.stuartsierra.component :as component]
            [org.zalando.stups.friboo.log :as log]
            [sieve.sieve :refer [number-for-index]]            
            [org.zalando.stups.friboo.system.db :as db]))

(def default-sqlite-sieve-storage-configuration
  {
   :sqlite-ss-classname   "org.sqlite.JDBC"
   :sqlite-ss-subprotocol "sqlite"
   :sqlite-ss-subname     "/tmp/ss.sqlite"
   :sqlite-ss-user        "duder"
   :sqlite-ss-password    "duder"
   })
  
(defqueries "db/sieve-storage.sql")

(defrecord SQLITE-SS [configuration datasource]
   component/Lifecycle
   (start [this]
     (merge (db/start-component this true) configuration))
   (stop [this]
     (db/stop-component this))  

   storage/SievinatorStorage
   (store-state [this state]
     (jdbc/with-db-transaction [conn this]
       (set-config! conn "block-size" (:block-size state))
       (dorun (map-indexed (fn [ind factor] (insert-odd-number! conn ind factor))
                           (:odd-numbers-seen state)))))

   (load-state [this]
     (jdbc/with-db-transaction [conn this]
       (let [{block-size :value} (first (get-config conn "block-size"))
             odd-numbers-rs (odd-numbers-seen conn)
             primes (->> odd-numbers-rs
                         (filter (fn [{:keys [ind factor]}] (zero? factor)))
                         (mapv #(number-for-index (:ind %))))
             odd-numbers-seen (->> odd-numbers-rs
                                   (mapv :factor))
             state {:block-size block-size :primes primes
                    :odd-numbers-seen odd-numbers-seen}]
         (log/debug "Loaded from sqlite: %s" state)
         state))))
  
