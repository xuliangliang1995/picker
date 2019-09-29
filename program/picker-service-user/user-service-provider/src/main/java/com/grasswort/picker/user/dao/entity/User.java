package com.grasswort.picker.user.dao.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_user")
@Data
public class User {
    @Id
    private Long id;

    private String username;

    private String password;

    private String name;

    /**
     * 只能存储11位的手机号
     */
    private String phone;

    private String email;

    /**
     * 性别 0、未知 1、男 2、女
     */
    private Byte sex;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

}