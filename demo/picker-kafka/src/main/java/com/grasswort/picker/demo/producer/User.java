package com.grasswort.picker.demo.producer;

import lombok.Data;
import org.msgpack.annotation.Message;

/**
 * @author xuliangliang
 * @Classname User
 * @Description TODO
 * @Date 2019/10/3 18:57
 * @blame Java Team
 */
@Data
@Message
public class User {

    private int id;

    private String name;

}
