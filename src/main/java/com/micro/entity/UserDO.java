package com.micro.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * user entity
 *
 * @author cc zhao 2019/07/10
 */
@Data
public class UserDO implements Serializable {

    private Long uid;

    private String name;

    private String address;

    private LocalDateTime addTime;

    private Integer tinyintDb;
}
