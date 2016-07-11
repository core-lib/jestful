package org.qfox.jestful.translation;

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
 * @date 2016年6月22日 下午8:55:49
 *
 * @since 1.0.0
 */
public class Type implements Comparable<Type> {
	private String directory;
	private String name;

	public Type() {
		super();
	}

	public Type(String directory, String name) {
		super();
		this.directory = directory;
		this.name = name;
	}

	public int compareTo(Type o) {
		return directory.compareTo(o.directory) != 0 ? directory.compareTo(o.directory) : name.compareTo(o.name);
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
