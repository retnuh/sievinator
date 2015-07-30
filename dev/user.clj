; Copyright 2013 Stuart Sierra
;
; The use and distribution terms for this software are covered by the Eclipse Public License 1.0.
;    http://opensource.org/licenses/eclipse-1.0.php

(ns user
  "Tools for interactive development with the REPL. This file should
  not be included in a production build of the application."
  (:require
   [clojure.java.io :as io]
   [clojure.java.javadoc :refer [javadoc]]
   [clojure.pprint :refer [pprint]]
   [clojure.reflect :refer [reflect]]
   [clojure.repl :refer [apropos dir doc find-doc pst source]]
   [clojure.tools.namespace.repl :refer [refresh refresh-all disable-unload!]]
   [com.stuartsierra.component :as component]))

(def system
  "A Var containing an object representing the application under
  development."
  nil)

(defn start
  "Starts the system running, sets the Var #'system."
  []
  (alter-var-root #'system
                  (fn [s] (eval (read-string
                                "(do (require '(sieve core))
                                     (sieve.core/run {:system-log-level \"DEBUG\"}))"  )))))

(defn stop
  "Stops the system if it is currently running, updates the Var
  #'system."
  []
  (alter-var-root #'system
                  (fn [s] (when s (component/stop s)))))

(defn go
  "Initializes and starts the system running."
  []
  (start)
  :ready)

(defn reset
  "Stops the system, reloads modified source files, and restarts it."
  []
  (stop)
  (refresh :after 'user/go))

;; Stop the reloading magic from unloading this bad boy, stop
;; irritation when compile error, etc.
(disable-unload!)
