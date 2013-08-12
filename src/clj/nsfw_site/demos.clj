(ns nsfw-site.demos
  (:require [nsfw]))




(defn make-tab [{:keys [name target-tab selected-tab href on-page-nav]}]
  (let [active? (= target-tab selected-tab)]
    [:li {:class (when active? "active")}
     [:a {:href href} name]
     (when (and active? on-page-nav)
       [:ul.on-page-nav
        (map (fn [{:keys [href text]}]
               [:li [:a {:href href} text]])
             on-page-nav)])]))

(nsfw/defcomp demo-nav [{:keys [tab on-page-nav]} body]
  [:ul.nav.demo-nav
   {:data-offset-top 200
    :data-spy "affix"}
   (map make-tab
        [{:name "Autoload"
          :target-tab :autoload
          :selected-tab tab
          :href "/examples/autoload"
          :on-page-nav on-page-nav}
         {:name "The List"
          :target-tab :the-list
          :selected-tab tab
          :href "/examples/the-list"
          :on-page-nav on-page-nav}
         {:name "The List (Redux)"
          :target-tab :the-list-redux
          :selected-tab tab
          :href "/examples/the-list-redux"
          :on-page-nav on-page-nav}])

   #_[:li.the-list-redux {:class (when (= :the-list-redux tab) "active")}
    [:a {:href "/examples/the-list-redux"} "The List (Redux)"]
    (when (and (= :the-list-redux tab)
               on-page-nav)
      [:ul.on-page-nav
       (map (fn [{:keys [href text]}]
              [:li [:a {:href href} text]])
            on-page-nav)])]])

(nsfw/defcomp demo [{:keys [tab js-entry nav]} body]
  [:page-body
   {:class "page-demo"
    :active-tab :demos
    :script "/js/app.js"
    :js-entry js-entry}
   [:div.container
    [:div.row
     [:div.col-lg-12
      [:div.page-lead
       [:h1 "Demos"]
       [:p.lead "Bite-size examples of the NSFW framework."]]]]
    [:div.row
     [:div.col-lg-3
      [:demo-nav {:tab tab}]]
     [:div.col-lg-9.demo-content
      body]]]])

(def nav {"autoload"
          {:name "Autoload"
           :md "src/md/autoload.md"
           :subnav [["#intro"       "Intro"]
                    ["#background"  "Background"]]}

          "the-list"
          {:name "The List"
           :md "src/md/the-list.md"
           :subnav [["#intro"       "Intro"]]
           :js-entry 'nsfw-site.demos.thelist.main}

          "the-list-redux"
          {:name "The List (Redux)"
           :md "src/md/the-list-redux.md"
           :js-entry 'nsfw-site.demos.redux.main}})

(defn render-example-page [{:keys [name md subnav] :as page}]
  (nsfw/render
   [:default-head]
   [:demo

    [:markdown {:src md}]]))

(nsfw/defroute "/examples/:example"
  examples [{:keys [route-params] :as r}]
  (let [example (:example route-params)
        page-data (get nav example)]
    (when page-data
      (-> page-data
          render-example-page))))

(nsfw/defroute "/examples/the-list"
  the-list [r]
  (nsfw/render
   [:default-head]
   [:demo
    {:tab :the-list
     :js-entry "nsfw_site.demos.thelist.main()"}

    [:page-nav-item {:href "#intro"} "Intro"]
    [:page-nav-item {:href "#data-binding"} "Data Binding"]
    [:page-nav-item {:href "#all-together"} "All Together"]

    [:div#the-list
     [:markdown {:src "src/md/the-list.md"}]]]))

(nsfw/defroute "/examples/the-list-redux"
  the-list-redux [r]
  (nsfw/render
   [:default-head]
   [:demo
    {:tab :the-list-redux
     :js-entry "nsfw_site.demos.redux.main()"
     :on-page-nav [{:href "#intro"
                    :text "Intro"}
                   {:href "#starting-small"
                    :text "Starting Small"}]}
    [:div#the-list
     [:markdown {:src "src/md/the-list-redux.md"}]]]))

(nsfw/defroute "/examples/data-binding"
  data-binding [r]
  (nsfw/render
   [:default-head]
   [:demo
    {:tab :the-list-redux
     :js-entry "nsfw_site.demos.redux.main()"}
    [:div#the-list
     [:markdown {:src "src/md/the-list-redux.md"}]]]))

(nsfw/defroute "/examples/autoload"
  autoload [r]
  (nsfw/render
   [:default-head]
   [:demo
    {:tab :autoload
     :js-entry 'nsfw-site.app.entry
     :on-page-nav [{:href "#intro"
                    :text "Intro"}
                   {:href "#under-the-hood"
                    :text "Background"}]}
    [:div#autoload
     [:markdown {:src "src/md/autoload.md"}]]]))