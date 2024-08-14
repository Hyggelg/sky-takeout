package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper{

    /**
     * @description:    插入数据
     * @author: liangguang
     * @date: 2024/8/13 0013 13:34
     * @param: [category]
     * @return: void
     **/
    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user)" +
            " VALUES" +
            " (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Category category);

    /**
     * @description:   分页查询
     * @author: liangguang
     * @date: 2024/8/13 0013 14:21
     * @param: [categoryPageQueryDTO]
     * @return: Page<Category>
     **/
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    List<Category> list(Integer type);

    /**
     * @description:    根据id删除分类
     * @author: liangguang
     * @date: 2024/8/14 0014 16:48
     * @param: [id]
     * @return: void
     **/
    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);

    /**
     * @description:    根据id修改分类
     * @author: liangguang 
     * @date: 2024/8/14 0014 17:15
     * @param: [category]
     * @return: void
     **/
    void update(Category category);
}
