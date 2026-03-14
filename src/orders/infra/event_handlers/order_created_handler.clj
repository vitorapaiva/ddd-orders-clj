(ns orders.infra.event-handlers.order-created-handler
  "Handler for OrderCreated event. SRP: reserves products when order is created."
  (:require [orders.ports.outbound :as ports]
            [orders.ports.event-handler :as eh]))

(defrecord OrderCreatedHandler [products-service]
  eh/EventHandler
  (supports? [_ event]
    (= :order-created (:type event)))
  (handle [_ event]
    (let [order-id (get-in event [:data :order-id])
          items (get-in event [:data :items])
          result (ports/reserve-products products-service order-id items)]
      (when-not (:success result)
        (throw (ex-info (or (:error result) "Failed to reserve products")
                        {:order-id order-id :result result}))))))

(defn create-handler
  [products-service]
  (->OrderCreatedHandler products-service))
