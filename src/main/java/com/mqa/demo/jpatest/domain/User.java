package com.mqa.demo.jpatest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author mengqa
 * @create 2018-05-04 14:48
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    private String name;

    private String email;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 生成策略
    private Long id;

    @Override
    public String toString() {
        return String.format("User[id=%d, name='%s', email='%s']", id, name, email);
    }
}
