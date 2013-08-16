## The List (Redux) <a id="intro"></a>

In the last example, The List was accomplished using direct
manipulation of the DOM, and direct event binding.

Let's explore a more understandable, testable version.

We're still dealing with the fundamental building blocks of dynamic
web applications in our ClojureScript: DOM generation / manipulation,
event-binding, and data-binding (styling is probably not a
responsibility of the ClojureScript

Sometimes you need just the templating, sometimes you need just
databinding. And sometimes you need full-blown bi-directional
communcation and automatic view updates based on some internal state's
last 3 transitions.

Well, we've got you covered.

In other words, NSFW scales up and down nicely.


## Starting Small <a id="starting-small"></a>

NSFW's goal is that you always know what's going on in your app. A big
part of this is turning previously opaque processes like event firing
and DOM transitions and making their changes a first-class
concern. We're bringing the 4th dimension into the foreground.

Say we've got an element that changes it's look based on which of it's
three states (default, hover, or disabled) it's in.

    # CSS
    .hover { font-weight: bold; }
    .disabled { font-weight: normal; color: #aaa; }

    # HTML
    <div class=hover">Hello World</div>

<div class="example">
  <div style="font-weight: bold;">Hello World</div>
</div>

    # Setting a disabled state looks like:
    <div class=hover">Hello World</div>

<div class="example">
  <div style="color: #ccc;">Hello World</div>
</div>



## All Together <a id="all-together"></a>

Here we've got about the same functionality, for less and more
readable code. This example's a bit more full-featured, as we've added
some validation and error handling.

To see this in action, try adding a blank or dupliate todo item.

<div class="example" id="the-list-redux"></div>



## Code <a id="code"></a>

    (defn list-item [!data item-text]
      (-> {}
          (state !data)

          (assoc :item-text item-text)

          (html [:li.item
                 item-text
                 [:span.pull-right "click to delete"]])

          (event :click (fn [e $el {:keys [item-text !state]}]
                          (swap! !state (fn [items]
                                          (remove #(= item-text %) items)))))

          $/build))


    (defn list-redux [!data]
      (-> {}

          ;; The state of our checklist, which is a Clojure vector in an atom.
          (state !data)

          (html [:div
                 [:ul.list-unstyled]
                 [:input.form-control {:type "text"}]
                 [:button.btn.btn-success.pull-right "Add"]])

          (bind (fn [new old {:keys [$el]}]
                  (-> ($/query $el :ul)
                      $/empty
                      ($/append (map #(list-item !data %) new)))))

          (event :input.keyup (fn [{:keys [key-code]} $el]
                                (condp = key-code
                                  13 [:submit-text ($/val $el)]
                                  27 [:reset-input]
                                  nil)))

          (event :button.click (fn [_ _ {:keys [$el]}]
                                 [:submit-text (-> $el
                                                   ($/query :input)
                                                   $/val)]))

          (handle :submit-text (fn [text {:keys [!state $el] :as o}]
                                 (cond
                                  (empty? text)              ($/send o [:input-error])
                                  (some #(= text %) @!state) ($/send o [:input-error])
                                  :else (do
                                          ($/send o [:reset-input])
                                          (swap! !state (fn [items]
                                                          (concat items [text])))))))

          ;; Add an error class on an :input-error message
          (handle :input-error (fn [{:keys [$el]}]
                                 (-> $el
                                     ($/query :input)
                                     ($/add-class :error))))

          (handle :reset-input (fn [{:keys [$el]}]
                                 (-> ($/query $el :input)
                                     ($/val "")
                                     ($/rem-class :error))))

          (handle (fn [msg o]
                    (util/lpr msg)))

          $/build))



## No DOM Manipulation In Event Handlers

## Manipulation as Data

## Transitions as Data

## Overkill? Nah, understandability.
