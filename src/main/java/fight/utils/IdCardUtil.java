package fight.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author XE-CZJ
 * @Date 2022/10/11 15:22
 */
public class IdCardUtil {

    private static final String patten="(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)" +
            "|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
    private static final Pattern idRegular= Pattern.compile(patten);

    private static final int[] ISO7064_a = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final char[] ISO7064_b = new char[]{'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};


    /**
     * 18 位身份证 xxxxxx  xxxxxxxxxxxx xxx x
     * 第一部分： 行政区划  0位 华北为1，东北为2，华东为3，中南为4，西南为5，西北为6 ，789港澳台+海外  1 位 省份
     * 1、华北地区：北京市|11，天津市|12，河北省|13，山西省|14，内蒙古自治区|15。
     * 2、东北地区：辽宁省|21，吉林省|22，黑龙江省|23。
     * 3、华东地区：上海市|31，江苏省|32，浙江省|33，安徽省|34，福建省|35，江西省|36，山东省|37。
     * 4、华中地区：河南省|41，湖北省|42，湖南省|43。
     * 5、华南地区：广东省|44，广西壮族自治区|45，海南省|46.
     * 6、西南地区：四川省|51，贵州省|52，云南省|53，西藏自治区|54，重庆市|50。
     * 7、西北地区：陕西省|61，甘肃省|62，青海省|63，宁夏回族自治区|64，新疆维吾尔自治区|65。
     * 8、特别地区：香港特别行政区（852)|81，澳门特别行政区（853)|82。(还没回归)台湾地区(886)|83。*
     * 第二部分： 出生年月日 yyyyMMdd
     * 第三部分： 地区+出生日期一致的 排序 男奇数 女偶数  ，好奇一个区县 一天出生超过500人怎么办
     * 第四部分： 校验码，具体是ISO7064 标准
     * 前17位与ISO7064_a 按顺序相乘 结果相加  再以11 取余  得到的就是 ISO7064_b 的下标 取出value对比最后一位
     * @param idCard
     * @return
     */
    public static boolean checkIdCard(String idCard) {
        Matcher matcher = idRegular.matcher(idCard);
        if (!matcher.matches()) {
            return false;
        }
        if(idCard.length()==15){
            return true;
        }
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            int multiply = Integer.valueOf(String.valueOf(idCard.charAt(i))) * ISO7064_a[i];
            sum += multiply;
        }
        return idCard.charAt(17) == ISO7064_b[sum % 11];
    }


    public static void main(String[] args) {
        System.out.println(checkIdCard("342423930717522"));
    }
}
