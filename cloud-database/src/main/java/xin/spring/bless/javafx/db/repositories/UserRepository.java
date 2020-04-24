package xin.spring.bless.javafx.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xin.spring.bless.javafx.common.pojo.User;

/**
 * 功能描述:
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 查找用户
     * @param loginName
     * @return
     */
    public User findByLoginName(String loginName);

}
