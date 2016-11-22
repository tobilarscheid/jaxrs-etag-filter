package de.tobiaslarscheid.etag.interfaces;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ETagged {
	@XmlTransient
	@JsonIgnore
	public String getETag();
}
