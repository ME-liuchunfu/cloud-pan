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
     * 父级编号
     */
    @Column(name="folder_pid", columnDefinition="bigint(64) COMMENT '父级编号'")
    private Long folderPid;

    /**
     *文件夹
     */
    @Column(name="folder_name", columnDefinition="varchar(255) COMMENT '文件夹'")
    private String folderName;

    /**
     * 父目录
     */
    @Column(name="folder_pname", columnDefinition="varchar(255) COMMENT '父目录'")
    private String folderPname;

    /**
     * 父目录集
     */
    @Column(name="folder_parents", columnDefinition="varchar(255) COMMENT '父目录集'")
    private String folderParents;

    /**
     * 文件扩展名
     */
    @Column(name="folder_type", columnDefinition="varchar(12) COMMENT '文件扩展名'")
    private String fileType;

    /**
     * 用户
     */
    @Column(name="user_id", columnDefinition="bigint(64) COMMENT '用户'")
    private Long userId;

    /**
     * 级别
     */
    @Column(name="lavel", columnDefinition="int(3) COMMENT '级别'")
    private Integer lavel;

    /**
     * 文件类型
     *      FOLDER, FILE
     */
    @Column(name="type", columnDefinition="varchar(255) COMMENT '文件类型'")
    private String type;

    /**
     * 创建时间
     */
    @Column(name="create_time", columnDefinition="varchar(255) COMMENT '创建时间'")
    private Date createTime;

    @Transient
    private User user;

}
