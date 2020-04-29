package xin.spring.bless.javafx.framework.http;

import java.io.File;

/**
 * 下载进度监听
 */
public interface OnDownloadListener {

    /**
     * 下载成功之后的文件
     */
    void onDownloadSuccess(File file);

    /**
     * 下载进度
     */
    void onDownloading(int progress);

    /**
     * 下载异常信息
     */

    void onDownloadFailed(Exception e);

}
