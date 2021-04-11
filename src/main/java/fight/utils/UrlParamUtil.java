package fight.utils;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created with IDEA
 *
 * @author chenzhengjian
 * @version 1.0
 * @date 2020/10/10 18:33
 */
public class UrlParamUtil {
    private static final Logger log = LoggerFactory.getLogger(UrlParamUtil.class);

    /**
     * 将url参数转换成map
     * @param param aa=11&bb=22&cc=33
     * @return
     */
    public static Map<String, Object> getUrlParams(String param) {
        Map<String, Object> map = new HashMap<String, Object>(4);
        if (StrUtil.isBlank(param)) {
            return map;
        }
        try {
            String[] params = param.split("&");
            for (int i = 0; i < params.length; i++) {
                String[] p = params[i].split("=");
                if (p.length == 2) {
                    map.put(p[0], p[1]);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return map;
    }

    /**
     * 将map转换成url
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }


    public static boolean checkStatic(String url,String suffix) {
        Pattern pattern = Pattern.compile("^.+\\.(?i)(" + suffix + ")$");
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }
}
