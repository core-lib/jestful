package org.qfox.jestful.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.io.IOUtils;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.ResponseSerializer;
import org.qfox.jestful.core.Result;

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
 * @date 2016年4月20日 上午10:52:55
 *
 * @since 1.0.0
 */
public class FileResponseSerializer implements ResponseSerializer {
	private int bufferSize = 4096;

	public String getContentType() {
		return "application/octet-stream";
	}

	public void serialize(Action action, MediaType mediaType, OutputStream out) throws IOException {
		Request request = action.getRequest();
		Result result = action.getResult();
		File file = (File) result.getValue();
		if (file == null) {
			return;
		}
		request.setRequestHeader("File-Name", file.getName());
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
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
