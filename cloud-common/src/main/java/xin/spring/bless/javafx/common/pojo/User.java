package xin.spring.bless.javafx.common.pojo;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 功能描述: 用户对象
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
@Data
@Entity
@Table(name = "cloud_user")
@org.hibernate.annotations.Table(appliesTo = "cloud_user",comment="用户表")
public class User implements Serializable {

    /**
     * 编号
     */
    @Id
    @Column(name="user_id", columnDefinition="bigint(64) COMMENT '编号'")
    @GenericGenerator(name="sysnative",strategy="native")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long userId;

    /**
     * 账号
     */
    @Column(name="login_name", columnDefinition="varchar(32) COMMENT '账号'")
    private String loginName;

    /**
     * 昵称
     */
    @Column(name="user_name", columnDefinition="varchar(12) COMMENT '昵称'")
    private String userName;

    /**
     * 手机号
     */
    @Column(name="phonenumber", columnDefinition="varchar(11) COMMENT '手机号'")
    private String phonenumber;

    /**
     * 邮箱
     */
    @Column(name="email", columnDefinition="varchar(32) COMMENT '邮箱'")
    private String email;

    /**
     * 密码
     */
    @Column(name="password", columnDefinition="varchar(32) COMMENT '密码'")
    private String password;

    /**
     *最大可存储大小
     */
    @Column(name="max_disk_size", columnDefinition="varchar(255) COMMENT '容量'")
    private Long maxDiskSize;

    /**
     *当前存储大小
     */
    @Column(name="current_disk_size", columnDefinition="varchar(255) COMMENT '已用容量'")
    private Long currentDiskSize;

    /**
     * 是否禁用
     */
    @Column(name="enables", columnDefinition="tinyint(1) COMMENT '是否禁用'")
    private boolean enables;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", loginName='" + loginName + '\'' +
                ", userName='" + userName + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", maxDiskSize=" + maxDiskSize +
                ", currentDiskSize=" + currentDiskSize +
                ", enables=" + enables +
                '}';
    }
}
