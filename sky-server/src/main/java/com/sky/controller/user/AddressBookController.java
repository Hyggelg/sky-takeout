package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "C端-地址簿接口")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * @description:    查询当前登录用户的所有地址信息
     * @author: liangguang
     * @date: 2024/9/5 0005 14:05
     * @param: []
     * @return: com.sky.result.Result<java.util.List<com.sky.entity.AddressBook>>
     **/
    @GetMapping("/list")
    @ApiOperation(value = "查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> list(){
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }

    /**
     * @description:    新增地址
     * @author: liangguang
     * @date: 2024/9/5 0005 14:20
     * @param: [addressBook]
     * @return: com.sky.result.Result
     **/
    @PostMapping
    @ApiOperation(value = "新增地址")
    public Result add(@RequestBody AddressBook addressBook){
        addressBookService.addAddressBook(addressBook);
        return Result.success();
    }

    /**
     * @description:    根据id查询地址
     * @author: liangguang
     * @date: 2024/9/5 0005 14:29
     * @param: [id]
     * @return: com.sky.result.Result<com.sky.entity.AddressBook>
     **/
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * @description:    根据id修改地址
     * @author: liangguang
     * @date: 2024/9/5 0005 14:34
     * @param: [addressBook]
     * @return: com.sky.result.Result
     **/
    @PutMapping
    @ApiOperation(value = "根据id修改地址")
    public Result update(@RequestBody AddressBook addressBook){
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * @description:    设置默认地址
     * @author: liangguang
     * @date: 2024/9/5 0005 14:44
     * @param: [id]
     * @return: com.sky.result.Result
     **/
    @PutMapping("/default")
    @ApiOperation(value = "设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook){
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * @description:    根据id删除地址
     * @author: liangguang
     * @date: 2024/9/5 0005 14:54
     * @param: [id]
     * @return: com.sky.result.Result
     **/
    @DeleteMapping
    @ApiOperation(value = "根据id删除地址")
    public Result deleteById(Long id){
        addressBookService.deleteById(id);
        return Result.success();
    }

    /**
     * @description:    查询默认地址
     * @author: liangguang
     * @date: 2024/9/5 0005 14:58
     * @param: []
     * @return: com.sky.result.Result<com.sky.entity.AddressBook>
     **/
    @GetMapping("/default")
    @ApiOperation(value = "查询默认地址")
    public Result<AddressBook> getDefault(){
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(1);
        List<AddressBook> list = addressBookService.list(addressBook);

        if (list != null && list.size() > 0){
            return Result.success(list.get(0));
        }
        return Result.error("没有查询到默认地址");
    }
}
