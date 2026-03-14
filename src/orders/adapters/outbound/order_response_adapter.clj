(ns orders.adapters.outbound.order-response-adapter
  "Adapts domain entities to HTTP JSON response format.")

(defn order->json
  [order]
  {:id (:id order)
   :customer-id (:customer-id order)
   :shipping-address (:shipping-address order)
   :billing-address (:billing-address order)
   :items (:items order)
   :total (:total order)
   :status (name (:status order))
   :created-at (str (:created-at order))
   :updated-at (str (:updated-at order))})

(defn orders->json
  [orders]
  (mapv order->json orders))
