package xin.spring.bless.javafx.framework.exception;

/**功能描述: 依赖注入异常
$params$
*@return:$return$
*@Author:$user$
*@Date:$date$ $time$
*/
public class InjectException extends RuntimeException{

    public InjectException(){
        this("依赖依赖注入异常");
    }

    public InjectException(String msg){
        super(msg);
    }

    public InjectException(Exception e) {
        super(e);
    }

    public InjectException(Exception e, String msg) {
        super(msg, e, true, true);
    }
}
