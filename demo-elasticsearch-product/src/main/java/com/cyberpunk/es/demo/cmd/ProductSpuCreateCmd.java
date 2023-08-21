package com.cyberpunk.es.demo.cmd;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lujun
 * @date 2023/8/21 17:07
 */
@Getter
@Setter
public class ProductSpuCreateCmd {
    private String spuName;

    private List<ProductSkuCreateCmd> skus;
}
