(ns orders.infra.http.handlers.get-order
  "Handler HTTP para obter pedido por ID. SRP: único endpoint."
  (:require [orders.ports.inbound.get-order :as use-case]
            [orders.adapters.outbound.order-response-adapter :as order-response]
            [orders.infra.http.response-helper :as response]
            [orders.infra.http.handler-exception-resolver :as exception-resolver]))

(defn create
  "Cria handler. DIP: recebe deps (repository)."
  [{:keys [repository]}]
  (fn [request]
    (try
      (let [order-id (get-in request [:path-params :id])
            result (use-case/execute repository order-id)]
        (if (:success result)
          (response/success {:order (order-response/order->json (:order result))} 200)
          (response/error (:error result) 404)))
      (catch Exception e
        (exception-resolver/to-response e)))))
