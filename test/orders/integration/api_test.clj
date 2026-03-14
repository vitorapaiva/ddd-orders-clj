(ns orders.integration.api-test
  (:require [clojure.test :refer [deftest testing is]]
            [cheshire.core :as json]
            [ring.mock.request :as mock]
            [orders.infra.http.server :as server]
            [orders.test-helpers :as helpers]
            [orders.infra.event-handlers.order-created-handler :as order-created-handler]
            [orders.infra.event-handlers.order-updated-handler :as order-updated-handler]
            [orders.infra.event-publisher :as event-publisher]))

(defn create-test-deps
  []
  (let [repo (helpers/create-in-memory-repository)
        products-service (helpers/create-mock-products-service)
        order-created (order-created-handler/create-handler products-service)
        order-updated (order-updated-handler/create-handler)
        publisher (event-publisher/create-publisher order-created order-updated)]
    {:repository repo
     :event-publisher publisher}))

(defn app
  []
  (server/create-app (create-test-deps)))

(defn body
  "Parses response body from JSON string to map."
  [response]
  (let [b (:body response)]
    (if (string? b) (json/parse-string b true) b)))

(def close-order-body
  {:customer-id "cust-1"
   :shipping-address {:street-type "Rua" :street-name "X" :number "1" :complement nil
                     :district "Y" :city "Z" :state "SP" :zip-code "01234-567"}
   :billing-address {:street-type "Rua" :street-name "X" :number "1" :complement nil
                     :district "Y" :city "Z" :state "SP" :zip-code "01234-567"}
   :items [{:product-id "p1" :quantity 2 :price 10.0}]})

(deftest post-order-close-test
  (testing "creates order and returns 201"
    (let [request (-> (mock/request :post "/order/close")
                     (mock/content-type "application/json")
                     (mock/body (json/generate-string close-order-body)))
          response ((app) request)]
      (is (= 201 (:status response)))
      (is (some? (get-in (body response) [:order :id])))
      (is (= "Order created successfully" (get-in (body response) [:message])))))
  (testing "invalid body returns 400"
    (let [request (-> (mock/request :post "/order/close")
                     (mock/content-type "application/json")
                     (mock/body (json/generate-string {:customer-id nil :items []})))
          response ((app) request)]
      (is (= 400 (:status response))))))

(deftest get-orders-test
  (testing "returns empty list when no orders"
    (let [request (mock/request :get "/orders")
          response ((app) request)]
      (is (= 200 (:status response)))
      (is (vector? (get-in (body response) [:orders])))
      (is (empty? (get-in (body response) [:orders]))))))

(deftest get-order-by-id-test
  (testing "returns 404 when order not found"
    (let [request (mock/request :get "/orders/non-existent-id")
          response ((app) request)]
      (is (= 404 (:status response))))))

(deftest put-order-status-test
  (testing "returns 404 when order not found"
    (let [request (-> (mock/request :put "/orders/non-existent-id/status")
                     (mock/content-type "application/json")
                     (mock/body (json/generate-string {:status "payment-processed"})))
          response ((app) request)]
      (is (= 404 (:status response))))))

(deftest full-flow-test
  (testing "create order, list, get by id, update status"
    (let [handler (app)
          create-req (-> (mock/request :post "/order/close")
                        (mock/content-type "application/json")
                        (mock/body (json/generate-string close-order-body)))
          create-resp (handler create-req)
          order-id (get-in (body create-resp) [:order :id])]
      (is (= 201 (:status create-resp)))
      (is (some? order-id))
      (let [list-resp (handler (mock/request :get "/orders"))]
        (is (= 200 (:status list-resp)))
        (is (= 1 (count (get-in (body list-resp) [:orders])))))
      (let [get-req (mock/request :get (str "/orders/" order-id))
            get-resp (handler get-req)]
        (is (= 200 (:status get-resp)))
        (is (= order-id (get-in (body get-resp) [:order :id]))))
      (let [update-req (-> (mock/request :put (str "/orders/" order-id "/status"))
                           (mock/content-type "application/json")
                           (mock/body (json/generate-string {:status "payment-processed"})))
            update-resp (handler update-req)]
        (is (= 200 (:status update-resp)))
        (is (= "payment-processed" (get-in (body update-resp) [:order :status])))))))
