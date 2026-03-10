(ns orders.domain.value-objects.address
  "Value object: endereço. SRP: única responsabilidade de representar e validar endereço.")

(defn- validate
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
    (validate a)
    a))
