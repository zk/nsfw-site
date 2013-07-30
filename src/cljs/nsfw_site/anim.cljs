(ns nsfw-site.anim
  (:require [nsfw.util :as util]
            [nsfw.dom :as dom]
            #_[nsfw.ani :as ani]
            [nsfw.components :as comp]
            [nsfw-site.common :as common]
            [nsfw-site.test :as test]))

(defn header-comp [title content example]
  [:header.navbar.container
   [:h1 header-brand title]])

(defn container-comp [& content]
  [:div.container
   content])

(def content
  [:div.anim-page
   common/$navbar
   [:div.page-content
    [:div.row
     [:div.col.span6
      [:h1.header-brand "CSS3 Transitions"]
      [:p "NSFW provies a lightweight, composible set of functions for applying CSS3 transforms to elements in the DOM."]
      [:p
       "CSS3 transitions provide a declarative way to assign transformation properties to DOM elements apart from which properties you want to transition. Applying a transition of "
       [:code "width 1s ease"]
       " specifies you'd like all subsequent width changes to be animated over a duration of 1 second, with easing."]]
     [:div.col.span6
      (test/content
       #(let [el (dom/$ [:div {:style {:width "10%"
                                       :height "100px"
                                       :background-color "#f55"}}])]
          (util/timeout
           (fn []
             (dom/trans el
                        {:width "100%"
                         :background-color "#5f5"
                         :ease :ease
                         :dur "0.5s"}
                        {:background-color "#55f"
                         :width "10%"
                         :position "relative"
                         :margin-left "-10%"
                         :left "100%"
                         :ease :ease
                         :dur "0.5s"}))
           500)
          el))]
     [:div.col.span6
      example]]
    [:div.row
     [:h2 "Examples"]]
    (test/section
     {:title   "Width"
      :code    (str "(dom/trans\n"
                    "  $el\n"
                    "  {:width \"100%\"\n"
                    "   :ease :ease\n"
                    "   :done #(util/log \"done\")})")
      :run     #(let [el (dom/$ [:div {:style {:width "10%"
                                               :height "100px"
                                               :background-color "#aaa"}}])]
                  (util/timeout
                   (fn []
                     (dom/trans el
                                {:width "100%"
                                 :done (fn [] (util/log "done"))}))
                   500)
                  el)})

    (test/section
     {:title   "Multiple Properties"
      :code    (str "(dom/trans\n"
                    "  $el\n"
                    "  {:width \"100%\"\n"
                    "   :opacity 0.5\n"
                    "   :ease :ease\n"
                    "   :done #(util/log \"done\")})")
      :run     #(let [el (dom/$ [:div {:style {:width "10%"
                                               :height "100px"
                                               :background-color "#aaa"}}])]
                  (util/timeout
                   (fn []
                     (dom/trans el
                                {:width "100%"
                                 :background-color "#55f"
                                 :done (fn [] (util/log "done"))}))
                   500)
                  el)})
    (let [easing-functions [:linear :ease :ease-in :ease-out :ease-in-out]]
      (test/section
       {:title "Easing"
        :desc [:div
               "Possible values for the " [:code ":ease"] " key are: "
               (->> easing-functions
                    (map str)
                    (map #(vector :code %))
                    (interpose ", "))]
        :run (fn []
               [:div.easing
                (map (fn [ease]
                       (let [el (dom/$ [:div {:style {:width "70px"
                                                      :background-color "#aaa"
                                                      :text-align "center"
                                                      :padding "0.5em"
                                                      :font-size "0.7em"}}
                                        (name ease)])]
                         (util/timeout (fn []
                                         (dom/trans el
                                                    {:left "100%"
                                                     :margin-left "-70px"
                                                     :position "relative"
                                                     :ease ease}))
                                       500)
                         [:div el]))
                     easing-functions)])}))
    (test/section
     {:title "Chaining Transitions"
      :desc [:p [:code "trans"] " is varardic -- parameters past the first are considered transition states and are run consecutively."]
      :code (str "(dom/trans $el\n"
                 "  {:width \"100%\" :dur \"0.5s\"}\n"
                 "  {:width \"50%\"\n"
                 "   :background-color \"#5f5\"\n"
                 "   :dur \"1s\"})\n")
      :run (fn []
             (let [el (dom/$ [:div {:style {:width "10%"
                                            :height "100px"
                                            :background-color "#f55"}}])]
               (util/timeout
                (fn []
                  (dom/trans el
                             {:width "100%" :dur "0.5s"}
                             {:width "50%" :dur "1s"
                              :background-color "#5f5"}))
                500)
               el))})]])

(defn app []
  (-> (dom/$ "body")
      (dom/apd content)))
