package org.qfox.jestful.commons;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.qfox.jestful.commons.collection.CaseInsensitiveMap;

/**
 * <p>
 * Description: Content-Type wrapper
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月9日 上午11:12:52
 *
 * @since 1.0.0
 */
public class MediaType implements Comparable<MediaType> {
	private final String name;
	private final String type;
	private final String subtype;
	private final Map<String, String> parameters;
	private final String charset;
	private final float weight;

	private MediaType(String name, Map<String, String> parameters) {
		super();
		this.name = String.valueOf(name);
		this.type = name.split("/")[0];
		this.subtype = name.split("/")[1];
		this.parameters = Collections.unmodifiableMap(parameters);
		this.charset = parameters.containsKey("charset") ? parameters.get("charset") : Charset.defaultCharset().name();
		this.weight = parameters.containsKey("q") ? Float.valueOf(parameters.get("q")) : 1.0f;
	}

	/**
	 * judge this media type is matches the specified content type, such as
	 * {@link MediaType} {@code application/json} matches content type
	 * {@link application/json} {@link application/*} matches
	 * {@code application/json}
	 * 
	 * @param contentType
	 *            the content type
	 * @return
	 */
	public boolean matches(String contentType) {
		return matches(MediaType.valueOf(contentType));
	}

	public boolean matches(MediaType mediaType) {
		if (this.equals(mediaType)) {
			return true;
		}
		if (this.type.equalsIgnoreCase(mediaType.type) && this.isWildcardSubtype()) {
			return true;
		}
		if (this.subtype.equalsIgnoreCase(mediaType.subtype) && this.isWildcardType()) {
			return true;
		}
		if (this.isWildcardType() && this.isWildcardSubtype()) {
			return true;
		}
		return false;
	}

	/**
	 * convert Content-Type from {@link String} to {@link MediaType}
	 * 
	 * @param mediaType
	 *            http Content-Type such as application/json;charset=UTF-8;q=0.9
	 * @return
	 * @throws MediaTypeFormatException
	 *             wrong format of Content-Type
	 */
	public static MediaType valueOf(String mediaType) {
		if (mediaType == null) {
			throw new NullPointerException();
		}
		mediaType = mediaType.replace(" ", "");
		if (mediaType.matches("[^;/]+/[^;/]+(;[^;=]+=[^;=]+)*") == false) {
			throw new IllegalArgumentException(mediaType);
		}
		String name = mediaType.split(";")[0];
		Map<String, String> parameters = new CaseInsensitiveMap<String, String>();
		Pattern pattern = Pattern.compile(";([^;=]+)=([^;=]+)");
		Matcher matcher = pattern.matcher(mediaType);
		while (matcher.find()) {
			String key = matcher.group(1);
			String value = matcher.group(2);
			parameters.put(key, value);
		}
		return new MediaType(name, parameters);
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getSubtype() {
		return subtype;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public String getCharset() {
		return charset;
	}

	public float getWeight() {
		return weight;
	}

	public boolean isWildcardType() {
		return "*".equals(type);
	}

	public boolean isWildcardSubtype() {
		return "*".equals(subtype);
	}

	public int compareTo(MediaType o) {
		return weight > o.weight ? -1 : weight < o.weight ? 1 : name.compareTo(o.name);
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
		MediaType other = (MediaType) obj;
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
