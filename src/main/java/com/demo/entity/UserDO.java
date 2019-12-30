package com.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;


/**
 * user entity
 *
 * @author cc zhao 2019/07/10
 */
@Data
public class UserDO {

    private Long uid;

    private String name;

    private String address;

    private LocalDateTime addTime;

    private Integer tinyintDb;
}
