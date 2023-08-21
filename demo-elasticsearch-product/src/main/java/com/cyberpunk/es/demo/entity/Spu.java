package com.cyberpunk.es.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lujun
 * @date 2023/8/21 16:49
 */
@TableName(value = "spu")
@Getter
@Setter
public class Spu {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String spuName;

    private String spuNo;
}
