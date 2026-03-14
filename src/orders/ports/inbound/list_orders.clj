(ns orders.ports.inbound.list-orders
  "Use case: list orders. SRP: single responsibility."
  (:require [orders.ports.outbound :as outbound]))

(defn execute
  "Executes the use case. DIP: repository is an abstraction."
  [repository]
  {:success true :orders (outbound/list-all repository)})
