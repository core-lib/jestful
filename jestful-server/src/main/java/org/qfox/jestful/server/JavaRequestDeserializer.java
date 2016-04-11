package org.qfox.jestful.server;  

import java.io.IOException;
import java.io.InputStream;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.Multipart;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
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
 * @date 2016年4月11日 下午9:39:11
 *
 * @since 1.0.0 
 */
public class JavaRequestDeserializer implements RequestDeserializer {

	public String getContentType() {
		return "application/x-java-serialized-object";
	}

	public void deserialize(Action action, MediaType mediaType, InputStream in) throws IOException {
		
	}

	public void deserialize(Action action, Parameter parameter, Multipart multipart, InputStream in) throws IOException {
		
	}

}
 