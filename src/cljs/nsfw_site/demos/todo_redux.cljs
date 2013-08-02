(ns nsfw-site.demos.todo-redux
  (:require [nsfw.dom :as $]))

(defn entry []
  (-> ($/node [:h1 "hello world"])
      ($/append-to ($/query :#example1))))
