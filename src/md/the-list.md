# The List

**TL;DR** <a href="#putting-it-together">Skip to the example</a>

NSFW provides you an imperitive interface to DOM maniupulation and
event binding in ClojureScript through the `nsfw.dom` namespace.

Imperitive interaction with DOM querying, modification, and event
binding is what we've been doing using jQuery & friends since 2006. No
suprises here, other than the use of **Clojure data structures as a
templating DSL**.


    ($/node [:h1 "Hello"]) ;=> [object HTMLHeadingElement]

    ($/query :body) ;=> [object HTMLBodyElement]

    ;; All together now!

    (-> ($/node [:h1 "Hello"])
      ($/mouseover #($/style %2 {:background-color "#"}))
      ($/mouseout #($/style %2 {:background-color nil}))
      ($/append-to ($/query :#example1)))

    ;; Which Produces (mouse over to test):

<div class="demo-example" id="the-list-example-1"></div>


## Data-Binding Using Atoms

Add **data-binding** into the mix with **DOM manipulation** and
**event-binding**, and you've got the Holy Trinity of web
programming. Or maybe it's the Devil's Three-Way, and the Holy Trinity
is HTML, CSS, and JS. Whatever.

The point is, one way to manage state in your application is to have a
backing data-structure for your presentation; changes to this data
structure are reflected somehow in the UI (and in your database, but
that's a different example). Atoms are one way to accomplish this in
ClojureScript.

Binding functionality is provided by the `nsfw.bind` namespace.

    (-> (let [data (atom (str (js/Date.)))
              $el ($/node [:h1 @data])]
          (bind/change data (fn [atom old new] ;; <-- update UI on data change
                              ($/text $el new)))
          (util/interval #(reset! data (str (js/Date.))) 1000)
          $el)
        ($/append-to ($/query :#example2)))

    ;; Produces:

<div class="demo-example" id="example2" style="text-align: center;"></div>


## <span id="putting-it-together">Putting It All Together</a>

Here's a quick todo app demo using imperitive dom manipulation and
event binding. It's quick, it's dirty, and it works.

<div id="todoapp" class="demo-example"></div>


### todo.cljs (minus non-todo parts)

    (ns nsfw-site.demos.todo
      (:require [nsfw.dom :as $]
                [nsfw.util :as util]
                [nsfw.bind :as bind]))

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


While succint and comparable to the equivilent JS code (length- & weight-wise), this isn't a very introspectable way to write your frontend code. It's difficult to grok, test, and maintain.

For a more NSFW-idiomatic way to build this todo widget, see <a href="#">here.</a>
