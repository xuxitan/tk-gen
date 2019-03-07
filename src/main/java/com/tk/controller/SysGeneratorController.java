/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.tk.controller;

import com.tk.entity.TargetDirectory;
import com.tk.utils.PageUtils;
import com.tk.service.SysGeneratorService;
import com.tk.utils.Query;
import com.tk.utils.R;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 代码生成器
 * 
 * @author Mark sunlightcs@gmail.com
 */
@Controller
@RequestMapping("/sys/generator")
public class SysGeneratorController {
	@Autowired
	private SysGeneratorService sysGeneratorService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params){

        PageUtils pageUtil = sysGeneratorService.queryList(new Query(params));
        return R.ok().put("page", pageUtil);
    }


	/**
	 * 生成代码
	 */
	@RequestMapping("/code")
    @ResponseBody
	public String code(String tables, TargetDirectory directory) throws IOException{
	    try {
            sysGeneratorService.generatorCode(tables.split(","),directory);
        }catch (Exception e){
           e.printStackTrace();
            return "失败";
        }
        return "成功";
    }
}
