package com.cyberpunk.es.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author lujun
 * @date 2023/8/21 16:50
 */
@TableName(value = "feature")
public class Feature {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String featureName;

    private String featureValue;


    private Long skuId;
}
