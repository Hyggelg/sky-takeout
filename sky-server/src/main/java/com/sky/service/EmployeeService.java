package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void save(EmployeeDTO employeeDTO);


    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void startOrStop(Integer status, Long id);

    /**
     * @description:    根据id查询员工
     * @author: liangguang
     * @date: 2024/8/11 0011 10:52
     * @param: [id]
     * @return: com.sky.entity.Employee
     **/
    Employee getById(Long id);

    /**
     * @description:    编辑员工信息
     * @author: liangguang
     * @date: 2024/8/11 0011 11:10
     * @param: [employeeDTO]
     * @return: void
     **/
    void update(EmployeeDTO employeeDTO);
}
