package fight.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

public class SecretKeyUtil {
    private static final String ECB_PKCS5_PADDING = "/ECB/PKCS5Padding";
    public static final String AES = "AES";
    public static final String DES = "DES";
    public static final String DES3 = "DESede";

    /**
     * AES 加密
     *
     * @param key
     * @param data
     * @return
     */
    public static byte[] crypt(byte key[], byte data[], String type) {
        try {
            KeySpec ks;
            SecretKeyFactory kf;
            SecretKey ky;
            if (type.equals(DES3)) {
                kf = SecretKeyFactory.getInstance(DES3);
                DESedeKeySpec dks = new DESedeKeySpec(key);
                ky = kf.generateSecret(dks);
            } else {
                kf = SecretKeyFactory.getInstance(type);
                ks = new DESKeySpec(key);
                ky = kf.generateSecret(ks);
            }

            Cipher c = Cipher.getInstance(type + ECB_PKCS5_PADDING);
            c.init(Cipher.ENCRYPT_MODE, ky);
            return c.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AES 解密
     *
     * @param key
     * @param data
     * @return
     */
    public static byte[] decrypt(byte key[], byte data[], String type) {
        try {
            KeySpec ks;
            SecretKeyFactory kf;
            SecretKey ky;
            if (type.equals(DES3)) {
                kf = SecretKeyFactory.getInstance(DES3);
                DESedeKeySpec dks = new DESedeKeySpec(key);
                ky = kf.generateSecret(dks);
            } else {
                kf = SecretKeyFactory.getInstance(type);
                ks = new DESKeySpec(key);
                ky = kf.generateSecret(ks);
            }

            Cipher c = Cipher.getInstance(type + ECB_PKCS5_PADDING);
            c.init(Cipher.DECRYPT_MODE, ky);
            return c.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    public static String byteToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer();
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes(), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('A' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String byteArrayToHexString(byte source[], int offset, int size) {
        String dest = "", strTmp = null;
        int len = source.length;
        if ((null == source) || (len <= 0))
            return dest;

        for (int tmp = offset; tmp < len && tmp < offset + size; tmp++) {
            strTmp = String.format("%02x", source[tmp]);
            dest += strTmp;
        }

        return dest;
    }

    public static void main(String[] args) {


        byte[] crypt = crypt(new String("").getBytes(), new String("").getBytes(), AES);

        System.out.println(new String(crypt));


        String data = "acaa22ef7508c1c7593c9b64502b8590ed454fc5b480602cd70acf70935fe1ca0f72151de0a2fd8d8fc26109c25639460814ab45f80e7be628644efaf16774c5477e9b4bb4a6d1836689c1098b928f51dd55d20addb0e70159012f35c400bc9e197c632be7ce2e49";
        byte[] byteAuth = SecretKeyUtil.hexToBytes(data);
        String k;
		try {
			k = SecretKeyUtil.SHA1("9999999");
			 // 3Des解码
	        byte[] decodeByte = SecretKeyUtil.decrypt(k.getBytes(), byteAuth,
	                SecretKeyUtil.DES3);
	        if (decodeByte != null) {
	            String decodedAuth = new String(decodeByte);
	            System.err.println(decodedAuth);
	        }
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        
        
//        try {
//            byte[] r = SecretKeyUtil.decrypt("babyxialiu0303".getBytes(), SecretKeyUtil.hexToBytes(data), SecretKeyUtil.DES);
//            System.out.println(new String(r));
//            String date = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
//            System.out.println("data = " + date);
//            data = "testhm052100002|0HISENSETV$0000001a9501000f|128073088876206695|ahdx|" + date;
//            byte[] encodeByte = SecretKeyUtil.crypt("youku_cfl0398xp".getBytes(), data.getBytes(), SecretKeyUtil.DES);
//            String token = SecretKeyUtil.byteToHexString(encodeByte);
//            System.out.println("token = " + new String(token));
//            r = SecretKeyUtil.decrypt("youku_cfl0398xp".getBytes(), SecretKeyUtil.hexToBytes(token), SecretKeyUtil.DES);
//            System.out.println(new String(r));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }
}
