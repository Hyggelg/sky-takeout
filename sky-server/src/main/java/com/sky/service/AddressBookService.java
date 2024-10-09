package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

    /**
     * @description:    查询当前登录用户的所有地址信息
     * @author: liangguang
     * @date: 2024/9/5 0005 14:07
     * @param: [addressBook]
     * @return: java.util.List<com.sky.entity.AddressBook>
     **/
    List<AddressBook> list(AddressBook addressBook);

    /**
     * @description:    新增地址
     * @author: liangguang
     * @date: 2024/9/5 0005 14:20
     * @param: [addressBook]
     * @return: void
     **/
    void addAddressBook(AddressBook addressBook);

    /**
     * @description:    根据id查询地址
     * @author: liangguang
     * @date: 2024/9/5 0005 14:31
     * @param: [id]
     * @return: com.sky.entity.AddressBook
     **/
    AddressBook getById(Long id);

    /**
     * @description:    根据id修改地址
     * @author: liangguang
     * @date: 2024/9/5 0005 14:35
     * @param: [addressBook]
     * @return: void
     **/
    void update(AddressBook addressBook);

    /**
     * @description:    设置默认地址
     * @author: liangguang
     * @date: 2024/9/5 0005 14:45
     * @param: [id]
     * @return: void
     **/
    void setDefault(AddressBook addressBook);

    /**
     * @description:    根据id删除地址
     * @author: liangguang
     * @date: 2024/9/5 0005 14:54
     * @param: [id]
     * @return: void
     **/
    void deleteById(Long id);
}

