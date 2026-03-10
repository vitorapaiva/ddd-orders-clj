(ns orders.infra.http.products-client
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [orders.ports.outbound :as ports]
            [orders.adapters.outbound.products-adapter :as adapter]
            [orders.infra.http.products-http-config :as http-config]))

(defn- http-request
  [base-url method path opts]
  (try
    (method (http-config/request-url base-url path)
            (merge http-config/default-options opts))
    (catch Exception e
      {:status 0 :error (str "Communication error: " (.getMessage e))})))

(defrecord HTTPProductsClient [base-url]
  ports/ProductsService
  
  (reserve-products [_ order-id items]
    (let [request-body (adapter/items->request order-id items)
          response (http-request base-url http/post "/products/reserve"
                                 {:body (json/generate-string request-body)})
          body (when (:body response)
                 (json/parse-string (:body response) true))]
      (if (:error response)
        {:success false :error (:error response)}
        (adapter/response->result (:status response) body))))
  
  (release-reservation [_ order-id]
    (let [response (http-request base-url http/delete
                                 (str "/products/reserve/" order-id))]
      (if (:error response)
        {:success false :error (:error response)}
        (if (= 200 (:status response))
          {:success true}
          {:success false :error "Failed to release reservation"})))))

(defn create-client
  [base-url]
  (->HTTPProductsClient base-url))
