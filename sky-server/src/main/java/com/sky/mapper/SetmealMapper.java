package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {

    /**
     * @description:    根据分类id查询套餐数量
     * @author: liangguang
     * @date: 2024/8/14 0014 16:37
     * @param: [id]
     * @return: java.lang.Integer
     **/
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

}
