package fight.jdLogic;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import fight.commons.Configs;
import fight.commons.Constants;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Component
public class LoginLogic {

    protected static final Logger logger = LoggerFactory.getLogger(LoginLogic.class);

    static HashMap<String,String> HEADER=new HashMap<>();

    static {
        HEADER.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        HEADER.put("Connection","keep-alive");
    }

    @Autowired
    private Configs configs;

    private HttpRequest  HTTPREQUEST;

//    @PostConstruct
    public  void login(){

        HEADER.put("User-Agent",configs.getUserAgent());
        HTTPREQUEST  = HttpRequest.get(Constants.LOGIN_PAGE);
        HTTPREQUEST.headerMap(HEADER,true);
        HttpResponse httpResponse = HTTPREQUEST.execute();
        logger.info("登陆页面 响应结果:{}",httpResponse.getStatus());
        saveImage();
        logger.info(JSON.toJSONString(HTTPREQUEST));



    }


    private void saveImage(){

        HTTPREQUEST.setUrl(Constants.SHOW_CODE);
        HashMap<String,Object> param=new HashMap<>();
        param.put("appid",133);
        param.put("size",147);
        param.put("t",System.currentTimeMillis()+"");
        HTTPREQUEST.form(param);
        HashMap<String,String> header=new HashMap<>();
        header.putAll(HEADER);
        header.put("Referer",Constants.LOGIN_PAGE);
        HTTPREQUEST.headerMap(header,true);
        HttpResponse httpResponse = HTTPREQUEST.execute();

        try {
            FileUtils.writeByteArrayToFile(new File(configs.getShowCodePath()),httpResponse.bodyBytes());
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }

        logger.info("扫描二维码登陆: {}",configs.getShowCodePath());
    }


    private void getCodeTicket(){







    }

}
