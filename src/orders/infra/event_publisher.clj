(ns orders.infra.event-publisher
  "Event publisher with pluggable handlers. OCP: new handlers without modifying core."
  (:require [orders.ports.outbound :as ports]
            [orders.ports.event-handler :as eh]))

(defrecord EventPublisherWithHandlers [handlers]
  ports/EventPublisher
  (publish [_ event]
    (doseq [h handlers]
      (when (eh/supports? h event)
        (eh/handle h event)))
    (println ">>> Event published:" (:type event))
    (println "    Data:" (:data event))
    (println "    Timestamp:" (:timestamp event))
    event))

(defn create-publisher
  "Creates publisher with list of handlers. DIP: depends on EventHandler protocol."
  [& handlers]
  (->EventPublisherWithHandlers (vec handlers)))
