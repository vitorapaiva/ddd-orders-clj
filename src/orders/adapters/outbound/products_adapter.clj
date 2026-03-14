(ns orders.adapters.outbound.products-adapter)

(defn items->request
  [order-id items]
  {:order-id order-id
   :items items})

(defn- http-response->result
  "Accepts {:error ...} or {:status ... :body ...}. success-fn receives body and returns map to merge into {:success true}."
  [response success-fn]
  (cond
    (:error response) {:success false :error (:error response)}
    (= 200 (:status response)) (merge {:success true} (success-fn (:body response)))
    :else {:success false :error "Status desconhecido"}))

(defn response->result
  [response]
  (http-response->result response (fn [body] {:products (:products body)})))

(defn release-response->result
  [response]
  (http-response->result response (fn [_] {})))
