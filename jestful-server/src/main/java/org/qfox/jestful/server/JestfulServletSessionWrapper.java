package org.qfox.jestful.server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import java.util.Enumeration;

/**
 * Created by yangchangpei on 16/11/20.
 */
@SuppressWarnings("deprecation")
public class JestfulServletSessionWrapper implements HttpSession {
	private final HttpSession session;

	public JestfulServletSessionWrapper(HttpSession session) {
		this.session = session;
	}

	public long getCreationTime() {
		return session.getCreationTime();
	}

	public String getId() {
		return session.getId();
	}

	public long getLastAccessedTime() {
		return session.getLastAccessedTime();
	}

	public ServletContext getServletContext() {
		return session.getServletContext();
	}

	public void setMaxInactiveInterval(int interval) {
		session.setMaxInactiveInterval(interval);
	}

	public int getMaxInactiveInterval() {
		return session.getMaxInactiveInterval();
	}

	public HttpSessionContext getSessionContext() {
		return session.getSessionContext();
	}

	public Object getAttribute(String name) {
		return session.getAttribute(name);
	}

	public Object getValue(String name) {
		return session.getValue(name);
	}

	public Enumeration<String> getAttributeNames() {
		return session.getAttributeNames();
	}

	public String[] getValueNames() {
		return session.getValueNames();
	}

	public void setAttribute(String name, Object value) {
		session.setAttribute(name, value);
	}

	public void putValue(String name, Object value) {
		session.putValue(name, value);
	}

	public void removeAttribute(String name) {
		session.removeAttribute(name);
	}

	public void removeValue(String name) {
		session.removeValue(name);
	}

	public void invalidate() {
		session.invalidate();
	}

	public boolean isNew() {
		return session.isNew();
	}
}
