(ns orders.ports.inbound.update-order-status
  "Use case: update order status. SRP: single responsibility."
  (:require [orders.domain.entities.order :as order-domain]
            [orders.domain.events.order-updated :as order-updated-event]
            [orders.ports.outbound :as outbound]))

(defn execute
  "Executes the use case. DIP: repository and event-publisher are abstractions."
  [repository event-publisher order-id new-status]
  (let [order (outbound/find-by-id repository order-id)]
    (if order
      (let [previous-status (:status order)
            updated-order (order-domain/update-status order new-status)]
        (outbound/update-order repository updated-order)
        (outbound/publish event-publisher (order-updated-event/order-updated updated-order previous-status))
        {:success true :order updated-order})
      {:success false :error "Order not found"})))
