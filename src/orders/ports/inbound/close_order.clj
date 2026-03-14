(ns orders.ports.inbound.close-order
  "Use case: close order. SRP: single responsibility."
  (:require [orders.domain.entities.order :as order-domain]
            [orders.domain.events.order-created :as order-created-event]
            [orders.ports.outbound :as outbound]))

(defn execute
  "Executes the use case. DIP: repository and event-publisher are abstractions."
  [repository event-publisher order-data]
  (let [order (order-domain/order order-data)]
    (outbound/save repository order)
    (outbound/publish event-publisher (order-created-event/order-created order))
    (let [updated-order (order-domain/update-status order :products-reserved)]
      (outbound/update-order repository updated-order)
      {:success true :order updated-order})))
