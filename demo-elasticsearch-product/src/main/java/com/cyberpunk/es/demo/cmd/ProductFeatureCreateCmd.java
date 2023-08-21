package com.cyberpunk.es.demo.cmd;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lujun
 * @date 2023/8/21 17:09
 */
@Getter
@Setter
public class ProductFeatureCreateCmd {

    private String featureName;

    private String featureValue;
}
