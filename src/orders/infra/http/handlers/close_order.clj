(ns orders.infra.http.handlers.close-order
  "HTTP handler for closing order. SRP: single endpoint."
  (:require [orders.ports.inbound.close-order :as use-case]
            [orders.adapters.inbound.order-json-adapter :as order-json]
            [orders.adapters.outbound.order-response-adapter :as order-response]
            [orders.infra.http.response-helper :as response]
            [orders.infra.http.handler-exception-resolver :as exception-resolver]))

(defn process
  [{:keys [repository event-publisher]}]
  (fn [request]
    (try
      (let [order-data (order-json/json->order-data (:body request))
            result (use-case/execute repository event-publisher order-data)]
        (response/success
         {:message "Order created successfully"
          :order (order-response/order->json (:order result))}
         201))
      (catch Exception e
        (exception-resolver/to-response e)))))
