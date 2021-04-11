package fight.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * created with IDEA
 *
 * @author chenzhengjian
 * @version 1.0
 * @date 2020/10/10 17:05
 */
public class ReadParamUtil {

    private static final Logger log = LoggerFactory.getLogger(ReadParamUtil.class);


    public static Map<String, Object> readParam(HttpServletRequest request) {
        Map<String, Object> param = new HashMap();
        InputStream in=null;
        try {
            String method = request.getMethod();
            if (HttpMethod.GET.name().equals(method.toUpperCase())) {
                Map<String, String[]> map = request.getParameterMap();
                // 打印请求url参数
                if (map != null) {
                    for (Map.Entry<String, String[]> entry : map.entrySet()) {
                        String key = entry.getKey();
                        String value = printArray(entry.getValue());
                        param.put(key, value);
                    }

                }
            } else if (HttpMethod.POST.name().equals(method.toUpperCase())) {
                in=request.getInputStream();
                String reqBody = IoUtil.read(in, "UTF-8");
                if (reqBody != null) {
                    String conType = request.getHeader("Content-Type");
                    if((StrUtil.isBlank(conType)||conType.contains(MediaType.APPLICATION_JSON_VALUE))&&JSONUtil.isJson(reqBody)){
                        //默认content-type传json-->application/json
                        JSONObject jsonObject = JSONUtil.parseObj(reqBody);
                        if(jsonObject!=null){
                            jsonObject.keySet().forEach(key->{
                                param.put(key,jsonObject.get(key));
                            });
                        }
                    }else if (StrUtil.isNotBlank(conType)&&(conType.contains(MediaType.TEXT_PLAIN_VALUE)||conType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE))) {
                        Map<String, Object> urlParams = UrlParamUtil.getUrlParams(reqBody);
                        param.putAll(urlParams);
                    }
                }
            }
        } catch (IOException e) {
            log.error("请求流解析异常：", e);
        } finally {
            IoUtil.close(in);
        }
        return param;
    }


    private static String printArray(String[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
