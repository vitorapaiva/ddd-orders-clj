(ns orders.adapters.outbound.products-adapter-test
  (:require [clojure.test :refer [deftest testing is]]
            [orders.adapters.outbound.products-adapter :as adapter]))

(deftest items->request-test
  (testing "builds request map"
    (is (= {:order-id "ord-1" :items [{:product-id "p1" :quantity 2}]}
           (adapter/items->request "ord-1" [{:product-id "p1" :quantity 2}])))))

(deftest response->result-test
  (testing "communication error returns failure"
    (is (= {:success false :error "Connection refused"}
           (adapter/response->result {:error "Connection refused"}))))
  (testing "200 status returns success with products"
    (is (= {:success true :products [{:id "p1"}]}
           (adapter/response->result {:status 200 :body {:products [{:id "p1"}]}}))))
  (testing "non-200 returns unknown status"
    (is (= {:success false :error "Status desconhecido"}
           (adapter/response->result {:status 404 :body nil})))))

(deftest release-response->result-test
  (testing "communication error returns failure"
    (is (= {:success false :error "Timeout"}
           (adapter/release-response->result {:error "Timeout"}))))
  (testing "200 status returns success"
    (is (= {:success true}
           (adapter/release-response->result {:status 200}))))
  (testing "non-200 returns unknown status"
    (is (= {:success false :error "Status desconhecido"}
           (adapter/release-response->result {:status 500})))))
