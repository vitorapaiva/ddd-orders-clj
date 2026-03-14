(ns orders.domain.logic.order
  "Order validation logic.")

(defn validate
  "Validates order: customer-id required, at least one item. Throws ex-info if invalid."
  [order]
  (when-not (some? (:customer-id order))
    (throw (ex-info "Invalid order: customer-id is required" {:order order})))
  (when-not (seq (:items order))
    (throw (ex-info "Invalid order: must contain at least one item" {:order order}))))
