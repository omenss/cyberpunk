package com.cyberpunk.es.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author lujun
 * @date 2023/8/21 16:47
 */
@Getter
@Setter
@TableName("sku")
public class Sku {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String skuName;

    private String skuDesc;

    private BigDecimal skuPrice;

    private Long spuId;
}
