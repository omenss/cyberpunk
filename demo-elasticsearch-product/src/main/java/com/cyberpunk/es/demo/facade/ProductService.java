package com.cyberpunk.es.demo.facade;

import com.cyberpunk.es.demo.cmd.ProductSpuCreateCmd;

/**
 * @author lujun
 * @date 2023/8/21 17:11
 */
public interface ProductService {

    /**
     * 创建商品
     * @param cmd 商品信息
     */
    void createProduct(ProductSpuCreateCmd cmd);
}
