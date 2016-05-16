package org.qfox.jestful.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.qfox.jestful.server.formatting.Multipart;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

public class JestfulServletRequestWrapper extends JestfulServletRequest {
	private final JestfulServletRequest request;

	public JestfulServletRequestWrapper(JestfulServletRequest request) {
		super(request);
		this.request = request;
	}

	@Override
	public String[] getHeaderKeys() {
		return request.getHeaderKeys();
	}

	@Override
	public String getRequestHeader(String name) {
		return request.getRequestHeader(name);
	}

	@Override
	public void setRequestHeader(String name, String value) {
		request.setRequestHeader(name, value);
	}

	@Override
	public String[] getRequestHeaders(String name) {
		return request.getRequestHeaders(name);
	}

	@Override
	public void setRequestHeaders(String name, String[] values) {
		request.setRequestHeaders(name, values);
	}

	@Override
	public InputStream getRequestInputStream() throws IOException {
		return request.getRequestInputStream();
	}

	@Override
	public OutputStream getRequestOutputStream() throws IOException {
		return request.getRequestOutputStream();
	}

	@Override
	public Iterator<String> getFileNames() {
		return request.getFileNames();
	}

	@Override
	public MultipartFile getFile(String name) {
		return request.getFile(name);
	}

	@Override
	public List<MultipartFile> getFiles(String name) {
		return request.getFiles(name);
	}

	@Override
	public Map<String, MultipartFile> getFileMap() {
		return request.getFileMap();
	}

	@Override
	public MultiValueMap<String, MultipartFile> getMultiFileMap() {
		return request.getMultiFileMap();
	}

	@Override
	public String getMultipartContentType(String paramOrFileName) {
		return request.getMultipartContentType(paramOrFileName);
	}

	@Override
	public HttpMethod getRequestMethod() {
		return request.getRequestMethod();
	}

	@Override
	public HttpHeaders getRequestHeaders() {
		return request.getRequestHeaders();
	}

	@Override
	public HttpHeaders getMultipartHeaders(String name) {
		return request.getMultipartHeaders(name);
	}

	@Override
	public List<Multipart> getMultiparts() {
		return request.getMultiparts();
	}

	@Override
	public void setMultiparts(List<Multipart> multiparts) {
		request.setMultiparts(multiparts);
	}

}
