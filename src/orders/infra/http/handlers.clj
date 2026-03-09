(ns orders.infra.http.handlers
  (:require [orders.ports.inbound :as use-cases]
            [orders.adapters.inbound.order-adapter :as inbound-adapter]
            [orders.adapters.outbound.order-adapter :as outbound-adapter]))

(defn close-order
  [{:keys [repository products-client event-publisher]}]
  (fn [request]
    (let [order-data (inbound-adapter/json->order-data (:body request))
          result (use-cases/close-order repository products-client event-publisher order-data)]
      (if (:success result)
        {:status 201
         :body {:message "Order created successfully"
                :order (outbound-adapter/order->json (:order result))}}
        {:status 400
         :body {:error (:error result)}}))))

(defn list-orders
  [{:keys [repository]}]
  (fn [_request]
    (let [result (use-cases/list-orders repository)]
      {:status 200
       :body {:orders (outbound-adapter/orders->json (:orders result))}})))

(defn get-order
  [{:keys [repository]}]
  (fn [request]
    (let [order-id (get-in request [:path-params :id])
          result (use-cases/get-order repository order-id)]
      (if (:success result)
        {:status 200
         :body {:order (outbound-adapter/order->json (:order result))}}
        {:status 404
         :body {:error (:error result)}}))))

(defn update-status
  [{:keys [repository event-publisher]}]
  (fn [request]
    (let [order-id (get-in request [:path-params :id])
          new-status (keyword (get-in request [:body :status]))
          result (use-cases/update-order-status repository event-publisher order-id new-status)]
      (if (:success result)
        {:status 200
         :body {:message "Status updated"
                :order (outbound-adapter/order->json (:order result))}}
        {:status 400
         :body {:error (:error result)}}))))
