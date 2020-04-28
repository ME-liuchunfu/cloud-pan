package xin.spring.bless.javafx.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xin.spring.bless.javafx.common.pojo.FolderFile;

import java.util.ArrayList;

/**
 * 功能描述: 文件&文件夹
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
@Repository
public interface FolderFileRepository extends JpaRepository<FolderFile, Long> {

    /**
     * 查找文件夹或文件
     * @param folderPid 文件夹或文件父目录编号
     * @param userId 用户id
     * @return
     */
    public ArrayList<FolderFile> findByFolderPidAndUserId(Long folderPid, Long userId);

    /**
     * 查找相关文件夹
     * @param folderName 文件夹名
     * @param folderPid 父级目录
     * @param userId 用户编号
     * @return
     */
    public FolderFile findByFolderNameAndFolderPidAndUserId(String folderName, Long folderPid, Long userId);


}
