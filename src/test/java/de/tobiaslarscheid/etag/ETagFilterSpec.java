package de.tobiaslarscheid.etag;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.never;

import java.io.IOException;
import java.util.Arrays;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.tobiaslarscheid.etag.exceptions.MissingETaggedInterfaceException;
import de.tobiaslarscheid.etag.interfaces.ETagged;

@RunWith(MockitoJUnitRunner.class)
public class ETagFilterSpec {
	@Mock
	private ContainerRequestContext requestCtx;
	@Mock
	private ContainerResponseContext responseCtx;
	@Mock
	private ETagged entity;
	private ETagFilter eTagFilter = new ETagFilter();;

	public void prepareMocks() {
		Mockito.when(entity.getETag()).thenReturn("42");
		Mockito.when(requestCtx.getHeaderString("If-None-Match")).thenReturn("42");
		Mockito.when(responseCtx.getEntity()).thenReturn(entity);
		Mockito.when(responseCtx.getStatus()).thenReturn(200);
	}

	@Test
	public void shouldReturn304IfNotModified() throws IOException {
		prepareMocks();
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();

		Mockito.when(responseCtx.getHeaders()).thenReturn(headers);

		eTagFilter.filter(requestCtx, responseCtx);

		Mockito.verify(responseCtx).setEntity(null);
		Mockito.verify(responseCtx).setStatusInfo(Response.Status.NOT_MODIFIED);
		assertThat(headers.containsKey("ETag"), is(true));
		assertThat(headers.get("ETag"), is(Arrays.asList("42")));
	}

	@Test()
	public void shouldAppendETagIfModified() throws IOException {
		prepareMocks();
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();

		Mockito.when(requestCtx.getHeaderString("If-None-Match")).thenReturn("0");
		Mockito.when(responseCtx.getHeaders()).thenReturn(headers);

		eTagFilter.filter(requestCtx, responseCtx);

		Mockito.verify(responseCtx, never()).setEntity(null);
		Mockito.verify(responseCtx, never()).setStatusInfo(Response.Status.NOT_MODIFIED);
		assertThat(headers.containsKey("ETag"), is(true));
		assertThat(headers.get("ETag"), is(Arrays.asList("42")));
	}

	@Test(expected = MissingETaggedInterfaceException.class)
	public void shouldThrowErrorIfEntityDoesNotImplementEtagged() throws IOException {
		prepareMocks();

		Mockito.when(responseCtx.getEntity()).thenReturn(new Object());

		eTagFilter.filter(requestCtx, responseCtx);
	}

	@Test
	public void shouldHandleMissingIfNoneMatch() throws IOException {
		prepareMocks();
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();

		Mockito.when(requestCtx.getHeaderString("If-None-Match")).thenReturn(null);
		Mockito.when(responseCtx.getHeaders()).thenReturn(headers);

		eTagFilter.filter(requestCtx, responseCtx);

		Mockito.verify(responseCtx, never()).setEntity(null);
		Mockito.verify(responseCtx, never()).setStatusInfo(Response.Status.NOT_MODIFIED);
		assertThat(headers.containsKey("ETag"), is(true));
		assertThat(headers.get("ETag"), is(Arrays.asList("42")));
	}

	@Test
	public void shouldDoNothingForNullEntity() throws IOException {
		prepareMocks();
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
		Mockito.when(responseCtx.getHeaders()).thenReturn(headers);

		Mockito.when(responseCtx.getEntity()).thenReturn(null);

		eTagFilter.filter(requestCtx, responseCtx);

		Mockito.verify(responseCtx, never()).setEntity(null);
		Mockito.verify(responseCtx, never()).setStatusInfo(Response.Status.NOT_MODIFIED);
		assertThat(headers.containsKey("ETag"), is(false));
	}

	@Test
	public void shouldDoNothingForNon200ResponseCode() throws IOException {
		prepareMocks();
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
		Mockito.when(responseCtx.getHeaders()).thenReturn(headers);

		Mockito.when(responseCtx.getStatus()).thenReturn(400);

		eTagFilter.filter(requestCtx, responseCtx);

		Mockito.verify(responseCtx, never()).setEntity(null);
		Mockito.verify(responseCtx, never()).setStatusInfo(Response.Status.NOT_MODIFIED);
		assertThat(headers.containsKey("ETag"), is(false));
	}
}
