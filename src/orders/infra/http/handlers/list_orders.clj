(ns orders.infra.http.handlers.list-orders
  "Handler HTTP para listar pedidos. SRP: único endpoint."
  (:require [orders.ports.inbound.list-orders :as use-case]
            [orders.adapters.outbound.order-response-adapter :as order-response]
            [orders.infra.http.response-helper :as response]
            [orders.infra.http.handler-exception-resolver :as exception-resolver]))

(defn create
  "Cria handler. DIP: recebe deps (repository)."
  [{:keys [repository]}]
  (fn [_request]
    (try
      (let [result (use-case/execute repository)]
        (response/success
         {:orders (order-response/orders->json (:orders result))}
         200))
      (catch Exception e
        (exception-resolver/to-response e)))))
