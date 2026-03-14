(ns orders.domain.logic.item
  "Order item validation logic.")

(defn validate
  [item]
  (when-not (some? (:product-id item))
    (throw (ex-info "Invalid item: product-id is required" {:item item})))
  (when-not (pos? (:quantity item))
    (throw (ex-info "Invalid item: quantity must be greater than zero" {:item item})))
  (when-not (pos? (:price item))
    (throw (ex-info "Invalid item: price must be greater than zero" {:item item}))))
