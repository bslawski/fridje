(ns fridje.config)


(defmulti env-var
  (fn [var-name]
    (keyword var-name)))

(defmethod env-var :db-file
  [_]
  (or
    (System/getenv "DB_FILE")
    "fridje.db"))
