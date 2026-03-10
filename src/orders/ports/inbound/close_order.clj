(ns orders.ports.inbound.close-order
  "Caso de uso: fechar pedido. SRP: única responsabilidade."
  (:require [orders.domain.entities :as entities]
            [orders.domain.events :as events]
            [orders.ports.outbound :as outbound]))

(defn execute
  "Executa o caso de uso. DIP: repository e event-publisher são abstrações."
  [repository event-publisher order-data]
  (let [order (entities/order order-data)]
    (outbound/save repository order)
    (outbound/publish event-publisher (events/order-created order))
    (let [updated-order (entities/update-status order :products-reserved)]
      (outbound/update-order repository updated-order)
      {:success true :order updated-order})))
