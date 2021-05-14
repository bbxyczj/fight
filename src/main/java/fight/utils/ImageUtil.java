package fight.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * @author 钱多多-陈正健
 * @version 1.0
 * @date 2021/5/14 10:44
 */
public class ImageUtil {

    private static final Logger logger=LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 根据图片地址获取图片信息
     *
     * @param urlPath 网络图片地址
     * @return
     */
    public static byte[] getImageFromURL(String urlPath) {
        InputStream is = null;
        HttpURLConnection conn = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            URL url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            is = conn.getInputStream();
            if (conn.getResponseCode() == 200) {
                byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = -1;
                try {
                    while ((length = is.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, length);
                    }
                    byteArrayOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return byteArrayOutputStream.toByteArray();
            }
        } catch (MalformedURLException e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
                e.printStackTrace();
            }
            conn.disconnect();
        }
        return null;
    }



    /**
     * 获取网络图片字节数
     * @param pictureUrl
     * @return
     */
    public static float getPictureUrlKb(String pictureUrl){
        byte[] imageFromURL = getImageFromURL(pictureUrl);
        if(imageFromURL!=null&&imageFromURL.length>0){
            BigDecimal fileSize = new BigDecimal(imageFromURL.length);
            BigDecimal kilobyte = new BigDecimal(1024);
            float returnValue  = fileSize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
            return returnValue;
        }
        return 0;
    }


    /**
     * 获取本地图片的字节数
     * @param picturePath
     * @return
     */
    public static float getPicturePathKb(String picturePath) {
        float returnValue=0f;
        try {
            File file = new File(picturePath);
            BigDecimal fileSize = new BigDecimal(file.length());
            BigDecimal kilobyte = new BigDecimal(1024);
            returnValue = fileSize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return returnValue;
    }








    /**
     * 图片验证码
     * @param response
     */
    public void ImageMask(HttpServletResponse response) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        int width = 60, height = 20;
        //创建图象
        BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        //获取图形上下文
        Graphics graphics = image.getGraphics();
        //生成随机类
        Random random = new Random();
        //设定背景色
        graphics.setColor(getRandColor(200, 250));
        //验证码大小
        graphics.fillRect(0, 0, width, height);
        //设置字体
        graphics.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        //graphics.setColor(getRandColor(160, 200));
        //随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            graphics.drawLine(x, y, x + xl, y + yl);
        }
        //取随机产生的认证码(4位数字)
        String sRand = "";
        for (int i = 0; i < 4; i++) {
            //0~9
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            //设置数字颜色
            graphics.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            //把随机产生的4位数画在图片上
            graphics.drawString(rand, 13 * i + 6, 16);
        }
        //放在session中
        //图象生效
        graphics.dispose();
        //输出图象到页面
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }

    //生成随机颜色
    public Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255){
            fc = 255;
        }
        if (bc > 255){
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
