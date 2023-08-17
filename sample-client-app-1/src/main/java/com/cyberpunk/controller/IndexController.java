package com.cyberpunk.controller;

import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lujun
 * @date 2023/8/16 13:53
 */
@Controller
@Slf4j
public class IndexController {
    @RequestMapping("/index")
    public String indexPage(HttpServletRequest request, ModelMap modelMap) {
        //获取cas给我们传递回来的对象，这个东西放到了session中
        //session的 key是 _const_cas_assertion_
        Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
        String loginName = assertion.getPrincipal().getName();
        modelMap.addAttribute("username", loginName);
        log.info("username:{}", loginName);
        return "index";
    }
}
