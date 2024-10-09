package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
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

    /**
     * @description:    动态条件查询套餐
     * @author: liangguang
     * @date: 2024/8/26 0026 13:46
     * @param: [setmeal]
     * @return: java.util.List<com.sky.entity.Setmeal>
     **/
    List<Setmeal> list(Setmeal setmeal);

    /**
     * @description:    根据套餐id查询菜品选项
     * @author: liangguang
     * @date: 2024/8/26 0026 14:05
     * @param: [id]
     * @return: java.util.List<com.sky.vo.DishItemVO>
     **/
    List<DishItemVO> getDishItemBySetmealId(Long id);

    /**
     * @description:    套餐表插入数据
     * @author: liangguang
     * @date: 2024/8/26 0026 14:41
     * @param: [setmeal]
     * @return: void
     **/
    void insert(Setmeal setmeal);

    /**
     * @description:    分页查询套餐
     * @author: liangguang
     * @date: 2024/8/26 0026 14:49
     * @param: [setmealPageQueryDTO]
     * @return: com.github.pagehelper.Page<com.sky.vo.SetmealVO>
     **/
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    Setmeal getById(Long id);

    void deleteById(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    Integer countByMap(Map map);
}