package org.qfox.jestful.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.Multipart;
import org.qfox.jestful.commons.io.IOUtils;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.RequestDeserializer;

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
 * @date 2016年4月19日 下午8:53:51
 *
 * @since 1.0.0
 */
public class FileRequestDeserializer implements RequestDeserializer {
	private int bufferSize = 4096;
	private String directory = System.getProperty("java.io.tmpdir");

	public String getContentType() {
		return "application/octet-stream";
	}

	public void deserialize(Action action, MediaType mediaType, InputStream in) throws IOException {
		Parameter[] parameters = action.getParameters();
		for (Parameter parameter : parameters) {
			if (parameter.getPosition() != Position.BODY) {
				continue;
			}
			Request request = action.getRequest();
			String filename = request.getRequestHeader("File-Name");
			File file = new File(directory, filename);
			File parent = file.getParentFile();
			if (parent.exists() == false) {
				synchronized (parent.getAbsolutePath().intern()) {
					parent.mkdirs();
				}
			}
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				byte[] buffer = new byte[bufferSize];
				int length = 0;
				while ((length = in.read(buffer)) != -1) {
					fos.write(buffer, 0, length);
				}
			} finally {
				IOUtils.close(fos);
			}
		}
	}

	public void deserialize(Action action, Parameter parameter, Multipart multipart, InputStream in) throws IOException {

	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

}
