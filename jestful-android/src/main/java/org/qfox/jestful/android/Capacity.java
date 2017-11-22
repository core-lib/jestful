package org.qfox.jestful.android;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Description: 计算机空间容量
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年5月31日 下午7:20:56
 * @since 1.0.0
 */
public class Capacity {
    private final long value;
    private final Unit unit;

    private Capacity(long value, Unit unit) {
        super();
        this.value = value;
        this.unit = unit;
    }

    public static Capacity valueOf(String string) {
        if (string == null) {
            throw new IllegalArgumentException("string can not be null");
        }
        String regex = "\\s*(\\d+)\\s*((?i)(K|M|G|T|P)?B(?i))\\s*";
        if (!string.matches(regex)) {
            throw new IllegalArgumentException("invalid capacity string " + string);
        }
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(string);
        matcher.find();
        long value = Long.valueOf(matcher.group(1));
        Unit unit = Unit.valueOf(matcher.group(2).toUpperCase());
        return new Capacity(value, unit);
    }

    public long toByteSize() {
        return unit.toByteSize(value);
    }

    public long getValue() {
        return value;
    }

    public Unit getUnit() {
        return unit;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        result = prime * result + (int) (value ^ (value >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Capacity other = (Capacity) obj;
        if (unit != other.unit)
            return false;
        return value == other.value;
    }

    @Override
    public String toString() {
        return value + unit.name();
    }

    public enum Unit {
        B {
            @Override
            public long toByteSize(long value) {
                return value;
            }
        },
        KB {
            @Override
            public long toByteSize(long value) {
                return B.toByteSize(1024L * value);
            }
        },
        MB {
            @Override
            public long toByteSize(long value) {
                return KB.toByteSize(1024L * value);
            }
        },
        GB {
            @Override
            public long toByteSize(long value) {
                return MB.toByteSize(1024L * value);
            }
        },
        TB {
            @Override
            public long toByteSize(long value) {
                return GB.toByteSize(1024L * value);
            }
        },
        PB {
            @Override
            public long toByteSize(long value) {
                return TB.toByteSize(1024L * value);
            }
        };

        public abstract long toByteSize(long value);

    }

}
