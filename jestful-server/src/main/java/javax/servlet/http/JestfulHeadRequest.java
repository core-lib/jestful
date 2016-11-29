package javax.servlet.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

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
 * @date 2016年6月1日 下午10:01:07
 *
 * @since 1.0.0
 */
public class JestfulHeadRequest extends HttpServletRequestWrapper {

	public JestfulHeadRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getMethod() {
		return "GET";
	}

}
