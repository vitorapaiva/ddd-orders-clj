(ns orders.infra.http.handlers.update-status
  "HTTP handler for updating order status. SRP: single endpoint."
  (:require [orders.ports.inbound.update-order-status :as use-case]
            [orders.adapters.outbound.order-response-adapter :as order-response]
            [orders.infra.http.response-helper :as response]
            [orders.infra.http.handler-exception-resolver :as exception-resolver]))

(defn process
  [{:keys [repository event-publisher]}]
  (fn [request]
    (try
      (let [order-id (get-in request [:path-params :id])
            new-status (keyword (get-in request [:body :status]))
            result (use-case/execute repository event-publisher order-id new-status)
            error-status (if (= "Order not found" (:error result)) 404 400)]
        (if (:success result)
          (response/success
           {:message "Status updated"
            :order (order-response/order->json (:order result))}
           200)
          (response/error (:error result) error-status)))
      (catch Exception e
        (exception-resolver/to-response e)))))
