(ns nsfw-site.demos.todo
  (:require [nsfw.dom :as $]
            [nsfw.util :as util]
            [nsfw.bind :as bind]
            [nsfw-site.demos.todo-redux :as redux]))

(defn render-item [item]
  (let [$el ($/node [:li.item
                     item
                     [:span.pull-right "click to delete"]])]
    ;; Fire custom event on delete click
    ($/click $el
             (fn [e $el]
               ($/prevent e)
               ($/fire $el :remove-item {:item item})))
    $el))

(defn render-items [!items]
  (let [$el ($/node [:ul.list-unstyled (map render-item @!items)])]
    ;; Re-render list when items are added / removed.
    (bind/change !items (fn [a old new]
                          (-> $el
                              $/empty
                              ($/append (map render-item new)))))
    ($/listen $el
              :remove-item
              (fn [{:keys [item] :as event} $el]
                (swap! !items (fn [items]
                                (remove #(= item %) items)))))
    $el))

(defn todo-input [!items]
  (let [$el ($/node [:div.clearfix
                     [:input.form-control {:type "text"
                                           :autofocus "autofocus"
                                           :placeholder "Add Todo Item"}]
                     [:button.btn.btn-success.pull-right "Add"]])]
    ($/keyup ($/query $el :input)
             (fn [{:keys [key-code] :as e} $el]
               ($/prevent e)
               (when (= 13 key-code)
                 (swap! !items concat [($/val $el)])
                 ($/val $el ""))))
    ($/click ($/query $el :button)
             (fn [e]
               (let [input ($/query $el :input)]
                 ($/prevent e)
                 (swap! !items concat [($/val input)])
                 ($/val input ""))))
    $el))

(defn todo-app [!items]
  [:div
   (render-items !items)
   (todo-input !items)])


(defn example1 []
  (-> ($/node [:h1 "Hello"])
      ($/mouseover #($/style %2 {:background-color :#2980B9
                                 :color :white}))
      ($/mouseout #($/style %2 {:background-color nil
                                :color :black}))
      ($/append-to ($/query :#example1))))


(defn example2 []
  (-> (let [data (atom 0)
            $el ($/node [:h1 @data])]
        (bind/change data #($/text $el %3))
        (util/interval #(swap! data inc) 1000)
        $el)
      ($/append-to ($/query :#example2))))

(defn ^:export main []
  (example1)
  (example2)
  (.highlight js/SyntaxHighlighter)
  (-> ($/query :#todoapp)
      ($/append (todo-app (atom ["the quick"
                                 "brown fox"
                                 "jumps over the"
                                 "lazy dog"])))))

(defn ^:export redux []
  (redux/entry))