package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("CategoryController")
@Slf4j
@Api(tags = "分类相关接口")
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * @description:    新增分类
     * @author: liangguang
     * @date: 2024/8/13 0013 13:42
     * @param: [categoryDTO]
     * @return: com.sky.result.Result<java.lang.String>
     **/
    @PostMapping
    @ApiOperation(value = "新增分类")
    public Result<String> save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类:{}",categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * @description:    分页查询
     * @author: liangguang
     * @date: 2024/8/13 0013 14:16
     * @param: [categoryPageQueryDTO]
     * @return: com.sky.result.Result<com.sky.result.PageResult>
     **/
    @GetMapping("/page")
    @ApiOperation(value = "分类分页查询")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询:{}",categoryPageQueryDTO);
        PageResult pageResult =categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * @description:    删除分类
     * @author: liangguang
     * @date: 2024/8/14 0014 16:15
     * @param: [id]
     * @return: com.sky.result.Result<java.lang.String>
     **/
    @DeleteMapping
    @ApiOperation(value = "删除分类")
    public Result<String> deleteById(Long id){
        log.info("删除分类:{}",id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * @description:    修改分类
     * @author: liangguang
     * @date: 2024/8/14 0014 16:39
     * @param: [categoryDTO]
     * @return: com.sky.result.Result<java.lang.String>
     **/
    @PutMapping
    @ApiOperation("修改分类")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类:{]",categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * @description:    启用禁用分类
     * @author: liangguang
     * @date: 2024/8/14 0014 16:58
     * @param: [status, id]
     * @return: com.sky.result.Result<java.lang.String>
     **/
    @PostMapping ("/status/{status}")
    @ApiOperation(value = "启用禁用分类")
    public Result<String> startOrStop(@PathVariable("status") Integer status,Long id){
        categoryService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * @description:    根据类型查询分类
     * @author: liangguang
     * @date: 2024/8/13 0013 15:41
     * @param: [type]
     * @return: com.sky.result.Result<List<Category>>
     **/
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> list(Integer type){
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
