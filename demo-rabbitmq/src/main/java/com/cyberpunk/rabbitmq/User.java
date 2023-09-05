package com.cyberpunk.rabbitmq;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author lujun
 * @date 2023/9/5 11:11
 */
@Data
@ToString
public class User implements Serializable {

    private String name;

    private String pass;


}
