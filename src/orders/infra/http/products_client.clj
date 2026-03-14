(ns orders.infra.http.products-client
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [orders.ports.outbound :as ports]
            [orders.adapters.outbound.products-adapter :as adapter]
            [orders.infra.http.products-http-config :as http-config]))

(defn- http-request
  "Performs HTTP request. Returns {:status :body} on success or {:status 0 :error ...} on exception."
  [base-url method path opts]
  (try
    (method (http-config/request-url base-url path)
            (merge http-config/default-options opts))
    (catch Exception e
      {:status 0 :error (str "Communication error: " (.getMessage e))})))

(defrecord HTTPProductsClient [base-url]
  ports/ProductsService
  
  (reserve-products [_ order-id items]
    (let [response (http-request base-url http/post "/products/reserve"
                                 {:body (json/generate-string (adapter/items->request order-id items))})]
      (adapter/response->result
        (cond-> response (:body response) (update :body #(json/parse-string % true))))))
  
  (release-reservation [_ order-id]
    (let [response (http-request base-url http/delete
                                 (str "/products/reserve/" order-id) {})]
      (adapter/release-response->result response))))

(defn create-client
  "Creates an HTTP client for the products service at base-url."
  [base-url]
  (->HTTPProductsClient base-url))
