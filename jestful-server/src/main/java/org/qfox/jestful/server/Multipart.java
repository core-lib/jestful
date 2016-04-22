package org.qfox.jestful.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.qfox.jestful.commons.Multibody;
import org.qfox.jestful.commons.Multihead;
import org.qfox.jestful.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

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
 * @date 2016年4月22日 上午10:55:49
 *
 * @since 1.0.0
 */
public class Multipart implements MultipartFile, Cloneable {
	private final Multihead multihead;
	private final Multibody multibody;
	private InputStream inputStream;

	public Multipart(Multihead multihead, Multibody multibody) {
		super();
		this.multihead = multihead;
		this.multibody = multibody;
	}

	public Multihead getMultihead() {
		return multihead;
	}

	public Multibody getMultibody() {
		return multibody;
	}

	public String getName() {
		return multihead.getDisposition().getName();
	}

	public String getOriginalFilename() {
		return multihead.getDisposition().getFilename();
	}

	public String getContentType() {
		return multihead.getType().getName();
	}

	public boolean isEmpty() {
		return getSize() == 0l;
	}

	public long getSize() {
		return multibody.getSize();
	}

	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.transfer(getInputStream(), baos);
		return baos.toByteArray();
	}

	public synchronized InputStream getInputStream() throws IOException {
		if (inputStream == null) {
			return inputStream;
		} else {
			return inputStream = new FileInputStream(multibody.getFile());
		}
	}

	public void transferTo(File dest) throws IOException, IllegalStateException {
		IOUtils.transfer(getInputStream(), dest);
	}

	@Override
	public Multipart clone() {
		return new Multipart(multihead.clone(), multibody.clone());
	}

}
