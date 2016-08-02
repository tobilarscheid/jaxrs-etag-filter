package de.tobiaslarscheid.etag.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

import de.tobiaslarscheid.etag.ETagFilterSpec;

/**
 * Use on JAX-RS Services to add ETags or send 304. See also:
 * {@link ETagFilterSpec}
 *
 */
@NameBinding
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ETag {

}
