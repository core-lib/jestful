package org.qfox.jestful.commons.io;

import java.util.Collections;
import java.util.Map;

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
 * @date 2016年4月9日 下午7:05:15
 *
 * @since 1.0.0
 */
public class Disposition {
	private final String type;
	private final String name;
	private final String filename;
	private final Map<String, String> parameters;

	private Disposition(String type, Map<String, String> parameters) {
		super();
		this.type = type;
		this.parameters = Collections.unmodifiableMap(parameters);
		this.name = parameters.get("name");
		this.filename = parameters.get("filename");
	}

	public static Disposition valueOf(String value) {
		if (value == null) {
			throw new NullPointerException();
		}
		if (value.matches("(?i)Content-Disposition:(?i)[^:;]+(;[^=;]+=[^=;]+)*") == false) {
			throw new IllegalArgumentException(value);
		}
		return null;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getFilename() {
		return filename;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

}
