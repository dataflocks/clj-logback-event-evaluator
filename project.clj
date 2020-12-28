(defproject dataflocks/clj-logback-event-evaluator "0.1.0"
  :description "a logback event evaluator that evaluates clojure expressions"
  :license {:name "MIT License"
            :url  "https://opensource.org/licenses/MIT"}
  :aot :all
  :url "https://github.com/dataflocks/clj-logback-event-evaluator"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ch.qos.logback/logback-classic "1.2.3"]]
  :repl-options {:init-ns dataflocks.clj-logback-event-evaluator})
