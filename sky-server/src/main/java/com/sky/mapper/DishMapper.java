package com.sky.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    /**
     * @description: 根据分类id查询菜品数量
     * @author: liangguang
     * @date: 2024/8/14 0014 16:29
     * @param: [id]
     * @return: java.lang.Integer
     **/
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);
}