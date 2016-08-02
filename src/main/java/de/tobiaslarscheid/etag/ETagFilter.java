package de.tobiaslarscheid.etag;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tobiaslarscheid.etag.exceptions.EntityIsNullException;
import de.tobiaslarscheid.etag.exceptions.MissingETaggedInterfaceException;
import de.tobiaslarscheid.etag.interfaces.ETag;
import de.tobiaslarscheid.etag.interfaces.ETagged;

@Provider
@ETag
public class ETagFilter implements ContainerResponseFilter {

	private static final Logger logger = LoggerFactory.getLogger(ETagFilter.class);

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		if (responseContext.getStatus() != Response.Status.OK.getStatusCode()) {
			logger.debug("Response status code is not 200, ignoring.");
			return;
		}
		String clientProvidedETag = requestContext.getHeaderString(HttpHeaders.IF_NONE_MATCH);
		String entityEtag;
		try {
			entityEtag = getEtagFromResponseEntity(responseContext);
		} catch (EntityIsNullException e) {
			logger.debug("Entity is null, ignoring.");
			return;
		}
		logger.trace("Entities ETag is " + entityEtag);
		logger.trace("ETag from if-none-match-header is " + clientProvidedETag);
		if (entityEtag.equals(clientProvidedETag)) {
			logger.trace("EntityEtag equals if-none-match-header, returning 304");
			responseContext.setEntity(null);
			responseContext.setStatusInfo(Response.Status.NOT_MODIFIED);
		}
		responseContext.getHeaders().add("ETag", entityEtag);
	}

	private String getEtagFromResponseEntity(ContainerResponseContext responseContext) throws EntityIsNullException {
		if (responseContext.getEntity() == null) {
			throw new EntityIsNullException();
		}
		if (!(responseContext.getEntity() instanceof ETagged)) {
			throw new MissingETaggedInterfaceException("Entity " + responseContext.getEntity() + " can not be cast to "
					+ ETagged.class + ". Make sure all returned Entities implement ETagged!");
		}
		return ((ETagged) responseContext.getEntity()).getETag();
	}

}
