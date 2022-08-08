/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package fight.utils;

import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * 字符串工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2021-11-28
 */
public class StringUtils {

    /**
     * 判断是否为非空字符串
     *
     * @param str 字符串
     * @return
     */
    public static boolean hasLength(@Nullable String str) {
        return null != str && !str.isEmpty();
    }

    /**
     * 按字节截取字符串
     *
     * @param str   待截取字符串
     * @param bytes 字节长度
     * @return
     */
    public static String substringByBytes(String str, int bytes) {
        if (hasLength(str)) {
            int len = 0;
            int strLength = str.length();
            for (int i = 0; i < strLength; i++) {
                // GBK 编码格式 中文占两个字节 UTF-8 编码格式中文占三个字节;
                len += (str.charAt(i) > 255 ? 3 : 1);
                if (len > bytes) {
                    return str.substring(0, i);
                }
            }
        }
        return str;
    }

    public static void main(String[] args) {
        long t0 = System.nanoTime();
        long l0 = System.currentTimeMillis();
        try {
            TimeUnit.MILLISECONDS.sleep(11L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long t1 = System.nanoTime();
        long l1 = System.currentTimeMillis();
        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);

        System.out.println(l1-l0);
        System.out.println(t1-t0);
        System.out.println(new BigDecimal(t1 - t0).divide(new BigDecimal(1000000),4,BigDecimal.ROUND_DOWN));
        System.out.println(millis);
    }
}
