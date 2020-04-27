package xin.spring.bless.javafx.common.pojo;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户-文件夹文件
 */
@Data
@Entity
@Table(name = "folder_file")
@org.hibernate.annotations.Table(appliesTo = "folder_file",comment="文件夹文件")
public class FolderFile implements Serializable {

    /**
     * 编号
     */
    @Id
    @Column(name="folder_id", columnDefinition="bigint(64) COMMENT '编号'")
    @GenericGenerator(name="sysnative",strategy="native")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long folderId;

    /**
     *文件夹
     */
    @Column(name="folder_name", columnDefinition="varchar(255) COMMENT '文件夹'")
    private String folderName;

    /**
     * 父目录
     */
    private String folderParentName;

    /**
     * 父目录集
     */
    private String folderParents;

    /**
     * 文件扩展名
     */
    private String fileType;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 级别
     */
    private Integer lavel;

    /**
     * 文件类型
     *      FOLDER, FILE
     */
    private String type;

    /**
     * 创建日期
     */
    private Date createTime;

    private User user;

}
