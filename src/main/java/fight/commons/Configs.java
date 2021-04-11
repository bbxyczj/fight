package fight.commons;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class Configs {

    @Value("${jd.DEFAULT_USER_AGENT}")
    private String userAgent;
    @Value("${jd.eid}")
    private String eId;
    @Value("${jd.fp}")
    private String fp;
    @Value("${jd.sku_id}")
    private String skuId;

    @Value("${jd.show_code}")
    private String showCodePath;

}
