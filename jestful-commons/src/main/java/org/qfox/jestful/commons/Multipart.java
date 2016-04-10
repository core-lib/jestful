package org.qfox.jestful.commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.commons.io.IOUtils;

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
 * @date 2016年4月9日 下午7:02:24
 *
 * @since 1.0.0
 */
public class Multipart {
	private final Disposition disposition;
	private final MediaType contentType;
	private final Map<String, String> header;

	public Multipart(InputStream inputStream) throws IOException {
		this.header = new CaseInsensitiveMap<String, String>();
		String line = null;
		while ((line = IOUtils.readLine(inputStream)) != null && line.isEmpty() == false) {
			int index = line.indexOf(':');
			if (index < 0) {
				throw new IllegalArgumentException(line);
			}
			String key = line.substring(0, index);
			String value = line.substring(index + 1);
			this.header.put(key, value);
		}
		this.disposition = this.header.containsKey("Content-Disposition") ? Disposition.valueOf(this.header.get("Content-Disposition")) : null;
		this.contentType = this.header.containsKey("Content-Type") ? MediaType.valueOf(this.header.get("Content-Type")) : null;
	}

	public Disposition getDisposition() {
		return disposition;
	}

	public MediaType getContentType() {
		return contentType;
	}

	public Map<String, String> getHeader() {
		return header;
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
		Multipart other = (Multipart) obj;
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
