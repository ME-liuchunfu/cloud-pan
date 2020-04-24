package xin.spring.bless.javafx.db.config.database;

import java.io.Serializable;

/**
 * 功能描述:
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
public class Hc3p0 implements Serializable {

    private String minSize;

    private String maxSize;

    public String getMinSize() {
        return minSize;
    }

    public void setMinSize(String minSize) {
        this.minSize = minSize;
    }

    public String getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(String maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public String toString() {
        return "Hc3p0{" +
                "minSize='" + minSize + '\'' +
                ", maxSize='" + maxSize + '\'' +
                '}';
    }
}