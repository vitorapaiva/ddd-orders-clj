(ns orders.ports.inbound.update-order-status
  "Caso de uso: atualizar status do pedido. SRP: única responsabilidade."
  (:require [orders.domain.entities :as entities]
            [orders.domain.events :as events]
            [orders.ports.outbound :as outbound]))

(defn execute
  "Executa o caso de uso. DIP: repository e event-publisher são abstrações."
  [repository event-publisher order-id new-status]
  (let [order (outbound/find-by-id repository order-id)]
    (if order
      (let [previous-status (:status order)
            updated-order (entities/update-status order new-status)]
        (outbound/update-order repository updated-order)
        (outbound/publish event-publisher (events/order-updated updated-order previous-status))
        {:success true :order updated-order})
      {:success false :error "Order not found"})))
