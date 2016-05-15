package org.qfox.jestful.core;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.qfox.jestful.commons.Weighted;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;

/**
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年5月15日 上午11:16:47
 *
 * @since 1.0.0
 */
public class Charset implements Weighted<Charset> {
	private final String name;
	private final Map<String, String> parameters;
	private final float weight;

	public Charset(String name, Map<String, String> parameters) {
		super();
		this.name = name;
		this.parameters = Collections.unmodifiableMap(parameters);
		this.weight = parameters.containsKey("q") ? Float.valueOf(parameters.get("q")) : 1.0f;
	}

	public static Charset valueOf(String charset) {
		if (charset == null) {
			throw new NullPointerException();
		}
		charset = charset.replace(" ", "");
		if (charset.matches("[^;]+(;[^;=]+=[^;=]+)*") == false) {
			throw new IllegalArgumentException(charset);
		}
		String name = charset.split(";")[0];
		Map<String, String> parameters = new CaseInsensitiveMap<String, String>();
		Pattern pattern = Pattern.compile(";([^;=]+)=([^;=]+)");
		Matcher matcher = pattern.matcher(charset);
		while (matcher.find()) {
			String k = matcher.group(1);
			String v = matcher.group(2);
			parameters.put(k, v);
		}
		return new Charset(name, parameters);
	}

	public int compareTo(Charset o) {
		return weight > o.weight ? -1 : weight < o.weight ? 1 : name.compareTo(o.name);
	}

	public String getName() {
		return name;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public float getWeight() {
		return weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.toLowerCase().hashCode());
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
		Charset other = (Charset) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.toLowerCase().equals(other.name.toLowerCase()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(name);
		for (Entry<String, String> entry : parameters.entrySet()) {
			builder.append(";").append(entry.getKey()).append("=").append(entry.getValue());
		}
		return builder.toString();
	}

}
