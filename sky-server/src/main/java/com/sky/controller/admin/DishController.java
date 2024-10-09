package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/*菜品管理*/
@RestController
@Slf4j
@Api(tags = "菜品相关接口")
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * @description:    新增菜品
     * @author: liangguang
     * @date: 2024/8/16 0016 10:51
     * @param: [dishDTO]
     * @return: com.sky.result.Result
     **/
    @PostMapping
    @ApiOperation(value = "新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品:{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);

        //清理缓存数据
        String key = "dish_" + dishDTO.getCategoryId();
        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * @description:    菜品分页查询
     * @author: liangguang
     * @date: 2024/8/16 0016 17:01
     * @param: [dishPageQueryDTO]
     * @return: com.sky.result.Result<com.sky.result.PageResult>
     **/
    @GetMapping("/page")
    @ApiOperation(value = "菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询:{}",dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * @description:    菜品批量删除
     * @author: liangguang
     * @date: 2024/8/16 0016 17:46
     * @param: [ids]
     * @return: com.sky.result.Result
     **/
    @DeleteMapping
    @ApiOperation("菜品批量删除")
    public Result delete(@RequestParam List<Long> ids){
        log.info("菜品批量删除:{}",ids);
        dishService.deleteBatch(ids);

        //将所有菜品缓存数据清理，所有dish_开头的key
        //Set keys = redisTemplate.keys("dish_");
        //redisTemplate.delete(keys);
        clearCache("dish_*");
        return Result.success();
    }

    /**
     * @description:    根据id查询菜品
     * @author: liangguang
     * @date: 2024/8/18 0018 10:37
     * @param: []
     * @return: com.sky.result.Result<com.sky.vo.DishVO>
     **/
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品:{}",id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * @description:    根据分类id查询菜品
     * @author: liangguang
     * @date: 2024/9/4 0004 16:33
     * @param: [categoryId]
     * @return: com.sky.result.Result<java.util.List<com.sky.entity.Dish>>
     **/
    @GetMapping("list")
    @ApiOperation(value = "根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

     /**
     * @description:    修改菜品
     * @author: liangguang
     * @date: 2024/8/18 0018 10:49
     * @param: [dishDTO]
     * @return: com.sky.result.Result
     **/
    @PutMapping
    @ApiOperation(value = "修改菜品")
    public Result updete(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品:{}",dishDTO);
        dishService.updateWithFlavor(dishDTO);

        //清理所有缓存数据
        //Set keys = redisTemplate.keys("dish_*");
        //redisTemplate.delete(keys);
        clearCache("dish_*");
        return Result.success();
    }

    /**
     * @description:    菜品起售停售
     * @author: liangguang
     * @date: 2024/9/4 0004 15:12
     * @param: [status, id]
     * @return: com.sky.result.Result<java.lang.String>
     **/
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        dishService.startOrStop(status, id);

        //将所有的菜品缓存数据清理掉，所有以dish_开头的key
        //Set keys = redisTemplate.keys("dish_*");
        //redisTemplate.delete(keys);
        clearCache("dish_*");
        return Result.success();
    }

    /**
     * @description:    清理缓存数据
     * @author: liangguang
     * @date: 2024/9/4 0004 15:20
     * @param: [pattern]
     * @return: void
     **/
    private void clearCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

}
