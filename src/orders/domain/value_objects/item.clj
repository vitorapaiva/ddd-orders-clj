(ns orders.domain.value-objects.item
  "Value object: item do pedido. SRP: única responsabilidade de representar e validar item.")

(defn- validate
  [item]
  (when-not (some? (:product-id item))
    (throw (ex-info "Invalid item: product-id is required" {:item item})))
  (when-not (pos? (:quantity item))
    (throw (ex-info "Invalid item: quantity must be greater than zero" {:item item})))
  (when-not (pos? (:price item))
    (throw (ex-info "Invalid item: price must be greater than zero" {:item item}))))

(defn item
  [{:keys [product-id quantity price]}]
  (let [i {:product-id product-id
           :quantity quantity
           :price price}]
    (validate i)
    i))

(defn subtotal
  [item]
  (* (:quantity item) (:price item)))
