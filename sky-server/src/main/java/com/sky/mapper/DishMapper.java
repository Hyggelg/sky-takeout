package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

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

    /**
     * @description:    插入菜品数据
     * @author: liangguang
     * @date: 2024/8/16 0016 11:06
     * @param: [dish]
     * @return: void
     **/
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * @description:    根据主键删除菜品数据
     * @author: liangguang
     * @date: 2024/8/16 0016 18:11
     * @param: [id]
     * @return: void
     **/
//    @Delete("delete from dish where id = #{id}")
//    void deleteById(Long id);

    /**
     * @description:    根据菜品id集合批量删除菜品数据
     * @author: liangguang
     * @date: 2024/8/18 0018 10:22
     * @param: [ids]
     * @return: void
     **/
    void deleteByIds(List<Long> ids);

    /**
     * @description:    根据id动态修改菜品
     * @author: liangguang
     * @date: 2024/8/18 0018 10:54
     * @param: [dish]
     * @return: void
     **/
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    List<Dish> getBySetmealId(Long id);

    /**
     * @description:    动态条件查询菜品
     * @author: liangguang
     * @date: 2024/8/26 0026 11:41
     * @param: [dish]
     * @return: java.util.List<com.sky.entity.Dish>
     **/
    List<Dish> list(Dish dish);

    /**
     * @description:    查询菜品总览
     * @author: liangguang
     * @date: 2024/9/22 0022 22:53
     * @param: [map]
     * @return: java.lang.Integer
     **/
    Integer countByMap(Map map);
}