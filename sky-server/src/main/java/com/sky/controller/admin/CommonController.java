package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOSSUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @description:    通用接口
 * @author: liangguang
 * @date: 2024/8/14 0014 18:24
 * @param:
 * @return:
 **/
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private AliOSSUtils aliOSSUtils;

    /**
     * @description:    文件上传
     * @author: liangguang
     * @date: 2024/8/14 0014 18:27
     * @param: [file]
     * @return: com.sky.result.Result<java.lang.String>
     **/
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        log.info("文件上传: {}", file.getOriginalFilename());
        //调用阿里云OSS工具类进行文件上传
        String url = aliOSSUtils.upload(file);
        log.info("文件上传成功,文件访问的url: {}",url);
        return Result.success(url);
    }
}
