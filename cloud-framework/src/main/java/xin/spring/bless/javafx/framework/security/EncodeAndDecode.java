package xin.spring.bless.javafx.framework.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 加密解密工具
 */
public class EncodeAndDecode {

    /**
     * Md5加密
     * @param str
     * @return
     */
    public static String md5encode(String str) {
        return DigestUtils.md5Hex(str);
    }

    /**
     * Base64加密
     * @param str
     * @return
     */
    public static String base64encode(String str) {
        byte[] b = Base64.encodeBase64(str.getBytes(), true);
        return new String(b);
    }
    /**
     * Base64解密
     * @param str
     * @return
     */
    public static String base64decode(String str) {
        byte[] b = Base64.decodeBase64(str.getBytes());
        return new String(b);
    }

    /**
     * 生成SHA1
     */
    public static String sHA1encode(String str) {
        return DigestUtils.sha1Hex(str);
    }

}
