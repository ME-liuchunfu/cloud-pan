package xin.spring.bless.javafx.common.pojo;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 功能描述: 云文件
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
@Data
@Entity
@Table(name = "cloud_file")
@org.hibernate.annotations.Table(appliesTo = "cloud_file",comment="云文件")
public class CloudFile implements Serializable {

    /**
     * 编号
     */
    @Id
    @Column(name="file_id", columnDefinition="bigint(64) COMMENT '编号'")
    @GenericGenerator(name="sysnative",strategy="native")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long fileId;

    /**
     *存储路径
     */
    @Column(name="file_path", columnDefinition="varchar(255) COMMENT '存储路径'")
    private String filePath;

    /**
     *用户
     */
    @Column(name="user_id", columnDefinition="bigint(64) COMMENT '用户'")
    private Long userId;

    /**
     * 创建时间
     */
    @Column(name="create_time", columnDefinition="datetime COMMENT '创建时间'")
    private Date createTime;

    /**
     *文件名称
     */
    @Column(name="file_name", columnDefinition="varchar(255) COMMENT '文件名称'")
    private String fileName;

    /**
     *文件大小
     */
    @Column(name="file_size", columnDefinition="bigint(64) COMMENT '文件大小'")
    private Long fileSize;

    /**
     * 文件类型
     */
    @Column(name="file_type", columnDefinition="varchar(12) COMMENT '文件类型'")
    private String fileType;

    @Transient
    private User user;

    @Override
    public String toString() {
        return "CloudFile{" +
                "fileId=" + fileId +
                ", filePath='" + filePath + '\'' +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", fileType='" + fileType + '\'' +
                ", user=" + user +
                '}';
    }
}
