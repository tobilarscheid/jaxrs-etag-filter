# JAX-RS ETag Filter

JAX-RS cache control finally became easy! Add ``ETags`` to all your responses and send ``304 Not Modified`` whenever a client sends matching ``ETags`` in ``If-None-Modified`` headers - with just one annotation!

To get started, add the following dependency from jitpack:

https://jitpack.io/#tobilarscheid/jaxrs-etag-filter/v-1.0.0

Make sure the entity you are planning to return from your service implements `ETagged`:

```java
public class Entity implements ETagged {
    public String getETag(){
        /**
        * if you you are reading this you probably know
        * what to consider when generating ETags
        */
    }
}
```

Then, simply annotate your service Method with `@ETag`.

```java
@ETag
public Response getMyEntity(){
    Entity e = //do something to get your entity here
    return Response.ok(e).build();
}
```

The `ETag` header is automatically added to your Response. If the client sends a request with a `If-None-Match` header matching your entities ETag, Status Code `304` and no body is returned.

## Hints
  - All the functionality is only applied if your Response´s status code is `200`
  - If you don't use class path scanning (hint: [you shouldn't]), you need to list `de.tobiaslarscheid.etag.ETagFilter.class` in your RestAppConfig. (Or whereever else you register your resource classes)

License
----

MIT

   [you shouldn't]: <https://blogs.oracle.com/japod/entry/when_to_use_jax_rs>
