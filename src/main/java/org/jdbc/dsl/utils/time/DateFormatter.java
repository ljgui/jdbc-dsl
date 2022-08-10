package org.jdbc.dsl.utils.time;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 日期格式化工具
 */
public interface DateFormatter {

    List<DateFormatter> supportFormatter = new ArrayList<>(Arrays.asList(
            /** 常见格式 */
            new DefaultDateFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2}"), "yyyyMMdd")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}"), "yyyy-MM-dd")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9]{2}"), "yyyy/MM/dd")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]{2}日"), "yyyy年MM月dd日")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]月[0-9]日"), "yyyy年M月d日")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]日"), "yyyy年MM月d日")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]月[0-9]{2}日"), "yyyy年M月dd日")

            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9]"), "yyyy-M-d")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]"), "yyyy-MM-d")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9]{2}"), "yyyy-M-dd")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9]"), "yyyy/M/d")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9]"), "yyyy/MM/d")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9]{2}"), "yyyy/M/dd")

            /** 佳都习惯格式 */
            // yyMMddHHmmss
            , new DefaultDateFormatter(Pattern.compile("[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}"), "yyMMddHHmmss")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}"), "yyMMddHHmm")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{2}[0-9]{2}[0-9]{2}"), "yyMMdd")


            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-MM-dd HH:mm:ss")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9] [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-M-d HH:mm:ss")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9] [0-9]:[0-9]:[0-9]"), "yyyy-M-d H:m:s")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9] [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-MM-d HH:mm:ss")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-M-dd HH:mm:ss")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9]{2} [0-9]:[0-9]:[0-9]"), "yyyy-M-dd H:m:s")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9] [0-9]:[0-9]:[0-9]"), "yyyy-MM-d H:m:s")


            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy/MM/dd HH:mm:ss")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9] [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy/M/d HH:mm:ss")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9] [0-9]:[0-9]:[0-9]"), "yyyy/M/d H:m:s")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9] [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy/MM/d HH:mm:ss")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy/M/dd HH:mm:ss")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9]{2} [0-9]:[0-9]:[0-9]"), "yyyy/M/dd H:m:s")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9] [0-9]:[0-9]:[0-9]"), "yyyy/MM/d H:m:s")

            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\\+[0-9]{4}"), "yyyy-MM-dd HH:mm:ssZ")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-MM-dd'T'HH:mm:ss")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}\\+[0-9]{4}"), "yyyy-MM-dd'T'HH:mm:ssZ")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]{2}日[0-9]{2}时[0-9]{2}分[0-9]{2}秒"), "yyyy年MM月dd日HH时mm分ss秒")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]{2}日 [0-9]{2}时[0-9]{2}分[0-9]{2}秒"), "yyyy年MM月dd日 HH时mm分ss秒")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{2}:[0-9]{2}:[0-9]{2}"), "HH:mm:ss")

            /** 奇奇怪怪的格式 */
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyyMMdd HH:mm:ss")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}"), "yyyyMMddHHmmss")
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2} [0-9]{2}[0-9]{2}[0-9]{2}"), "yyyyMMdd HHmmss")

            , new SampleJDKDateFormatter(str -> str.contains("年") && str.contains("CST") && str.split("[ ]").length == 6, () -> new SimpleDateFormat("yyyy年 MM月 dd日 EEE HH:mm:ss 'CST'", Locale.CHINESE))
            , new SampleJDKDateFormatter(str -> str.contains("年") && str.contains("GMT") && str.split("[ ]").length == 6, () -> new SimpleDateFormat("yyyy年 MM月 dd日 EEE HH:mm:ss 'GMT'", Locale.CHINESE))
            , new SampleJDKDateFormatter(str -> str.contains("CST") && str.split("[ ]").length == 6, () -> new SimpleDateFormat("EEE MMM dd HH:mm:ss 'CST' yyyy", Locale.US))
            , new SampleJDKDateFormatter(str -> str.contains("GMT") && str.split("[ ]").length == 6, () -> new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.US))
            , new SampleJDKDateFormatter(str -> str.endsWith("AM") || str.endsWith("PM") && str.split("[ ]").length == 5, () -> new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH))
    ));

    boolean support(String str);

    Date format(String str);

    String toString(Date date);

    String getPattern();

    static Optional<Date> fromString(String dateString) {
        Optional<DateFormatter> formatterOptional = getFormatter(dateString);
        if (formatterOptional.isPresent()) {
            return Optional.ofNullable(formatterOptional.get().format(dateString));
        }
        return Optional.ofNullable(null);
    }

    static Optional<Date> fromString(String dateString, String pattern) {
        try {
            return Optional.ofNullable(new SimpleDateFormat(pattern).parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
            return Optional.ofNullable(null);
        }
    }

    static Optional<String> toString(Date date, String format) {
        if (null == date) return Optional.ofNullable(null);
        for (DateFormatter formatter : supportFormatter) {
            if (formatter.getPattern().equals(format))
                return Optional.ofNullable(formatter.toString(date));
        }
        return Optional.ofNullable(new DateTime(date).toString(format));
    }

    static boolean isSupport(String dateString) {
        return !(dateString == null || dateString.length() < 4) && supportFormatter.parallelStream().anyMatch(formatter -> formatter.support(dateString));
    }

    static Optional<DateFormatter> getFormatter(String dateString) {
        if (dateString == null || dateString.length() < 4) return Optional.ofNullable(null);
        for (DateFormatter formatter : supportFormatter) {
            if (formatter.support(dateString))
                return Optional.ofNullable(formatter);
        }
        return Optional.ofNullable(null);
    }
}
