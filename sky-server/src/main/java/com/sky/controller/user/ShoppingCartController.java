package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "C端-购物车相关接口")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * @description:    添加购物车
     * @author: liangguang
     * @date: 2024/9/4 0004 21:58
     * @param: [shoppingCart]
     * @return: com.sky.result.Result
     **/
    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result add(@RequestBody ShoppingCartDTO shoppingCart){
        log.info("添加购物车商品信息:{}",shoppingCart);
        shoppingCartService.addShoppingCart(shoppingCart);
        return Result.success(shoppingCart);
    }

    /**
     * @description:    查看购物车
     * @author: liangguang
     * @date: 2024/9/4 0004 22:56
     * @param: []
     * @return: com.sky.result.Result<java.util.List<com.sky.entity.ShoppingCart>>
     **/
    @GetMapping("/list")
    @ApiOperation("查看购物车")
    public Result<List<ShoppingCart>> list(){
        List<ShoppingCart> list = shoppingCartService.showShoppingCart();
        return Result.success(list);
    }

    /**
     * @description:    清空购物车
     * @author: liangguang
     * @date: 2024/9/4 0004 23:06
     * @param: []
     * @return: com.sky.result.Result
     **/
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result clean(){
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }

    /**
     * @description:    删除购物车中一个商品
     * @author: liangguang
     * @date: 2024/9/5 0005 13:37
     * @param: [shoppingCartDTO]
     * @return: com.sky.result.Result
     **/
    @PostMapping("/sub")
    @ApiOperation("删除购物车中一个商品")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("删除购物车中一个商品:{}",shoppingCartDTO);
        shoppingCartService.subShoppingCart(shoppingCartDTO);
        return Result.success();
    }
}


