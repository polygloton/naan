# naan

Naan provides CRUD functions for working with Korma entities.

What is naan?  It isn't korma.  It doesn't replace korma.  It doesn't wrap around korma.  It's just bread... er... CRUD for Korma.  It is a nice-to-have that goes well with korma.  Korma is excellent and does many things very well.  However, I do find that for create, read, update, and destroy operations, I really want to DRY-up my code.  Nann provides these four functions, and a few more helpers.  Try naan with korma, its satisfying... tasty even.

## CRUD, korma and naan

Use defdb and defentity as normal.

```clojure
(defentity users
  (entity-fields :id :first :last)
  (pk :id)
  (database db))
```

You could create an instance of the entity using korma.

```clojure
(insert users
  (values {:first "Tasty", :last "SQL"}))
```

That's not bad.  But naan is a bit shorter.

```clojure
(create users {:first "Tasty", :last "SQL"})
```

Meh.  That isn't much different.  Now read that user using a korma select.

```clojure
(first
  (select users
    (where {:id [= 1]})
    (limit 1)))
```

And using naan read.

```clojure
(read users 1)
```

That's a very straightforward helper function.  Let's see updating with korma update.


```clojure
(update users
  (set-fields {:last "Korma"})
  (where {:id [= 1]}))
```

Naan is shorter.

```clojure
(update users {:last "Korma"} 1)
```

Finally destroying in korma.

```clojure
(delete users
  (where {:id [= 1]}))
```

And again with naan.

```clojure
(destroy users 1)
```

CRUD with naan makes korma more satisfying.

## String keys

It is pretty common to use an 'id' column on our tables.  It seems like we also commonly give things names, and CRUD should just work.

```clojure
(defentity cats
  (entity-fields :name :breed :color :gender)
  (pk :id)
  (database db))

(create cats {:name "Crookshanks", :breed "Tabby", :color "Orange", :gender "M"})
(read cats "Crookshanks")
```

We should also be able to specify an alternate string key.

```clojure
(defentity cats
  (entity-fields :nickname :breed :color :gender)
  (set-string-key :nickname)
  (pk :id)
  (database db))

  (create cats {:nickname "Crookshanks", :breed "Tabby", :color "Orange", :gender "M"})
  (read cats "Crookshanks")
```

Or maybe we just want to override the default.

```clojure
(defentity cats
  (entity-fields :name :owner :breed :color :gender)
  (pk :id)
  (database db))

  (create cats {:name "Crookshanks", :owner "Granger" :breed "Tabby", :color "Orange", :gender "M"})
  (binding [*string-key* :owner] (read cats "Granger"))
```



## License

- Copyright © 2013 Stephen Sloan
- Sponsored by Rafter, Inc.
- MIT Licensed
