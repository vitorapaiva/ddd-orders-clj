(ns orders.infra.event-publisher
  "Publicador de eventos com handlers plugáveis. OCP: novos handlers sem modificar o core."
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
  "Cria publicador com lista de handlers. DIP: depende do protocolo EventHandler."
  [& handlers]
  (->EventPublisherWithHandlers (vec handlers)))
