package com.mqa.demo.jpatest.dao;

import com.mqa.demo.jpatest.domain.User;
import org.springframework.data.repository.CrudRepository;

/**
 * @author mengqa
 * @create 2018-05-04 14:50
 **/
public interface UserDao extends CrudRepository<User, Long> {


}
