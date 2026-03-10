(ns orders.ports.inbound.list-orders
  "Caso de uso: listar pedidos. SRP: única responsabilidade."
  (:require [orders.ports.outbound :as outbound]))

(defn execute
  "Executa o caso de uso. DIP: repository é abstração."
  [repository]
  {:success true :orders (outbound/list-all repository)})
