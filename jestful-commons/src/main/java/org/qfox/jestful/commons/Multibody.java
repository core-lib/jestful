package org.qfox.jestful.commons;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import org.qfox.jestful.commons.io.IOUtils;

import eu.medsea.mimeutil.MimeUtil;

/**
 * <p>
 * Description: 多部分的内容体
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月22日 上午9:59:46
 *
 * @since 1.0.0
 */
public class Multibody {
	private final File file;
	private final String contentType;
	private final long size;

	public Multibody(File file) throws IOException {
		super();
		this.file = file;
		Collection<?> types = MimeUtil.getMimeTypes(file);
		this.contentType = types == null || types.isEmpty() ? "application/octet-stream" : types.toArray()[0].toString();
		this.size = file.length();
	}

	public Multibody(InputStream inputStream) throws IOException {
		super();
		this.file = File.createTempFile("jestful", ".tmp");
		IOUtils.transfer(inputStream, file);
		Collection<?> types = MimeUtil.getMimeTypes(file);
		this.contentType = types == null || types.isEmpty() ? "application/octet-stream" : types.toArray()[0].toString();
		this.size = file.length();
	}

	public void writeTo(OutputStream out) throws IOException {
		IOUtils.transfer(file, out);
	}

	public File getFile() {
		return file;
	}

	public String getContentType() {
		return contentType;
	}

	public long getSize() {
		return size;
	}

}
