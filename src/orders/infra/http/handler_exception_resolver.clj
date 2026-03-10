(ns orders.infra.http.handler-exception-resolver
  "Resolve exceções para respostas HTTP. SRP: única responsabilidade de mapear erros."
  (:require [orders.infra.http.response-helper :as response]
            [clojure.string :as str]))

(defn to-response
  "Mapeia exceção para resposta HTTP {:status :body}."
  [e]
  (cond
    (instance? IllegalArgumentException e)
    (response/error "Invalid status" 400)

    (instance? clojure.lang.ExceptionInfo e)
    (let [msg (.getMessage e)
          status (if (str/includes? (str msg) "not found") 404 400)]
      (response/error msg status))

    :else
    (response/error "Internal server error" 500)))
