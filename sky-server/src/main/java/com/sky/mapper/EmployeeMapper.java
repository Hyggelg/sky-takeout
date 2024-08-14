package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 插入员工数据
     * @param employee
     */
    @Insert("insert into employee (name,username,password,phone,sex,id_number,create_time,update_time,create_user,update_user,status) " +
            "values " +
            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser},#{status})")
    @AutoFill(OperationType.INSERT)
    void insert(Employee employee);


    /**
     * @description:    分页查询
     * @author: liangguang
     * @date: 2024/8/6 0006 16:44
     * @param: [employeePageQueryDTO]
     * @return: com.github.pagehelper.Page<com.sky.entity.Employee>
     **/
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * @description: 根据主键动态修改属性
     * @author: liangguang
     * @date: 2024/8/11 0011 10:58
     * @param: [employee]
     * @return: void
     **/
    @AutoFill(OperationType.UPDATE)
    void update(Employee employee);

    /**
     * @description:    根据id查询员工信息
     * @author: liangguang
     * @date: 2024/8/11 0011 10:58
     * @param: [id]
     * @return: com.sky.entity.Employee
     **/
    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);
}
