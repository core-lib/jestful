package org.qfox.jestful.server.exception;

import java.lang.reflect.Type;
import java.util.Map;

import org.qfox.jestful.core.exception.JestfulRuntimeException;
import org.qfox.jestful.server.converter.ConversionProvider;

public class UnconvertableParameterException extends JestfulRuntimeException {
	private static final long serialVersionUID = 5467589843078783058L;

	private final String name;
	private final Type type;
	private final Map<String, String[]> map;
	private final ConversionProvider provider;

	public UnconvertableParameterException(String name, Type type, Map<String, String[]> map, ConversionProvider provider) {
		this(null, null, name, type, map, provider);
	}

	public UnconvertableParameterException(String message, String name, Type type, Map<String, String[]> map, ConversionProvider provider) {
		this(message, null, name, type, map, provider);
	}

	public UnconvertableParameterException(Throwable cause, String name, Type type, Map<String, String[]> map, ConversionProvider provider) {
		this(null, cause, name, type, map, provider);
	}

	public UnconvertableParameterException(String message, Throwable cause, String name, Type type, Map<String, String[]> map, ConversionProvider provider) {
		super(message, cause);
		this.name = name;
		this.type = type;
		this.map = map;
		this.provider = provider;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public Map<String, String[]> getMap() {
		return map;
	}

	public ConversionProvider getProvider() {
		return provider;
	}

}
