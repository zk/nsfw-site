(ns nsfw-site.demos.todo-redux
  (:require [nsfw.util :as util]
            [nsfw.dom :as $]))

(defn entry []
  (-> ($/build
       {:init (fn [opts]
                ($/node [:h1 "hello world build"]))
        :events [{:event :click
                  :transform (fn [e $el] :h1-clicked)}]
        :msg-handler (fn [msg]
                       [{:text "hi"}
                        {:style {:background-color "blue"}}])})
      :$el
      ($/append-to ($/query :#example1))))
