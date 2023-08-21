package com.cyberpunk.es.demo.controller;

import com.cyberpunk.es.demo.cmd.ProductSpuCreateCmd;
import com.cyberpunk.es.demo.facade.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lujun
 * @date 2023/8/21 17:06
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/create")
    public String createProduct(@RequestBody ProductSpuCreateCmd cmd) {
        productService.createProduct(cmd);
        return "success";
    }
}



