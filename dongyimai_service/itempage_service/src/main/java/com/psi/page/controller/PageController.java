package com.psi.page.controller;

import com.psi.entity.Result;
import com.psi.entity.StatusCode;
import com.psi.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/page")
public class PageController {
    @Autowired
    private PageService pageService;

    /***
     * 生成静态页面
     * @param id
     * @return
     */
    @GetMapping("createHtml/{id}")
    public Result createHtml(@PathVariable("id") Long id) {
        try {
            pageService.createHtmlPage(id);
            return new Result(true, StatusCode.OK, "页面创建成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "页面创建失败");
        }

    }
}
