package fight.commons;

import java.io.Serializable;

/**
 * @Author XE-CZJ
 * @Date 2023/3/1 10:28
 */
public class BaseBean<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;

    // appid
    protected String aid;
    // 具体传参
    protected T data;


    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
