package org.qfox.jestful.commons;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

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
 * @date 2016年4月9日 下午7:02:24
 *
 * @since 1.0.0
 */
public class Multipart {
	private final Disposition disposition;
	private final MediaType contentType;
	private final Map<String, String> header;

	public Multipart(Map<String, String> header) {
		try {
			String charset = Charset.defaultCharset().name();
			String _disposition = header.get("Content-Disposition");
			this.disposition = _disposition != null ? Disposition.valueOf(_disposition) : null;
			String _contentType = header.get("Content-Type");
			this.contentType = _contentType != null ? MediaType.valueOf(_contentType) : null;
			Map<String, String> map = new CaseInsensitiveMap<String, String>();
			for (Entry<String, String> entry : header.entrySet()) {
				map.put(URLDecoder.decode(entry.getKey(), charset), URLDecoder.decode(entry.getValue(), charset));
			}
			this.header = Collections.unmodifiableMap(map);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public Multipart(Map<String, String> header, String charset) throws UnsupportedEncodingException {
		super();
		String _disposition = header.get("Content-Disposition");
		this.disposition = _disposition != null ? Disposition.valueOf(_disposition) : null;
		String _contentType = header.get("Content-Type");
		this.contentType = _contentType != null ? MediaType.valueOf(_contentType) : null;
		this.header = new CaseInsensitiveMap<String, String>();
		for (Entry<String, String> entry : header.entrySet()) {
			this.header.put(URLDecoder.decode(entry.getKey(), charset), URLDecoder.decode(entry.getValue(), charset));
		}
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

}
