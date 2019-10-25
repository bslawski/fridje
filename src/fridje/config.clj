(ns fridje.config)

(def db-file
  (or
    (System/getenv "DB_FILE")
    "fridje.db"))
