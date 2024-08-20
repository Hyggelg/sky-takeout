package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * @description:    新增菜品和对应口味
     * @author: liangguang
     * @date: 2024/8/16 0016 10:53
     * @param: [dishDTO]
     * @return: void
     **/
    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * @description:    菜品分页查询
     * @author: liangguang
     * @date: 2024/8/16 0016 17:03
     * @param: [dishPageQueryDTO]
     * @return: com.sky.result.PageResult
     **/
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * @description:    菜品批量删除
     * @author: liangguang
     * @date: 2024/8/16 0016 17:48
     * @param: [ids]
     * @return: void
     **/
    void deleteBatch(List<Long> ids);

    /**
     * @description:    根据id查询菜品和对应的口味数据
     * @author: liangguang
     * @date: 2024/8/18 0018 10:39
     * @param: [id]
     * @return: com.sky.vo.DishVO
     **/
    DishVO getByIdWithFlavor(Long id);

    /**
     * @description:    根据id修改菜品基本信息和对应口味信息
     * @author: liangguang
     * @date: 2024/8/18 0018 10:50
     * @param: [dishDTO]
     * @return: void
     **/
    void updateWithFlavor(DishDTO dishDTO);
}
