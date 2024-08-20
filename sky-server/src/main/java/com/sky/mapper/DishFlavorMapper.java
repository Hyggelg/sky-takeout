package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * @description:    批量插入口味数据
     * @author: liangguang
     * @date: 2024/8/16 0016 11:10
     * @param: [flavors]
     * @return: void
     **/
    void insertBatch(List<DishFlavor> flavors);

    /**
     * @description:    根据菜品id删除对应口味数据
     * @author: liangguang
     * @date: 2024/8/16 0016 18:14
     * @param: [dishId]
     * @return: void
     **/
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    /**
     * @description:    根据菜品id集合批量删除关联的口味数据
     * @author: liangguang
     * @date: 2024/8/18 0018 10:27
     * @param: [dishIds]
     * @return: void
     **/
    void deleteByDishIds(List<Long> dishIds);

    /**
     * @description:    根据菜品id查询对应口味数据
     * @author: liangguang
     * @date: 2024/8/18 0018 10:43
     * @param: [dishId]
     * @return: java.util.List<com.sky.entity.DishFlavor>
     **/
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
