package org.qfox.jestful.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.Multipart;
import org.qfox.jestful.commons.io.IOUtils;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.RequestSerializer;

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
 * @date 2016年4月19日 下午8:42:39
 *
 * @since 1.0.0
 */
public class FileRequestSerializer implements RequestSerializer {
	private int bufferSize = 4096;

	public String getContentType() {
		return "application/octet-stream";
	}

	public boolean supports(Parameter parameter) {
		return parameter.getValue() instanceof File;
	}

	public void serialize(Action action, MediaType mediaType, OutputStream out) throws IOException {
		Request request = action.getRequest();
		Parameter[] parameters = action.getParameters();
		for (Parameter parameter : parameters) {
			if (parameter.getPosition() != Position.BODY) {
				continue;
			}
			FileInputStream fis = null;
			try {
				File file = (File) parameter.getValue();
				request.setRequestHeader("File-Name", file.getName());
				fis = new FileInputStream(file);
				byte[] buffer = new byte[bufferSize];
				int length = 0;
				while ((length = fis.read(buffer)) != -1) {
					out.write(buffer, 0, length);
				}
				break;
			} finally {
				IOUtils.close(fis);
			}
		}
	}

	public void serialize(Action action, Parameter parameter, Multipart multipart, OutputStream out) throws IOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream((File) parameter.getValue());
			byte[] buffer = new byte[bufferSize];
			int length = 0;
			while ((length = fis.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
		} finally {
			IOUtils.close(fis);
		}
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

}
