package fight.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: lpye
 * @description: 获取网络相关如ip port
 * @date: 2020/9/9
 */
public class NetUtil {

    private NetUtil() {}
    
    /**
     * 逗号
     */
    private static final String COMMA = ",";

    /**
     * 冒号
     */
    private static final String COLON = ":";

    /**
     * unknown
     */
    private static final String UNKNOWN = "unknown";

    /**
     * 获取远程IP
     * @return
     */
    public static String getRemoteIP() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            if(ip.split(COMMA).length>1){
                ip=ip.split(COMMA)[0];
            }
            if(ip.split(COLON).length>1){
                ip=ip.split(COLON)[0];
            }
            return ip;
        }
        return "";
    }

    /**
     * 获取上一级端口号，注意端口号","拼接,格式：客户端,nginx,网关,各级应用服务
     * 或者在网关层使用getServerPost获取nginx 端口
     * @return
     */
    public static int getPrePortNum() {

        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            String portStr=request.getHeader("X-Forwarded-Port");
            int port;
            if (portStr == null || portStr.length() == 0 || UNKNOWN.equalsIgnoreCase(portStr)) {
                port = request.getRemotePort();
            }else{
            	try{
            		String[] s = portStr.split(",");
            		port=Integer.parseInt(s[s.length-1].trim());
            	}catch (Exception e){
                    return 0;
                }
            }
            return port;
        }
        return 0;

    }
    /**
     * 获取客户端端口号，注意端口号","拼接,格式：客户端,nginx,网关,各级应用服务
     * 或者在网关层使用getServerPost获取nginx 端口
     * @return
     */
    public static int getFirstPortNum() {

        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            String portStr=request.getHeader("X-Forwarded-Port");
            int port;
            if (portStr == null || portStr.length() == 0 || UNKNOWN.equalsIgnoreCase(portStr)) {
                port = request.getRemotePort();
            }else{
            	try{
            		port=Integer.parseInt(portStr.split(",")[0].trim());
            	}catch (Exception e){
                    return 0;
                }
            }
            return port;
        }
        return 0;

    }

    public static String getHeader(String name) {

        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            return request.getHeader(name);
        }
        return StringUtils.EMPTY;
    }
}
