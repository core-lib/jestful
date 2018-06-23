package org.qfox.jestful.sample.user;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-22 13:08
 **/
public class DateFormatTest {

    public static void main(String... args) throws IOException {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String format = dateFormat.format(new Date());
        System.out.println(format);
    }

}
