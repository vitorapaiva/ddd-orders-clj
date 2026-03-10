(ns orders.infra.http.routes
  (:require [orders.infra.http.handlers.close-order :as close-order]
            [orders.infra.http.handlers.list-orders :as list-orders]
            [orders.infra.http.handlers.get-order :as get-order]
            [orders.infra.http.handlers.update-status :as update-status]))

(defn create-routes
  [deps]
  [["/order/close" {:post {:handler (close-order/create deps)}}]
   ["/orders" {:get {:handler (list-orders/create deps)}}]
   ["/orders/:id" {:get {:handler (get-order/create deps)}}]
   ["/orders/:id/status" {:put {:handler (update-status/create deps)}}]])
