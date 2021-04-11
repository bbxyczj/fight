package fight.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.bestv.ops.manager.common.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * http工具类
 *
 * @author ShuYi
 * @date 2020/10/19 10:06
 */
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private HttpUtils() {}
    
    /**
     * get请求
     *
     * @param url
     *            请求地址
     * @param username
     *            账号
     * @param password
     *            密码
     * @return com.alibaba.fastjson.JSONObject
     */
    public static String get(String url, String username, String password, Integer connectionTimeout) {
        HttpRequest httpRequest = HttpUtil.createGet(url);
        httpRequest.setConnectionTimeout(connectionTimeout);
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            httpRequest.basicAuth(username, password);
        }
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpRequest.execute();
            if (httpResponse.isOk()) {
                InputStream inputStream = httpResponse.bodyStream();
                return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(httpResponse)) {
                httpResponse.close();
            }
        }
        return StringUtils.EMPTY;
    }


    public static String httpPost(String requestUrl, String param){
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        OutputStreamWriter out = null;
        StringBuilder responseBuilder = new StringBuilder();
        try {
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            out = new OutputStreamWriter(conn.getOutputStream(), Charset.forName("UTF-8"));
            out.write(param);
            out.flush();
            int code = conn.getResponseCode();
            if (code == 200) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
            }
        } catch (Exception ex) {
            logger.error("error in HttpUtil,the request url:{},the param:{}", requestUrl, param, ex);
            throw new ServiceException("post请求失败:"+ex.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception ex) {
                logger.error("error in HttpUtil,the request url:{},the param:{}", requestUrl, param, ex);
                //ignore
            }
        }
        return responseBuilder.toString();
    }


    public static void main(String[] args) throws Exception {
        String s = httpPost("http://106.14.166.158:19083/admin/api/show-config", "sign=NDc4MWM4ZTI3NTY4ZDlhMjE0NWM5ODUxZjIwMDc4Yjk5MjA1YWQ4MA==&path=show-config&time=1615370390105");

        System.out.println(JSONUtil.parseObj(s).get("data"));
    }


}
