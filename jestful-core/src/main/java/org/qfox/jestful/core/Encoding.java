package org.qfox.jestful.core;

import java.util.Collections;
import java.util.Map;

import org.qfox.jestful.commons.Weighted;

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
 * @date 2016年5月15日 上午11:22:53
 *
 * @since 1.0.0
 */
public class Encoding implements Weighted<Encoding> {
	private final String name;
	private final Map<String, String> parameters;
	private final float weight;

	public Encoding(String name, Map<String, String> parameters) {
		super();
		this.name = name;
		this.parameters = Collections.unmodifiableMap(parameters);
		this.weight = parameters.containsKey("q") ? Float.valueOf(parameters.get("q")) : 1.0f;
	}
	
	public int compareTo(Encoding o) {
		return weight > o.weight ? -1 : weight < o.weight ? 1 : name.compareTo(o.name);
	}

	public String getName() {
		return name;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public float getWeight() {
		return weight;
	}

}
