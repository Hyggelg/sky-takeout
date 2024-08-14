package com.sky.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealMapper {

    /**
     * @description:    根据分类id查询套餐数量
     * @author: liangguang
     * @date: 2024/8/14 0014 16:37
     * @param: [id]
     * @return: java.lang.Integer
     **/
    @Delete("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);
}
