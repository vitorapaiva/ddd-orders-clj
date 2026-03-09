(ns orders.adapters.outbound.products-adapter)

(defn items->request
  [order-id items]
  {:order-id order-id
   :items items})

(defn response->result
  [status body]
  (if (= 200 status)
    {:success true :products (:products body)}
    {:success false :error (or (:error body) "Failed to reserve products")}))
