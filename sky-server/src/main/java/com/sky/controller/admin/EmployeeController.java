package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * @description:    登录
     * @author: liangguang
     * @date: 2024/8/11 0011 10:44
     * @param: [employeeLoginDTO]
     * @return: com.sky.result.Result<com.sky.vo.EmployeeLoginVO>
     **/
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * @description:    登出
     * @author: liangguang
     * @date: 2024/8/11 0011 10:44
     * @param: []
     * @return: com.sky.result.Result<java.lang.String>
     **/
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

   /**
    * @description: 新增员工
    * @author: liangguang
    * @date: 2024/8/11 0011 10:43
    * @param: [employeeDTO]
    * @return: com.sky.result.Result<java.lang.String>
    **/
    @PostMapping
    @ApiOperation("新增员工")
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工:{}",employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * @description:    员工分页查询
     * @author: liangguang
     * @date: 2024/8/11 0011 10:39
     * @param: [employeePageQueryDTO]
     * @return: com.sky.result.Result<com.sky.result.PageResult>
     **/
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询，参数为：{}",employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * @description:    启用禁用账号
     * @author: liangguang
     * @date: 2024/8/11 0011 10:39
     * @param: [status, id]
     * @return: com.sky.result.Result
     **/
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result staartOrStop(@PathVariable Integer status, Long id) {
        log.info("启用禁用员工账号：{}，{}", status, id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * @description:    根据id查询员工
     * @author: liangguang
     * @date: 2024/8/11 0011 10:47
     * @param: [status]
     * @return: com.sky.result.Result<com.sky.entity.Employee>
     **/
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询员工")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工:{}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * @description:    编辑员工信息
     * @author: liangguang
     * @date: 2024/8/11 0011 11:08
     * @param: [employeeDTO]
     * @return: com.sky.result.Result
     **/
    @PutMapping
    @ApiOperation(value = "编辑员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑员工信息:{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

}
