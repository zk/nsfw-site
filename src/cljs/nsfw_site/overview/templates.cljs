(ns nsfw-site.overview.templates
  (:require [clojure.string :as str]
            [nsfw.dom :as dom]))

(defn title->id
  "Convert a title to kebob-case, used for anchor targets."
  [title]
  (-> (str/lower-case title)
      (str/replace #"\s" "-")
      (str/replace #"/" "")
      (str/replace #"-+" "-")))

(defn section
  "Styleguide section template"
  [title & content]
  (let [id (title->id title)]
    (dom/$ [:div.container.section.child-pad-1em
            [:div.row
             [:div.col.span12
              [:h2 {:id id}
               [:a.section-link {:href (str "#" id)} "link"]
               title]]]
            content])))

(def $hero
  (section "hello world foo bar baz"))
