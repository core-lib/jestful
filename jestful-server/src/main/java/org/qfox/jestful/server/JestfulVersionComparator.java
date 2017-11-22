package org.qfox.jestful.server;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年5月13日 下午7:25:29
 * @since 1.0.0
 */
public class JestfulVersionComparator implements VersionComparator {

    // 有版本号比没版本号更新
    public int compare(String a, String b) {
        if (a == null && b == null) {
            return 0;
        } else if (a == null) {
            return 1;
        } else if (b == null) {
            return -1;
        } else {
            return a.compareTo(b);
        }
    }

}
