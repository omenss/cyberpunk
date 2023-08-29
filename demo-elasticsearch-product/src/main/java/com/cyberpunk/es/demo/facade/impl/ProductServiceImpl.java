package com.cyberpunk.es.demo.facade.impl;

import cn.hutool.core.collection.CollUtil;
import com.cyberpunk.es.demo.cmd.ProductFeatureCreateCmd;
import com.cyberpunk.es.demo.cmd.ProductSkuCreateCmd;
import com.cyberpunk.es.demo.cmd.ProductSpuCreateCmd;
import com.cyberpunk.es.demo.entity.Feature;
import com.cyberpunk.es.demo.entity.Sku;
import com.cyberpunk.es.demo.entity.Spu;
import com.cyberpunk.es.demo.facade.ProductService;
import com.cyberpunk.es.demo.service.IFeatureService;
import com.cyberpunk.es.demo.service.ISkuService;
import com.cyberpunk.es.demo.service.ISpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author lujun
 * @date 2023/8/21 17:11
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ISkuService skuService;
    @Autowired
    ISpuService spuService;
    @Autowired
    IFeatureService featureService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createProduct(ProductSpuCreateCmd cmd) {
        Spu spu = new Spu();
        spu.setSpuName(cmd.getSpuName());
        spu.setSpuNo(UUID.randomUUID().toString());
        spuService.save(spu);
        List<ProductSkuCreateCmd> skus =
                cmd.getSkus();
        if (CollUtil.isNotEmpty(skus)) {
            for (ProductSkuCreateCmd skuCreateCmd : skus) {
                Sku sku = new Sku();
                sku.setSkuName(skuCreateCmd.getSkuName());
                sku.setSkuDesc(skuCreateCmd.getSkuDesc());
                sku.setSkuPrice(skuCreateCmd.getSkuPrice());
                sku.setSkuNo(UUID.randomUUID().toString());
                sku.setSpuId(spu.getId());
                skuService.save(sku);
                List<ProductFeatureCreateCmd> features = skuCreateCmd.getFeatures();
                if (CollUtil.isNotEmpty(features)) {
                    List<Feature> featureList = new ArrayList<>(features.size());
                    for (ProductFeatureCreateCmd featureCreateCmd : features) {
                        Feature feature = new Feature();
                        feature.setFeatureName(featureCreateCmd.getFeatureName());
                        feature.setFeatureValue(featureCreateCmd.getFeatureValue());
                        feature.setSkuId(sku.getId());
                        featureList.add(feature);
                    }
                    featureService.saveBatch(featureList);
                }
            }
        }
    }
}
