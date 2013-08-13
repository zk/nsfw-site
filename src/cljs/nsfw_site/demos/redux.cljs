(ns nsfw-site.demos.redux
  (:require [nsfw.util :as util]
            [nsfw.affix :as affix]
            [nsfw.dom :as $]
            [nsfw.scroll-to :as scroll-to]))

(defn list-redux []
  (-> {:init (fn [opts]
               ($/node
                [:div
                 [:ul.list-unstyled
                  [:li "hello world"]]]))}
      $/build
      :$el))

(defn html [struct]
  (fn [m]
    (assoc m :init
           (fn [opts]
             ($/node struct)))))

(defn ^:export main []
  (scroll-to/init)
  (affix/init)
  #_($/append ($/query :#the-list-redux)
              (:$el
               ($/build
                (build
                 (html [:h1 "the quick brown fox"])

                 (bind :mouseover [:highlight])
                 (bind :mouseout [:unhighlight])

                 ))))

  #_(-> ($/build
         {:init (fn [opts]
                  ($/node [:h1 "hello world build"]))
          :events [{:event :mouseover
                    :transform (fn [e $el] :mouseover)}
                   {:event :mouseout
                    :transform (fn [e $el] :mouseout)}]
          :msg-handler (fn [msg]
                         (condp = msg
                           :mouseover {:style {:background-color "blue"}}
                           :mouseout {:style {:background-color "white"}}))})
        :$el
        ($/append-to ($/query :#the-list-redux)))
  #_($/append ($/query :#the-list-redux) (todo-list)))
