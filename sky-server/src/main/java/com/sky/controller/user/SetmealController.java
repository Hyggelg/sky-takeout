package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userSetmealController")
@Api(tags = "C端-套餐浏览接口")
@RequestMapping("/user/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * @description:    条件查询   根据分类id查询套餐
     * @author: liangguang
     * @date: 2024/8/26 0026 11:48
     * @param: [categoryId]
     * @return: com.sky.result.Result<java.util.List<com.sky.entity.Setmeal>>
     **/
    @GetMapping("/list")
    @ApiOperation("根据分类id查询套餐")
    @Cacheable(cacheNames = "setmealCache",key = "#categoryId")//key:setmealCache::100
    public Result<List<Setmeal>> list(Long categoryId){
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);

        List<Setmeal> list = setmealService.list(setmeal);
        return Result.success(list);
    }

    /**
     * @description:    根据套餐id查询包含的菜品列表
     * @author: liangguang
     * @date: 2024/8/26 0026 14:07
     * @param: [id]
     * @return: com.sky.result.Result<java.util.List<com.sky.vo.DishItemVO>>
     **/
    @GetMapping("/dish/{id}")
    @ApiOperation(value = "根据套餐id查询包含的菜品列表")
    public Result<List<DishItemVO>> dishList(@PathVariable("id") Long id){
        List<DishItemVO> list = setmealService.getDishItemById(id);
        return Result.success(list);
    }
}
