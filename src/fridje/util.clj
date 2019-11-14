(ns fridje.util
  (:require [clojure.java.io :as io]
            [simple-time.core :as stime]
            [taoensso.nippy :as nippy])
  (:import [java.io DataInputStream DataOutputStream]))


(defn current-timestamp
  [& {:keys [timestamp-format] :or {timestamp-format "YYYY-MM-dd HH:mm:ss"}}]
  (stime/format (stime/now) timestamp-format))


(def ^:private random-string-chars
  "abcdefghijklmnopqrstuvwxyz0123456789")

(def ^:private random-string-char-count
  (count random-string-chars))

(defn random-alphanumeric
  "Returns a random case-insensitive alphanumeric string of the given length."
  [length]
  (clojure.string/join
    (repeatedly
      length
      #(.charAt random-string-chars
         (rand-int random-string-char-count)))))


(defn read-file-binary
 [file-path]
 (with-open [reader (io/input-stream file-path)]
   (nippy/thaw-from-in! (DataInputStream. reader))))
