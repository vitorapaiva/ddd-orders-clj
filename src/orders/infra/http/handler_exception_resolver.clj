(ns orders.infra.http.handler-exception-resolver
  "Maps exceptions to HTTP responses. SRP: single responsibility for error mapping."
  (:require [orders.infra.http.response-helper :as response]
            [clojure.string :as str]))

(def ^:private exception-handlers
  {IllegalArgumentException (fn [_] [400 "Invalid status"])
   clojure.lang.ExceptionInfo (fn [e]
                                [(if (str/includes? (str (.getMessage e)) "not found") 404 400)
                                 (.getMessage e)])
   :default (fn [_] [500 "Internal server error"])})

(defn to-response
  "Maps exception to HTTP response {:status :body}."
  [e]
  (let [handler (get exception-handlers (type e) (:default exception-handlers))
        [status msg] (handler e)]
    (response/error msg status)))
