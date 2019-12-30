package com.demo.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户查询条件
 *
 * @author cc zhao 2019/08/09
 */
@Data
public class UserQueryVO implements Serializable {

    private Long uid;

    private String name;

    private LocalDateTime addTime;

}
