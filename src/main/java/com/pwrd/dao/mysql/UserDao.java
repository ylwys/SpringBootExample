package com.pwrd.dao.mysql;

import com.pwrd.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * User: ylwys
 * Date: 2016/12/17
 * Time: 15:29
 */
@Transactional
public interface UserDao extends CrudRepository<User, Long> {

    List<User> getByAccount(String account);

    @Query("SELECT COUNT(1) FROM User WHERE account = ?1")
    int defineSql(String account);

    @Query("FROM User WHERE account = ?1")
    List<User> defineSql1(String account, Pageable pageable);

    @Modifying
    @Query("DELETE FROM User WHERE account = ?1")
    void defineSql2(String account);

    @Query("FROM User")
    List<User> queryUserPage(Pageable pageable);

    @Query("SELECT COUNT(*) FROM User")
    int getAllUserCount();
}

