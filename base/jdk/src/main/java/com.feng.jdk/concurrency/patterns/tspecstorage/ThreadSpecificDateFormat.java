package com.feng.concurrency.patterns.tspecstorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author fengsy
 * @date 5/18/21
 * @Description
 */
public class ThreadSpecificDateFormat {
    static final ThreadLocal<SimpleDateFormat> TS_SDF;

    static {
        TS_SDF = ThreadLocal.withInitial(() -> new SimpleDateFormat());
    }

    public static Date parse(String timeStamp, String format) throws ParseException {
        final SimpleDateFormat sdf = TS_SDF.get();
        sdf.applyPattern(format);
        Date date = sdf.parse(timeStamp);
        return date;
    }

    public static void main(String[] args) throws ParseException {
        Date date = ThreadSpecificDateFormat.parse("20150501123040", "yyyyMMddHHmmss");
        System.out.println(date);

    }
}
