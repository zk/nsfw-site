# The List (Redux)

In the last example, The List was accomplished using direct manipulation of the DOM, and direct event binding.

Let's explore a more understandable, testable version.

We're still dealing with the fundamental building blocks of dynamic web applications in our ClojureScript: DOM generation / manipulation, event-binding, and data-binding (styling is probably not a responsibility of the ClojureScript

Sometimes you need just the templating, sometimes you need just databinding. And sometimes you need full-blown bi-directional communcation and automatic view updates based on some internal state's last 3 transitions.

Well, we've got you covered.

In other words, NSFW scales up and down nicely.


## Starting Small

NSFW's goal is that you always know what's going on in your app. A big part of this is turning previously opaque processes like event firing and DOM transitions and making their changes a first-class concern. We're bringing the 4th dimension into the foreground.

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
