package xin.spring.bless.javafx.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xin.spring.bless.javafx.common.pojo.CloudFile;

/**
 * 功能描述:
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
@Repository
public interface CloudFileRepository extends JpaRepository<CloudFile, Long> {

}
