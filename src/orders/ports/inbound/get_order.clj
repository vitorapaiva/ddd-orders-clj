(ns orders.ports.inbound.get-order
  "Caso de uso: obter pedido por ID. SRP: única responsabilidade."
  (:require [orders.ports.outbound :as outbound]))

(defn execute
  "Executa o caso de uso. DIP: repository é abstração."
  [repository order-id]
  (let [order (outbound/find-by-id repository order-id)]
    (if order
      {:success true :order order}
      {:success false :error "Order not found"})))
