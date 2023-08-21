package com.cyberpunk.es.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyberpunk.es.demo.entity.Feature;
import com.cyberpunk.es.demo.entity.Spu;
import com.cyberpunk.es.demo.mapper.FeatureMapper;
import com.cyberpunk.es.demo.mapper.SpuMapper;
import com.cyberpunk.es.demo.service.IFeatureService;
import com.cyberpunk.es.demo.service.ISpuService;
import org.springframework.stereotype.Service;

/**
 * @author lujun
 * @date 2023/8/21 16:52
 */
@Service
public class FeatureServiceImpl extends ServiceImpl<FeatureMapper, Feature> implements IFeatureService {
}
