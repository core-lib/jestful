package org.qfox.jestful.server.tree;

public class PathExpression implements Expression<PathExpression> {
	private final String pattern;
	private final String command;

	public PathExpression() {
		this(null, null);
	}

	public PathExpression(String pattern) {
		this(pattern, null);
	}

	public PathExpression(String pattern, String command) {
		super();
		this.pattern = pattern != null ? pattern : "";
		this.command = command != null ? command : "";
	}

	public boolean match(String value) {
		return value.equals(pattern) || value.matches(pattern);
	}

	public int compareTo(PathExpression o) {
		return pattern.compareTo(o.pattern) != 0 ? pattern.compareTo(o.pattern) : command.compareTo(o.command);
	}

	public String pattern() {
		return pattern;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PathExpression other = (PathExpression) obj;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return pattern;
	}

}