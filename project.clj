(defproject sieve "0.1.0-SNAPSHOT"
  :description "The all-singing all-dancing SIEVINATOR for your prime number needs!"
  :url "https://github.com/retnuh/sievinator"

  :license {:name "The UNLICENSE"
            :url  "http://unlicense.org"}

  :min-lein-version "2.0.0"

  :dependencies [[org.zalando.stups/friboo "1.0.0-RC1"]]

  :main ^:skip-aot sieve.core
  :uberjar-name "sieve.jar"

  :plugins []

  :pom-addition [:developers
                 [:developer {:id "retnuh"}
                  [:name "Hunter Kelly"]
                  [:email "retnuh@gmail.com"]
                  [:role "Maintainer"]]]

  :profiles {:uberjar {:aot :all}

             :dev     {:repl-options {:init-ns user}
                       :source-paths ["dev"]
                       :dependencies [[org.clojure/tools.namespace "0.2.10"]
                                      [org.clojure/java.classpath "0.2.2"]]}})
