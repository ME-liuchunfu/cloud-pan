package xin.spring.bless.javafx.framework.exception;

/**功能描述: FastDfs客户端异常
$params$
*@return:$return$
*@Author:$user$
*@Date:$date$ $time$
*/
public class FastDfsClientException extends RuntimeException{

    public FastDfsClientException(){
        this("FastDfs客户端异常");
    }

    public FastDfsClientException(String msg){
        super(msg);
    }

    public FastDfsClientException(Exception e) {
        super(e);
    }

    public FastDfsClientException(Exception e, String msg) {
        super(msg, e, true, true);
    }
}
