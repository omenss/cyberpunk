package com.cyberpunk.es.demo.cmd;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lujun
 * @date 2023/8/21 17:07
 */
@Getter
@Setter
public class ProductSkuCreateCmd {
    private String skuName;

    private String skuDesc;

    private BigDecimal skuPrice;

    private List<ProductFeatureCreateCmd> features;
}
