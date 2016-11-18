[![Build Status](https://travis-ci.org/tobilarscheid/jaxrs-etag-filter.svg?branch=master)](https://travis-ci.org/tobilarscheid/jaxrs-etag-filter)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bb6e22e1a20f409d84c5ea8cf3d8e2e1)](https://www.codacy.com/app/tobilarscheid/jaxrs-etag-filter?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=tobilarscheid/jaxrs-etag-filter&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/bb6e22e1a20f409d84c5ea8cf3d8e2e1)](https://www.codacy.com/app/tobilarscheid/jaxrs-etag-filter?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=tobilarscheid/jaxrs-etag-filter&amp;utm_campaign=Badge_Coverage)

# JAX-RS ETag Filter

JAX-RS cache control finally became easy! Add ``ETags`` to all your responses and send ``304 Not Modified`` whenever a client sends matching ``ETags`` in ``If-None-Modified`` headers - with just one annotation!

To get started, add the following dependency from jitpack:

https://jitpack.io/#tobilarscheid/jaxrs-etag-filter/v-1.0.2

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
  - If you want to use `Cache-Control`or `Expires` headers to complete your caching, you should have a look at [JAX-RS Cache-Control Filter](https://github.com/tobilarscheid/jaxrs-cache-control-filter)

License
----

MIT

   [you shouldn't]: <https://blogs.oracle.com/japod/entry/when_to_use_jax_rs>
