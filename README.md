# naan

Naan provides CRUD functions for working with Korma entities.

What is naan?  It isn't korma.  It doesn't replace korma.  It doesn't wrap around korma.  It's just bread... er... CRUD for Korma.  It is a nice-to-have that goes well with korma.  Korma is excellent and does many things very well.  However, I do find that for create, read, update, and destroy operations, I really want to DRY-up my code.  Nann provides these four functions, and a few more helpers.  Try naan with korma, its satisfying... tasty even.

## Usage

### Include korma and naan

```clojure
(ns some.namespace
  (:require
    [korma.core :as korma]
    [korma.db :as db]
    [naan.core :as naan]))
```

### CRUD with korma and naan


Use defdb and defentity as normal.

```clojure
(korma/defentity users
  (korma/entity-fields :id :first :last)
  (korma/pk :id)
  (korma/database db))
```

You could create an instance of the entity using korma.

```clojure
(korma/insert users
  (korma/values {:first "Tasty", :last "SQL"}))
```

That's not bad.  But naan is a bit shorter.

```clojure
(naan/create users {:first "Tasty", :last "SQL"})
```

Meh.  That isn't much different.  Now read that user using a korma select.

```clojure
(first
  (korma/select users
    (korma/where {:id [= 1]})
    (korma/limit 1)))
```

And using naan read.

```clojure
(naan/read users 1)
```

That's a very straightforward helper function.  Let's see updating with korma update.


```clojure
(korma/update users
  (korma/set-fields {:last "Korma"})
  (korma/where {:id [= 1]}))
```

Naan is shorter.

```clojure
(naan/update users {:last "Korma"} 1)
```

Finally destroying in korma.

```clojure
(korma/delete users
  (korma/where {:id [= 1]}))
```

And again with naan.

```clojure
(naan/destroy users 1)
```

CRUD with naan makes korma more satisfying.

### String keys

It is pretty common to use an 'id' column on our tables.  It seems like we also commonly give things names, and CRUD should just work.

```clojure
(korma/defentity cats
  (korma/entity-fields :name :breed :color :gender)
  (korma/pk :id)
  (korma/database db))

(naan/create cats {:name "Crookshanks", :breed "Tabby", :color "Orange", :gender "M"})
(naan/read cats "Crookshanks")
```

We should also be able to specify an alternate string key.

```clojure
(korma/defentity cats
  (korma/entity-fields :nickname :breed :color :gender)
  (naan/set-string-key :nickname)
  (korma/pk :id)
  (korma/database db))

  (naan/create cats {:nickname "Crookshanks", :breed "Tabby", :color "Orange", :gender "M"})
  (naan/read cats "Crookshanks")
```

Or maybe we just want to override the default.

```clojure
(korma/defentity cats
  (korma/entity-fields :nickname :owner :breed :color :gender)
  (naan/set-string-key :nickname)
  (korma/pk :id)
  (korma/database db))

  (naan/create cats {:name "Crookshanks", :owner "Granger" :breed "Tabby", :color "Orange", :gender "M"})
  (binding [*string-key* :owner] (naan/read cats "Granger"))
```

## Credit and copyright

Naan is Copyright © 2013 Stephen Sloan, and is funded by [Rafter.com](http://www.rafter.com "Rafter.com")

![Rafter Logo](http://rafter-logos.s3.amazonaws.com/rafter_github_logo.png "Rafter")


## License

Released under a MIT style License, see LICENSE for details.
