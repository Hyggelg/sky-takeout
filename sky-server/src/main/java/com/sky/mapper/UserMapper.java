package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * @description:    根据id获取用户信息
     * @author: liangguang
     * @date: 2024/9/11 0011 15:33
     * @param: [id]
     * @return: com.sky.entity.User
     **/
    @Select("select * from User where id = #{id}")
    User getById(Long id);

    /**
     * @description:    根据openid查询用户
     * @author: liangguang
     * @date: 2024/8/23 0023 15:36
     * @param: [openid]
     * @return: com.sky.entity.User
     **/
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * @description:    插入数据
     * @author: liangguang
     * @date: 2024/8/23 0023 15:40
     * @param: [user]
     * @return: void
     **/
    void insert(User user);

    /**
     * @description:    根据动态条件统计用户数据
     * @author: liangguang
     * @date: 2024/9/22 0022 16:36
     * @param: [map]
     * @return: java.lang.Integer
     **/
    Integer countByMap(Map map);
}
