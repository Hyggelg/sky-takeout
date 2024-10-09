package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:    套餐业务实现
 * @author: liangguang
 * @date: 2024/8/26 0026 11:53
 * @param:
 * @return:
 **/
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;

    /**
     * @description:    条件查询
     * @author: liangguang
     * @date: 2024/8/26 0026 13:43
     * @param: [setmeal]
     * @return: java.util.List<com.sky.entity.Setmeal>
     **/
    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * @description:    根据id查询菜品选项
     * @author: liangguang
     * @date: 2024/8/26 0026 13:53
     * @param: [id]
     * @return: java.util.List<com.sky.vo.DishItemVO>
     **/
    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }

    /**
     * @description:    新增套餐
     * @author: liangguang
     * @date: 2024/8/26 0026 14:39
     * @param: [setmealDTO]
     * @return: void
     **/
    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //向套餐表插入数据
        setmealMapper.insert(setmeal);
        //获取生成的套餐id
        Long id = setmeal.getId();
        //设置id
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(id));

        //保存套餐和菜品的关联关系
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * @description:    分页查询套餐
     * @author: liangguang
     * @date: 2024/8/26 0026 14:45
     * @param: [setmealPageQueryDTO]
     * @return: com.sky.result.PageResult
     **/
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());

        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * @description:    批量删除套餐
     * @author: liangguang
     * @date: 2024/8/26 0026 14:53
     * @param: [ids]
     * @return: void
     **/
    @Override
    public void deleteBatch(List<Long> ids) {
        //起售中的套餐不能删除
        ids.forEach(id ->{
            Setmeal setmeal = setmealMapper.getById(id);
            if (StatusConstant.ENABLE == setmeal.getStatus()){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        ids.forEach(id ->{
            //删除套餐表中的数据
            setmealMapper.deleteById(id);
            //删除套餐餐品关系表中的数据
            setmealDishMapper.deleteBySetmaleId(id);
        });
    }

    /**
     * @description:    根据id查询套餐
     * @author: liangguang
     * @date: 2024/8/26 0026 15:02
     * @param: [id]
     * @return: com.sky.vo.SetmealVO
     **/
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        SetmealVO setmealVO = new SetmealVO();

        //查询套餐基本信息
        Setmeal setmeal = setmealMapper.getById(id);
        BeanUtils.copyProperties(setmeal,setmealVO);

        //根据套餐信息查询菜品信息
        List<SetmealDish> setmealDishList = setmealDishMapper.getBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishList);

        return setmealVO;
    }

    /**
     * @description:    修改套餐
     * @author: liangguang
     * @date: 2024/8/26 0026 15:08
     * @param: [setmealDTO]
     * @return: void
     **/
    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //1.修改套餐表，执行updata
        setmealMapper.update(setmeal);
        //套餐id
        Long id = setmealDTO.getId();
        //2.删除套餐和菜品的关联关系
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(id));
        //3.重新插入套餐和菜品的关联关系
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * @description:    套餐起售停售
     * @author: liangguang
     * @date: 2024/8/26 0026 15:21
     * @param: [status, id]
     * @return: void
     **/
    @Override
    public void startOrStop(Integer status, Long id) {
        //起售套餐时，判断套餐内是否有停售菜品，有停售菜品提示”套餐内包含未起售菜品，无法起售“
        if (status == StatusConstant.ENABLE){
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            if (dishList != null && dishList.size() > 0){
                dishList.forEach(dish -> {
                    if (StatusConstant.ENABLE == dish.getStatus()){ //有停售商品
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }
}
