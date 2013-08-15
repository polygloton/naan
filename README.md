# naan

Naan provides CRUD functions for working with Korma entities.

What is naan?  It isn't korma.  It doesn't replace korma.  It doesn't wrap around korma.  It's just bread... er... CRUD for Korma.  It is a nice-to-have that goes well with korma.  Korma is excellent and does many things very well.  However, I do find that for create, read, update, and destroy operations, I really want to DRY-up my code.  Nann provides these four functions, and a few more helpers.  Use naan with korma, its tasty!

## Usage

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

```cljure
(first
  (select users
    (where {:id [= 1]})
    (limit 1)))
```

And using naan read.

```clojure
(read users {:id 1})
```

That's a very straightforward helper function.  Let's see updating with korma update.


```clojure

```



## License

- Copyright © 2013 Stephen Sloan
- Sponsored by Rafter, Inc.
- MIT Licensed
