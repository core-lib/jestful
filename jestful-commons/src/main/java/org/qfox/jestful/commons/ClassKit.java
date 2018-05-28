package org.qfox.jestful.commons;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 字节码工具
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-28 13:39
 **/
public class ClassKit {
    private static final ConcurrentMap<Class<?>, Classy> CACHE = new ConcurrentHashMap<Class<?>, Classy>();

    public static Classy read(final Class<?> clazz) throws IOException {
        Classy classy = CACHE.get(clazz);
        if (classy != null) return classy;

        InputStream in = null;
        DataInputStream dis = null;
        try {
            in = clazz.getResourceAsStream("/" + clazz.getName().replace('.', '/') + ".class");
            if (in == null) return null;
            dis = new DataInputStream(new BufferedInputStream(in));
            Map<String, Methody> methods = new HashMap<String, Methody>();
            Map<Integer, String> strings = new HashMap<Integer, String>();
            Map<Integer, Integer> indexes = new HashMap<Integer, Integer>();
            dis.skipBytes(4);//Magic
            dis.skipBytes(2);//副版本号
            dis.skipBytes(2);//主版本号

            //读取常量池
            int constant_pool_count = dis.readUnsignedShort();
            for (int i = 0; i < (constant_pool_count - 1); i++) {
                byte flag = dis.readByte();
                switch (flag) {
                    case 7://CONSTANT_Class:
                        int z = dis.readUnsignedShort();
                        indexes.put(i + 1, z);
                        break;
                    case 9://CONSTANT_Fieldref:
                    case 10://CONSTANT_Methodref:
                    case 11://CONSTANT_InterfaceMethodref:
                        dis.skipBytes(2);
                        dis.skipBytes(2);
                        break;
                    case 8://CONSTANT_String:
                        dis.skipBytes(2);
                        break;
                    case 3://CONSTANT_Integer:
                    case 4://CONSTANT_Float:
                        dis.skipBytes(4);
                        break;
                    case 5://CONSTANT_Long:
                    case 6://CONSTANT_Double:
                        dis.skipBytes(8);
                        i++;//必须跳过一个,这是class文件设计的一个缺陷,历史遗留问题
                        break;
                    case 12://CONSTANT_NameAndType:
                        dis.skipBytes(2);
                        dis.skipBytes(2);
                        break;
                    case 1://CONSTANT_Utf8:
                        int len = dis.readUnsignedShort();
                        byte[] data = new byte[len];
                        dis.readFully(data);
                        strings.put(i + 1, new String(data, "UTF-8"));//必然是UTF8的
                        break;
                    case 15://CONSTANT_MethodHandle:
                        dis.skipBytes(1);
                        dis.skipBytes(2);
                        break;
                    case 16://CONSTANT_MethodType:
                        dis.skipBytes(2);
                        break;
                    case 18://CONSTANT_InvokeDynamic:
                        dis.skipBytes(2);
                        dis.skipBytes(2);
                        break;
                    default:
                        throw new RuntimeException("Impossible!! flag=" + flag);
                }
            }

            dis.skipBytes(2);//版本控制符
            //类名
            int z = dis.readUnsignedShort();
            Integer index = indexes.get(z);
            String type = strings.get(index);
            if (type != null) type = type.replace('/', '.');
            dis.skipBytes(2);//超类

            //跳过接口定义
            int interfaces_count = dis.readUnsignedShort();
            dis.skipBytes(2 * interfaces_count);//每个接口数据,是2个字节

            //跳过字段定义
            int fields_count = dis.readUnsignedShort();
            for (int i = 0; i < fields_count; i++) {
                dis.skipBytes(2);
                dis.skipBytes(2);
                dis.skipBytes(2);
                int attributes_count = dis.readUnsignedShort();
                for (int j = 0; j < attributes_count; j++) {
                    dis.skipBytes(2);//跳过访问控制符
                    int attribute_length = dis.readInt();
                    dis.skipBytes(attribute_length);
                }
            }

            //开始读取方法
            int methods_count = dis.readUnsignedShort();
            for (int i = 0; i < methods_count; i++) {
                dis.skipBytes(2); //跳过访问控制符
                String methodName = strings.get(dis.readUnsignedShort());
                String descriptor = strings.get(dis.readUnsignedShort());
                short attributes_count = dis.readShort();
                String key = methodName + "," + descriptor;
                for (int j = 0; j < attributes_count; j++) {
                    String attrName = strings.get(dis.readUnsignedShort());
                    int attribute_length = dis.readInt();
                    if ("Code".equals(attrName)) { //形参只在Code属性中
                        dis.skipBytes(2);
                        dis.skipBytes(2);
                        int code_len = dis.readInt();
                        dis.skipBytes(code_len); //跳过具体代码
                        int exception_table_length = dis.readUnsignedShort();
                        dis.skipBytes(8 * exception_table_length); //跳过异常表

                        int code_attributes_count = dis.readUnsignedShort();
                        for (int k = 0; k < code_attributes_count; k++) {
                            int str_index = dis.readUnsignedShort();
                            String codeAttrName = strings.get(str_index);
                            int code_attribute_length = dis.readInt();
                            if ("LocalVariableTable".equals(codeAttrName)) {//形参在LocalVariableTable属性中
                                int local_variable_table_length = dis.readUnsignedShort();
                                //按本地变量表的Slot属性升序排列，保证形参名称的位置与所对应参数的位置一致
                                TreeMap<Integer, String> varSlotNameMap = new TreeMap<Integer, String>();
                                for (int l = 0; l < local_variable_table_length; l++) {
                                    dis.skipBytes(2);
                                    dis.skipBytes(2);
                                    String varName = strings.get(dis.readUnsignedShort());
                                    dis.skipBytes(2);
                                    int varSlot = dis.readUnsignedShort();//这是变量的位置
                                    //非静态方法,第一个参数是this
                                    if (!"this".equals(varName)) {
                                        varSlotNameMap.put(varSlot, varName);
                                    }
                                }

                                List<String> varNames = new ArrayList<String>(varSlotNameMap.values());
                                if (!methods.containsKey(key)) methods.put(key, new Methody(key, varNames));
                            } else if ("LineNumberTable".equals(codeAttrName)) {
                                int len = dis.readUnsignedShort();
                                if (len > 0) {
                                    dis.skipBytes(2);
                                    dis.readUnsignedShort();
                                    dis.skipBytes(code_attribute_length - 6);
                                }
                            } else {
                                dis.skipBytes(code_attribute_length);
                            }
                        }
                    } else if ("MethodParameters".equals(attrName)) {
                        // JDK 8的参数名存储, 需要编译时加了-parameters 选项
                        // http://www.java-allandsundry.com/2013/12/java-8-parameter-name-at-runtime.html
                        int paramCount = dis.readByte();
                        List<String> varNames = new ArrayList<String>(paramCount);
                        for (int l = 0; l < paramCount; l++) {
                            String varName = strings.get(dis.readUnsignedShort());
                            dis.skipBytes(2);
                            //非静态方法,第一个参数是this
                            if (!"this".equals(varName)) varNames.add(varName);
                        }
                        methods.put(key, new Methody(key, varNames));
                    } else {
                        dis.skipBytes(attribute_length);
                    }
                }
            }
            return classy = new Classy(type, methods);
        } finally {
            if (classy != null) CACHE.put(clazz, classy);
            if (dis != null) IOKit.close(dis);
            if (in != null) IOKit.close(in);
        }
    }

    public static String sign(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            char c;
            if (clazz == Integer.TYPE) {
                c = 'I';
            } else if (clazz == Long.TYPE) {
                c = 'J';
            } else if (clazz == Boolean.TYPE) {
                c = 'Z';
            } else if (clazz == Byte.TYPE) {
                c = 'B';
            } else if (clazz == Character.TYPE) {
                c = 'C';
            } else if (clazz == Short.TYPE) {
                c = 'S';
            } else if (clazz == Double.TYPE) {
                c = 'D';
            } else if (clazz == Float.TYPE) {
                c = 'F';
            } else /* if (d == Void.TYPE) */ {
                c = 'V';
            }
            return new String(new char[]{c});
        } else if (clazz.isArray()) {
            return "[" + sign(clazz.getComponentType());
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append('L');
            String name = clazz.getName();
            int len = name.length();
            for (int i = 0; i < len; ++i) {
                char c = name.charAt(i);
                builder.append(c == '.' ? '/' : c);
            }
            builder.append(';');
            return builder.toString();
        }
    }

}
