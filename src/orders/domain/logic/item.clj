(ns orders.domain.logic.item
  "Order item validation logic.")

(defn validate
  [item]
  (cond
    (nil? (:product-id item))
    (throw (ex-info "Invalid item: product-id is required" {:item item}))

    (not (pos? (:quantity item)))
    (throw (ex-info "Invalid item: quantity must be greater than zero" {:item item}))

    (not (pos? (:price item)))
    (throw (ex-info "Invalid item: price must be greater than zero" {:item item}))))
