package com.cyberpunk.es.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyberpunk.es.demo.entity.Feature;
import com.cyberpunk.es.demo.entity.Sku;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lujun
 * @date 2023/8/21 16:52
 */
@Mapper
public interface FeatureMapper extends BaseMapper<Feature> {
}

