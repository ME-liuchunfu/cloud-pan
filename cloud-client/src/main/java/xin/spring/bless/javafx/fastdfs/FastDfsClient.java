package xin.spring.bless.javafx.fastdfs;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import xin.spring.bless.javafx.framework.exception.FastDfsClientException;
import xin.spring.bless.javafx.framework.logs.Slf4jLog;

import java.io.File;
import java.io.IOException;

/**
 * 功能描述: fastdfs客户端
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
public class FastDfsClient implements Slf4jLog {

    private static final FastDfsClient instance = new FastDfsClient();

    private FastDfsClient(){
        //初始化
        try {
            log.info("初始化FastDfs配置文件");
            ClientGlobal.initByProperties("fastdfs-client.properties");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("初始化FastDfs配置文件  失败");
        } catch (MyException e) {
            e.printStackTrace();
            log.error("初始化FastDfs配置文件 失败");
        }
    }

    public static FastDfsClient newInstance(){
        return instance;
    }

    public String uploadFile(String path) throws FastDfsClientException {
        if(path == null || "".equals(path)){
            throw new FastDfsClientException("FastDfsClient客户端文件加载失败");
        }
        String fileid = null;
        try {
            log.info("创建TrackerClient创建客户端");
            //创建客户端
            TrackerClient tc = new TrackerClient();
            //连接tracker Server
            log.info("创建TrackerServer创建客户端");
            TrackerServer ts = tc.getConnection();
            if (ts == null) {
                log.info("创建TrackerServer创建客户端 失败");
                throw new FastDfsClientException("创建TrackerServer失败");
            }
            //获取一个storage server
            log.info("创建StorageServer创建客户端");
            StorageServer ss = tc.getStoreStorage(ts);
            if (ss == null) {
                log.info("创建StorageServer创建客户端 失败");
                throw new FastDfsClientException("创建StorageServer失败");
            }
            //创建一个storage存储客户端
            log.info("创建一个storage存储客户端");
            StorageClient1 sc1 = new StorageClient1(ts, ss);
            NameValuePair[] meta_list = null; //new NameValuePair[0];

            int i = path.lastIndexOf(".");
            String subFix = path.substring(i + 1);
            fileid = sc1.upload_file1(path, subFix, meta_list);
            log.info("上传成功，远程节点地址：{}", fileid);
        }catch (IOException e) {
            log.error("IOException FastDfsClient异常");
            e.printStackTrace();
            throw new FastDfsClientException(e,"创建FastDfsClient IOException失败");
        } catch (MyException e) {
            log.error("IOException FastDfsClient异常");
            e.printStackTrace();
            throw new FastDfsClientException(e, "创建FastDfsClient MyException失败");
        }
        return fileid;
    }

    public String uploadFile(File file) throws FastDfsClientException {
        if(file == null){
            throw new FastDfsClientException("FastDfsClient客户端文件加载失败");
        }
        return uploadFile(file.getAbsolutePath());
    }

}
