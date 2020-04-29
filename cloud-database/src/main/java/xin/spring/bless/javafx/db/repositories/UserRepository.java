package xin.spring.bless.javafx.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * 更改剩余存储空间
     * @param currentDiskSize
     * @param userId
     * @return
     */
    @Transactional
    @Modifying
    @Query("UPDATE User SET currentDiskSize = ?1 WHERE userId = ?2")
    int updateCurrentDiskSizeByUserId(long currentDiskSize, long userId);

}
