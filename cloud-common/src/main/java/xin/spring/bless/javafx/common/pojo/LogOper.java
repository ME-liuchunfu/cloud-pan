package xin.spring.bless.javafx.common.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 功能描述:
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
@Entity
@Table(name = "cloud_log_oper")
@org.hibernate.annotations.Table(appliesTo = "cloud_log_oper",comment="表注释")
public class LogOper implements Serializable {

    @Id
    @Column(name="log_id", columnDefinition="bigint(64) COMMENT '编号'")
    @GenericGenerator(name="sysnative",strategy="native")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="contenxt", columnDefinition="varchar(255) COMMENT '内容'")
    private String contenxt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenxt() {
        return contenxt;
    }

    public void setContenxt(String contenxt) {
        this.contenxt = contenxt;
    }

    @Override
    public String toString() {
        return "LogOper{" +
                "id=" + id +
                ", contenxt='" + contenxt + '\'' +
                '}';
    }
}
