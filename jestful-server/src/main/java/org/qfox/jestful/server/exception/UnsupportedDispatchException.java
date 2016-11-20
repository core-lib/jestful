package org.qfox.jestful.server.exception;

import javax.servlet.DispatcherType;

import org.qfox.jestful.core.exception.JestfulException;

/**
 * Created by yangchangpei on 16/10/8.
 */
public class UnsupportedDispatchException extends JestfulException {
	private static final long serialVersionUID = -1318182255899791582L;
	private final DispatcherType dispatcherType;

	public UnsupportedDispatchException(DispatcherType dispatcherType) {
		this.dispatcherType = dispatcherType;
	}

	public DispatcherType getDispatcherType() {
		return dispatcherType;
	}
}
