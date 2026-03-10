(ns orders.adapters.inbound.order-adapter
  (:require [orders.domain.value-objects.address :as address]
            [orders.domain.value-objects.item :as item]
            [cheshire.core :as json]))

(defn json->order-data
  [data]
  {:customer-id (:customer-id data)
   :shipping-address (address/address (:shipping-address data))
   :billing-address (address/address (:billing-address data))
   :items (mapv item/item (:items data))})

(defn db->order
  [row]
  (when row
    {:id (:id row)
     :customer-id (:customer_id row)
     :shipping-address (json/parse-string (:shipping_address row) true)
     :billing-address (json/parse-string (:billing_address row) true)
     :items (json/parse-string (:items row) true)
     :total (:total row)
     :status (keyword (:status row))
     :created-at (:created_at row)
     :updated-at (:updated_at row)}))
