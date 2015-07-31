(ns sieve.storage.fs
  (:require
   [sieve.storage :as storage]
   [clojure.java.io :as io]
   [com.stuartsierra.component :as component]
   [org.zalando.stups.friboo.log :as log]
   [org.zalando.stups.friboo.config :refer [require-config]]))

(defrecord FS-SS [configuration]
   component/Lifecycle
   (start [this]
     (require-config configuration :storage-path)
     this)
   (stop [this]
     ;; nothing to do
     this)

   storage/SievinatorStorage
   (store-state [this state]
     (spit (:storage-path configuration) state))

   (load-state [this]
     (binding [*read-eval* false]
       (let [path (:storage-path configuration)
             file (io/file path)]
         (when (.exists file)
           (read-string (slurp path)))))))

(def default-fs-sieve-storage-configuration {:fs-ss-storage-path "/tmp/ss.clj"})
