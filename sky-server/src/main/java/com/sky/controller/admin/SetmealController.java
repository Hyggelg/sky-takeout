package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * @description:    新增套餐
     * @author: liangguang
     * @date: 2024/8/26 0026 14:34
     * @param: [setmealDTO]
     * @return: com.sky.result.Result
     **/
    @PostMapping
    @ApiOperation(value = "新增套餐")
    @CacheEvict(cacheNames = "setmealCache",key = "#setmealDTO.categoryId")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * @description:    批量删除套餐
     * @author: liangguang
     * @date: 2024/8/26 0026 14:51
     * @param: [id]
     * @return: com.sky.result.Result
     **/
    @DeleteMapping
    @ApiOperation(value = "批量删除套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result delete(@RequestParam List<Long> ids){
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * @description:    根据id查询套餐
     * @author: liangguang
     * @date: 2024/8/26 0026 15:01
     * @param: [id]
     * @return: com.sky.result.Result<com.sky.vo.SetmealVO>
     **/
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id){
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }

    /**
     * @description:    修改套餐
     * @author: liangguang
     * @date: 2024/8/26 0026 15:07
     * @param: [setmealDTO]
     * @return: com.sky.result.Result
     **/
    @PutMapping
    @ApiOperation(value = "修改套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO){
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * @description:    分页查询套餐
     * @author: liangguang
     * @date: 2024/8/26 0026 14:43
     * @param: [setmealPageQueryDTO]
     * @return: com.sky.result.Result<com.sky.result.PageResult>
     **/
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * @description:    套餐起售停售
     * @author: liangguang
     * @date: 2024/8/26 0026 15:19
     * @param: [status, id]
     * @return: com.sky.result.Result
     **/
    @PostMapping("/status/{status}")
    @ApiOperation(value = "套餐起售停售")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result startOrStop(@PathVariable Integer status, Long id){
        setmealService.startOrStop(status, id);
        return Result.success();
    }
}
