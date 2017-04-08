package org.qfox.jestful.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

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
 * @date 2016年4月30日 下午5:31:46
 * @since 1.0.0
 */
public class Accepts implements Iterable<MediaType> {
    private final Set<MediaType> mediaTypes;

    public Accepts(Collection<MediaType> mediaTypes) {
        super();
        this.mediaTypes = new TreeSet<MediaType>(mediaTypes);
    }

    public static Accepts valueOf(String accept) {
        Set<MediaType> mediaTypes = new TreeSet<MediaType>();
        String[] contentTypes = accept != null && accept.length() != 0 ? accept.split(",") : new String[0];
        for (String contentType : contentTypes) {
            MediaType mediaType = MediaType.valueOf(contentType);
            mediaTypes.add(mediaType);
        }
        return new Accepts(mediaTypes);
    }

    public int size() {
        return mediaTypes.size();
    }

    public boolean contains(String contentType) {
        return contains(MediaType.valueOf(contentType));
    }

    public boolean contains(MediaType mediaType) {
        for (MediaType type : mediaTypes) {
            if (type.matches(mediaType)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Iterator<MediaType> iterator() {
        return mediaTypes.iterator();
    }

    public boolean retainAll(Accepts accepts) {
        return this.mediaTypes.retainAll(accepts.mediaTypes);
    }

    public String toString() {
        return toString(null);
    }

    public String toString(String version) {
        StringBuilder builder = new StringBuilder();
        Iterator<MediaType> iterator = iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next().toString(version));
            if (iterator.hasNext()) {
                builder.append(",");
            }
        }
        return builder.toString();
    }

}
