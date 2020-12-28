(ns dataflocks.clj-logback-event-evaluator-test
  (:require [clojure.test :refer :all]
            [dataflocks.clj-logback-event-evaluator :refer :all])
  (:import  [dataflocks CljLogbackEventEvaluator]
            [ch.qos.logback.classic.spi ILoggingEvent]
            [ch.qos.logback.classic Level]))


(def dummy-event
  (reify ILoggingEvent
    (getMDCPropertyMap [_] {"key" "value"})
    (getLevel [_] Level/DEBUG)
    (getMessage [_] "billing")
    (getFormattedMessage [_] "fmt-message")
    (getLoggerName [_] "logger-name")
    (getThreadName [_] "thread-name")
    (getTimeStamp [_] 9999)
    (getMarker [_] nil)))

(deftest test-expression []
  (let [lee (doto (CljLogbackEventEvaluator.)
              (.setExpression "(= (get mdc \"key\") \"value\")")
              (.start))]
    (is (true? (.evaluate lee dummy-event)))))


(deftest test-invalid-expression []
  (is (thrown? clojure.lang.Compiler$CompilerException
               (doto (CljLogbackEventEvaluator.)
                 (.setExpression "this is not valid clojure code.")
                 (.start)))))

(deftest test-evaluator-exception []
  (let [lee (doto (CljLogbackEventEvaluator.)
              (.setExpression "(.length marker)")
              (.start))]
    (is (thrown? java.lang.NullPointerException
                 (.evaluate lee dummy-event)))))

(deftest test-contains-example []
  (let [lee (doto (CljLogbackEventEvaluator.)
              (.setExpression "(.contains msg \"billing\")")
              (.start))]
    (is (true? (.evaluate lee dummy-event)))))

(deftest test-mapping-equality-example []
  (let [lee (doto (CljLogbackEventEvaluator.)
              (.setExpression "(= msg (.getMessage event))")
              (.start))]
    (is (true? (.evaluate lee dummy-event)))))
