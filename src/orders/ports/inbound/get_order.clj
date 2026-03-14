(ns orders.ports.inbound.get-order
  "Use case: get order by ID. SRP: single responsibility."
  (:require [orders.ports.outbound :as outbound]))

(defn execute
  "Executes the use case. DIP: repository is an abstraction."
  [repository order-id]
  (let [order (outbound/find-by-id repository order-id)]
    (if order
      {:success true :order order}
      {:success false :error "Order not found"})))
