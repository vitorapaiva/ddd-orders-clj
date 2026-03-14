(ns orders.domain.value-objects.address
  "Value object: address. SRP: single responsibility for representing address."
  (:require [orders.domain.logic.address :as logic]))

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
    (logic/validate a)
    a))
