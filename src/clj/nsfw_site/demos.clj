(ns nsfw-site.demos
  (:require [nsfw]))

(nsfw/defcomp demo-nav [{:keys [subnav tab]} _]
  [:ul.nav.demo-nav
   {:data-offset-top 200
    :data-spy "affix"}
   (map (fn [{:keys [name tab-name href]}]
          (let [active? (= tab tab-name)]
            [:li {:class (when active? "active")}
             [:a {:href href} name]
             (when (and active? (not (empty? subnav)))
               [:ul.on-page-nav
                (map (fn [[href text]]
                       [:li [:a.scroll-to {:href href} text]])
                     (partition 2 subnav))])]))
        nav)])

(nsfw/defcomp demo [{:keys [tab js-entry nav subnav] :as attrs} body]
  [:page-body
   {:class "page-demo"
    :active-tab :examples
    :script "/js/app.js"
    :js-entry js-entry}
   [:div.container
    [:div.row
     [:div.col-lg-12
      [:div.page-lead
       [:h1 "Examples"]
       [:p.lead "Bite-size concepts from the NSFW framework."]]]]
    [:div.row
     [:div.col-lg-3
      [:demo-nav attrs]]
     [:div.col-lg-9.demo-content
      body]]]])

(def nav [{:path "getting-started"
           :name "Getting Started"
           :md "src/md/getting-started.md"
           :tab-name :getting-started
           :href "/examples/getting-started"
           :subnav ["#intro"              "Intro"
                    "#the-nsfw-namespace" "The NSFW Namespace"
                    "#starting-an-app"    "Starting an App"
                    "#rendering-html"     "Rendering HTML"
                    "#app-config"         "App Config"
                    "#components"         "Components"
                    "#static-pages"       "Static Pages"]
           :js-entry 'nsfw-site.app.entry}

          {:path "autoload"
           :name "Code Reloading"
           :md "src/md/autoload.md"
           :subnav ["#intro"       "Intro"
                    "#background"  "Under The Hood"]
           :js-entry 'nsfw-site.app.entry
           :tab-name :autoload
           :href "/examples/autoload"}

          {:path "the-list"
           :name "The List"
           :md "src/md/the-list.md"
           :subnav ["#intro"        "Intro"
                    "#data-binding" "Data Binding"
                    "#all-together" "All Together"
                    "#code"         "Code"]
           :js-entry 'nsfw-site.demos.thelist.main
           :tab-name :the-list
           :href "/examples/the-list"}

          {:path "the-list-redux"
           :name "The List (Redux)"
           :md "src/md/the-list-redux.md"
           :subnav ["#intro"          "Intro"
                    "#starting-small" "Starting Small"
                    "#all-together"   "All Together"
                    "#code"           "Code"]
           :js-entry 'nsfw-site.demos.redux.main
           :tab-name :the-list-redux
           :href "/examples/the-list-redux"}])

(defn render-example-page
  [{:keys [name md subnav js-entry tab-name] :as page} nav]
  (nsfw/render-html
   [:default-head]
   [:demo
    {:js-entry (or js-entry 'nsfw-site.app.entry)
     :nav nav
     :subnav subnav
     :tab tab-name}
    [:markdown {:src md}]]))

(nsfw/defroute "/examples/:example"
  examples [{:keys [route-params] :as r}]
  (let [example (:example route-params)
        page-data (->> nav
                       (filter #(= example (:path %)))
                       first)]
    (when page-data
      (-> page-data
          (render-example-page nav)))))