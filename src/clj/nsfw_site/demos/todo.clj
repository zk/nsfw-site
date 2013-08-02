(ns nsfw-site.demos.todo
  (:require [nsfw.http :as http]
            [nsfw.html :as html]
            [nsfw-site.demos :as demos]))

(defn body []
  [:div.row
   [:div.col-lg-10
    (-> "src/md/todo.md"
        slurp
        html/markdown)]])

(defn
  ^{:route "/demos/the-list"}
  todo
  [r]
  (-> {:body (body)
       :active-tab :the-list
       :js-entry "nsfw_site.demos.todo.main()"}
      demos/layout
      http/html))

(defn
  ^{:route "/demos/the-list-redux"}
  the-list-redux
  [r]
  (-> {:body [:div.row
              [:div.col-lg-10
               (-> "src/md/the-list-redux.md"
                   slurp
                   html/markdown)]]
       :active-tab :the-list
       :js-entry "nsfw_site.demos.todo.redux()"}
      demos/layout
      http/html))
