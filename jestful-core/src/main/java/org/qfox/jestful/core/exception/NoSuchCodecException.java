package org.qfox.jestful.core.exception;

import org.qfox.jestful.core.Encodings;

public class NoSuchCodecException extends JestfulException {
	private static final long serialVersionUID = -4320358107952280537L;

	private final Encodings expects;
	private final Encodings actuals;

	public NoSuchCodecException(Encodings expects, Encodings actuals) {
		super();
		this.expects = expects;
		this.actuals = actuals;
	}

	public Encodings getExpects() {
		return expects;
	}

	public Encodings getActuals() {
		return actuals;
	}

}
