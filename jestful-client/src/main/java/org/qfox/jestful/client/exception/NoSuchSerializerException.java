package org.qfox.jestful.client.exception;

import java.util.Collection;

import org.qfox.jestful.core.Accepts;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.RequestSerializer;
import org.qfox.jestful.core.exception.JestfulException;

public class NoSuchSerializerException extends JestfulException {
	private static final long serialVersionUID = 9075042655898294114L;

	private final Parameter parameter;
	private final Accepts accepts;
	private final Collection<RequestSerializer> serializers;

	public NoSuchSerializerException(Parameter parameter, Accepts accepts, Collection<RequestSerializer> serializers) {
		super();
		this.parameter = parameter;
		this.accepts = accepts;
		this.serializers = serializers;
	}

	public Parameter getParameter() {
		return parameter;
	}

	public Accepts getAccepts() {
		return accepts;
	}

	public Collection<RequestSerializer> getSerializers() {
		return serializers;
	}

}
