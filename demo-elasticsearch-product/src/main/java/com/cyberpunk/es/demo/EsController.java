package com.cyberpunk.es.demo;

import com.cyberpunk.es.demo.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lujun
 * @date 2023/8/21 15:34
 */
@RestController
@RequestMapping("/es")
public class EsController {
    @Autowired
    EsService esService;

    @PostMapping("/createIndex")
    public String createIndex() {
        esService.createIndex();
        return "success";
    }
}
