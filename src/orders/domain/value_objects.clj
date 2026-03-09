(ns orders.domain.value-objects)

(defn- validate-address
  [address]
  (when-not (and (some? (:street-type address))
                 (some? (:street-name address))
                 (some? (:number address))
                 (some? (:district address))
                 (some? (:city address))
                 (some? (:state address))
                 (some? (:zip-code address)))
    (throw (ex-info "Invalid address: required fields not filled" {:address address}))))

(defn address
  [{:keys [street-type street-name number complement district city state zip-code]}]
  (let [a {:street-type street-type
           :street-name street-name
           :number number
           :complement complement
           :district district
           :city city
           :state state
           :zip-code zip-code}]
    (validate-address a)
    a))

(defn- validate-item
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
    (validate-item i)
    i))

(defn subtotal
  [item]
  (* (:quantity item) (:price item)))

(defn- validate-money
  [m]
  (when-not (and (some? (:amount m))
                 (>= (:amount m) 0))
    (throw (ex-info "Invalid money value" {:money m}))))

(defn money
  [amount]
  (let [m {:amount amount :currency "BRL"}]
    (validate-money m)
    m))
