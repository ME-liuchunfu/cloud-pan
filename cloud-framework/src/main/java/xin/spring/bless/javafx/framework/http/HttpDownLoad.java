package xin.spring.bless.javafx.framework.http;

import okhttp3.*;

import javax.xml.ws.http.HTTPException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络下载工具
 */
public class HttpDownLoad{

    private static HttpDownLoad downloadUtil;

    public static HttpDownLoad newInstance() {
        if (downloadUtil == null) {
            downloadUtil = new HttpDownLoad();
        }
        return downloadUtil;
    }

    private HttpDownLoad() {}

    /**
     * @param url          下载连接
     * @param parentDir  下载的文件储存目录
     * @param fileName 下载文件名称，后面记得拼接后缀，否则手机没法识别文件类型
     * @param listener     下载监听
     */
    public void downloadAsyn(final String url, final String parentDir, final String fileName, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        //异步请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败监听回调
                listener.onDownloadFailed(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[4096];
                int len = 0;
                FileOutputStream fos = null;
                //储存下载文件的目录
                File dir = new File(parentDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, fileName);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        //下载中更新进度条
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    //下载完成
                    listener.onDownloadSuccess(file);
                } catch (Exception e) {
                    listener.onDownloadFailed(e);
                }finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * @param url          下载连接
     * @param parentDir  下载的文件储存目录
     * @param fileName 下载文件名称，后面记得拼接后缀，否则手机没法识别文件类型
     * @param listener     下载监听
     */
    public void downloadSync(final String url, final String parentDir, final String fileName, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            InputStream is = null;
            byte[] buf = new byte[4096];
            int len = 0;
            FileOutputStream fos = null;
            //储存下载文件的目录
            File dir = new File(parentDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            try {
                is = response.body().byteStream();
                long total = response.body().contentLength();
                fos = new FileOutputStream(file);
                long sum = 0;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    sum += len;
                    int progress = (int) (sum * 1.0f / total * 100);
                    //下载中更新进度条
                    listener.onDownloading(progress);
                }
                fos.flush();
                //下载完成
                listener.onDownloadSuccess(file);
            } catch (Exception e) {
                listener.onDownloadFailed(e);
            }finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            listener.onDownloadFailed(e);
        }
    }

    /**
     * 从网络Url中下载文件
     *
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public void downLoadFromUrl(String urlStr, String fileName, String savePath, OnDownloadListener listener) {
        InputStream inputStream = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            // 防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.setRequestProperty("Accept-Encoding", "identity");
            // 文件保存位置
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            File file = new File(saveDir + File.separator + fileName);
            if (file.exists()){
                if(listener != null){
                    listener.onDownloadSuccess(file);
                }
                return;
            }
            // 得到输入流
            if(conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();
                bis = new BufferedInputStream(inputStream);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                long total = conn.getContentLengthLong();
                System.out.println("total:" + total);
                byte[] buffer = new byte[16384];
                long sum = 0;
                int len = 0;
                while ((len = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                    sum += len;
                    //下载中更新进度条
                    if(listener!=null) {
                        int progress = (int) (sum * 1.0f / total * 100);
                        listener.onDownloading(progress);
                    }
                }
                conn.disconnect();
                if (listener != null){
                    listener.onDownloadSuccess(file);
                }
            }else{
                if(listener != null){
                    listener.onDownloadFailed(new HTTPException(conn.getResponseCode()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(listener != null){
                listener.onDownloadFailed(e);
            }
        }finally {
            try {
                if(bos != null){
                    bos.flush();
                    bos.close();
                }
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
                if(bis != null){
                    bis.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
