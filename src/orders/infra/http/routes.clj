(ns orders.infra.http.routes
  (:require [orders.infra.http.handlers :as handlers]))

(defn create-routes
  [deps]
  [["/order/close" {:post {:handler (handlers/close-order deps)}}]
   ["/orders" {:get {:handler (handlers/list-orders deps)}}]
   ["/orders/:id" {:get {:handler (handlers/get-order deps)}}]
   ["/orders/:id/status" {:put {:handler (handlers/update-status deps)}}]])
