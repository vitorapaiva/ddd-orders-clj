(ns orders.infra.event-publisher
  (:require [orders.ports.outbound :as ports]))

(defrecord ConsoleEventPublisher []
  ports/EventPublisher
  
  (publish [_ event]
    (println ">>> Event published:" (:type event))
    (println "    Data:" (:data event))
    (println "    Timestamp:" (:timestamp event))
    event))

(defn create-publisher
  []
  (->ConsoleEventPublisher))
