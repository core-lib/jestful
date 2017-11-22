package org.qfox.jestful.core;

import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>
 * Description: 多部分内容头
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月9日 下午7:02:24
 * @since 1.0.0
 */
public class Multihead implements Cloneable {
	private final Disposition disposition;
	private final MediaType type;
	private final Map<String, String> header;

	public Multihead(Disposition disposition, MediaType type) {
		this.disposition = disposition;
		this.type = type;
		this.header = new CaseInsensitiveMap<String, String>();
		if (disposition != null) {
			this.header.put("Content-Disposition", disposition.toString());
		}
		if (type != null) {
			this.header.put("Content-Type", type.toString());
		}
	}

	public Multihead(Map<String, String> header) {
		super();
		this.header = new CaseInsensitiveMap<String, String>(header);
		this.disposition = this.header.containsKey("Content-Disposition") ? Disposition.valueOf(this.header.get("Content-Disposition")) : null;
		this.type = this.header.containsKey("Content-Type") ? MediaType.valueOf(this.header.get("Content-Type")) : null;
	}

	public Multihead(InputStream inputStream) throws IOException {
		this.header = new CaseInsensitiveMap<String, String>();
		String line;
		while ((line = IOKit.readln(inputStream)) != null && line.length() == 0 == false) {
			int index = line.indexOf(':');
			if (index < 0) {
				throw new IllegalArgumentException(line);
			}
			String key = line.substring(0, index);
			String value = line.substring(index + 1);
			this.header.put(key.trim(), value.trim());
		}
		this.disposition = this.header.containsKey("Content-Disposition") ? Disposition.valueOf(this.header.get("Content-Disposition")) : null;
		this.type = this.header.containsKey("Content-Type") ? MediaType.valueOf(this.header.get("Content-Type")) : null;
	}

	public void writeTo(Writer writer) throws IOException {
		writer.write(toString());
		writer.write(new char[]{'\r', '\n'});
	}

	public Disposition getDisposition() {
		return disposition;
	}

	public MediaType getType() {
		return type;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	@Override
	public Multihead clone() {
		return new Multihead(header);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((header == null) ? 0 : header.hashCode());
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
		Multihead other = (Multihead) obj;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = header.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			builder.append(entry.getKey()).append(": ").append(entry.getValue()).append(iterator.hasNext() ? "\r\n" : "");
		}
		return builder.toString();
	}

}
