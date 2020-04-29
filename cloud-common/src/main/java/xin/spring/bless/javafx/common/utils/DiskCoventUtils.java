package xin.spring.bless.javafx.common.utils;

/**
 * 磁盘大小转换工具类
 */
public class DiskCoventUtils {

    public static String covent(long c, long t){
        if (c > 0 && t > 0) {
            int kb = 1024;
            int mb = kb * kb;
            int gb = mb * kb;
            int cgb = (int) (c / gb);
            int cmb = (int) (c % gb / mb);
            int ckb = (int) (c % gb % mb / kb);
            String res = "已用：" + cgb + "." + cmb + ckb + "G";
            int tgb = (int) (t / gb);
            int tmb = (int) (t % gb / mb);
            int tkb = (int) (t % gb % mb / kb);
            res += "\r\n总共：" + tgb + "." + tmb + tkb + "G";
            return res;
        }else {
            return "容量正在读取";
        }
    }

}
