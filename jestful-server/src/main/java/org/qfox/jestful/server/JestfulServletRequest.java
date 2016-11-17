package org.qfox.jestful.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;

import org.qfox.jestful.core.Request;
import org.qfox.jestful.server.formatting.Multipart;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
 * @date 2016年4月8日 下午4:12:32
 *
 * @since 1.0.0
 */
public class JestfulServletRequest extends HttpServletRequestWrapper implements Request, MultipartHttpServletRequest {
	private final HttpServletRequest request;
	private List<Multipart> multiparts = new ArrayList<Multipart>();
	private List<Part> parts = new ArrayList<Part>();

	public JestfulServletRequest(HttpServletRequest request) {
		super(request);
		this.request = request;
	}

	public String[] getHeaderKeys() {
		Enumeration<String> enumeration = request.getHeaderNames();
		if (enumeration == null) {
			return null;
		}
		List<String> keys = new ArrayList<String>();
		while (enumeration.hasMoreElements()) {
			keys.add(enumeration.nextElement());
		}
		return keys.toArray(new String[keys.size()]);
	}

	public String getRequestHeader(String name) {
		return request.getHeader(name);
	}

	public void setRequestHeader(String name, String value) {
		throw new UnsupportedOperationException();
	}

	public String[] getRequestHeaders(String name) {
		Enumeration<String> enumeration = request.getHeaders(name);
		if (enumeration == null) {
			return null;
		}
		List<String> values = new ArrayList<String>();
		while (enumeration.hasMoreElements()) {
			values.add(enumeration.nextElement());
		}
		return values.toArray(new String[values.size()]);
	}

	public void setRequestHeaders(String name, String[] values) {
		throw new UnsupportedOperationException();
	}

	public int getConnTimeout() {
		return 0;
	}

	public void setConnTimeout(int timeout) {
		throw new UnsupportedOperationException();
	}

	public int getReadTimeout() {
		throw new UnsupportedOperationException();
	}

	public void setReadTimeout(int timeout) {
		throw new UnsupportedOperationException();
	}

	public String getContentType() {
		return super.getContentType();
	}

	public void setContentType(String type) {

	}

	public void connect() throws IOException {
		throw new UnsupportedOperationException();
	}

	public void close() throws IOException {

	}

	public InputStream getRequestInputStream() throws IOException {
		return request.getInputStream();
	}

	public OutputStream getRequestOutputStream() throws IOException {
		throw new UnsupportedOperationException();
	}

	public void connect(int timeout) throws IOException {
		throw new UnsupportedOperationException();
	}

	public Iterator<String> getFileNames() {
		Set<String> names = new LinkedHashSet<String>();
		for (Multipart multipart : multiparts) {
			names.add(multipart.getName());
		}
		return names.iterator();
	}

	public MultipartFile getFile(String name) {
		List<MultipartFile> files = getFiles(name);
		return files.isEmpty() ? null : files.get(0);
	}

	public List<MultipartFile> getFiles(String name) {
		List<MultipartFile> files = new ArrayList<MultipartFile>();
		for (Multipart multipart : multiparts) {
			if (multipart.getName().equals(name)) {
				files.add(multipart);
			}
		}
		return files;
	}

	public Map<String, MultipartFile> getFileMap() {
		Map<String, MultipartFile> map = new LinkedHashMap<String, MultipartFile>();
		Iterator<String> iterator = getFileNames();
		while (iterator.hasNext()) {
			String name = iterator.next();
			MultipartFile value = getFile(name);
			map.put(name, value);
		}
		return map;
	}

	public MultiValueMap<String, MultipartFile> getMultiFileMap() {
		MultiValueMap<String, MultipartFile> map = new LinkedMultiValueMap<String, MultipartFile>();
		Iterator<String> iterator = getFileNames();
		while (iterator.hasNext()) {
			String name = iterator.next();
			List<MultipartFile> value = getFiles(name);
			map.put(name, value);
		}
		return map;
	}

	public String getMultipartContentType(String paramOrFileName) {

		return null;
	}

	public HttpMethod getRequestMethod() {
		String method = getMethod().toUpperCase();
		return HttpMethod.valueOf(method);
	}

	public HttpHeaders getRequestHeaders() {
		HttpHeaders headers = new HttpHeaders();
		String[] keys = getHeaderKeys();
		for (String key : keys) {
			String[] values = getRequestHeaders(key);
			headers.put(key, Arrays.asList(values));
		}
		return headers;
	}

	public HttpHeaders getMultipartHeaders(String name) {
		Multipart multipart = (Multipart) getFile(name);
		if (multipart == null) {
			return null;
		}
		HttpHeaders headers = new HttpHeaders();
		Map<String, String> header = multipart.getMultihead().getHeader();
		for (Entry<String, String> entry : header.entrySet()) {
			headers.add(entry.getKey(), entry.getValue());
		}
		return headers;
	}

	public List<Multipart> getMultiparts() {
		return multiparts;
	}

	public void setMultiparts(List<Multipart> multiparts) {
		this.multiparts.addAll(multiparts);
		this.parts.addAll(multiparts);
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		return parts;
	}

	@Override
	public Part getPart(String name) throws IOException, ServletException {
		for (Part part : parts) {
			if (name == null ? part.getName() == null : name.equals(part.getName())) {
				return part;
			}
		}
		return null;
	}

}
