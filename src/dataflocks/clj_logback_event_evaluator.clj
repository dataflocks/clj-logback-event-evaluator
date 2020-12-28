(ns dataflocks.clj-logback-event-evaluator
  (:gen-class
   :name dataflocks.CljLogbackEventEvaluator
   :extends ch.qos.logback.core.boolex.EventEvaluatorBase
   :state state
   :init init
   :exposes-methods {start parentStart}
   :constructors {[] []}
   :methods [[setExpression [String] void]])
  (:import [ch.qos.logback.classic.spi ILoggingEvent]))

(defn event->map [^ILoggingEvent e]
  {:mdc         (.getMDCPropertyMap e)
   :level       (str (.getLevel e))
   :msg         (.getMessage e)
   :fmt-msg     (.getFormattedMessage e)
   :logger-name (.getLoggerName e)
   :thread-name (.getThreadName e)
   :marker      (.getMarker e)
   :timestamp   (.getTimeStamp e)
   :event       e})

(defn -init []
  [[]
   (atom {:expression nil
          :fn         nil})])

(defn -setExpression [^dataflocks.CljLogbackEventEvaluator this ^java.lang.String expression]
  (swap! (.state this) assoc :expression expression))

(defn -getExpression [^dataflocks.CljLogbackEventEvaluator this]
  (:expression @(.state this)))

(defn mk-evaluator-fn [str]
  (eval `(fn [{:keys [~'mdc ~'level ~'msg ~'fmt-msg ~'logger-name ~'thread-name ~'marker ~'timestamp ~'event]}]
           (boolean ~(read-string str)))))

(defn -start [^dataflocks.CljLogbackEventEvaluator this]
  (.parentStart this)
  (swap! (.state this)
         (fn [v]
           (assoc v :fn (mk-evaluator-fn (:expression v))))))

(defn -evaluate [^dataflocks.CljLogbackEventEvaluator this ^ILoggingEvent e]
  (let [evaluator-fn (:fn @(.state this))]
    (evaluator-fn (event->map e))))
