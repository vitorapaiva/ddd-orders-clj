(ns orders.infra.http.products-client
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [orders.ports.outbound :as ports]
            [orders.adapters.outbound.products-adapter :as adapter]))

(defrecord HTTPProductsClient [base-url]
  ports/ProductsService
  
  (reserve-products [_ order-id items]
    (try
      (let [request-body (adapter/items->request order-id items)
            response (http/post (str base-url "/products/reserve")
                                {:body (json/generate-string request-body)
                                 :content-type :json
                                 :accept :json
                                 :throw-exceptions false})
            body (when (:body response)
                   (json/parse-string (:body response) true))]
        (adapter/response->result (:status response) body))
      (catch Exception e
        {:success false :error (str "Communication error: " (.getMessage e))})))
  
  (release-reservation [_ order-id]
    (try
      (let [response (http/delete (str base-url "/products/reserve/" order-id)
                                  {:accept :json
                                   :throw-exceptions false})]
        (if (= 200 (:status response))
          {:success true}
          {:success false :error "Failed to release reservation"}))
      (catch Exception e
        {:success false :error (str "Communication error: " (.getMessage e))}))))

(defn create-client
  [base-url]
  (->HTTPProductsClient base-url))
