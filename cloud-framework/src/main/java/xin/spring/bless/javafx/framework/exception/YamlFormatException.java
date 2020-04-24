package xin.spring.bless.javafx.framework.exception;

import java.io.FileNotFoundException;

/**功能描述:
$params$
*@return:$return$
*@Author:$user$
*@Date:$date$ $time$
*/
public class YamlFormatException extends RuntimeException{

    public YamlFormatException(){
        this("解析yaml文件异常");
    }

    public YamlFormatException(String msg){
        super(msg);
    }

    public YamlFormatException(Exception e) {
        super(e);
    }

    public YamlFormatException(Exception e, String msg) {
        super(msg, e, true, true);
    }
}
