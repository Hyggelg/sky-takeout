package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AddressBookImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * @description:    查询当前登录用户的所有地址信息
     * @author: liangguang
     * @date: 2024/9/5 0005 14:09
     * @param: [addressBook]
     * @return: java.util.List<com.sky.entity.AddressBook>
     **/
    @Override
    public List<AddressBook> list(AddressBook addressBook) {
        return addressBookMapper.list(addressBook);
    }

    /**
     * @description:    新增地址
     * @author: liangguang
     * @date: 2024/9/5 0005 14:22
     * @param: [addressBook]
     * @return: void
     **/
    @Override
    public void addAddressBook(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.insert(addressBook);
    }

    /**
     * @description:    根据id查询地址
     * @author: liangguang
     * @date: 2024/9/5 0005 14:31
     * @param: [id]
     * @return: com.sky.entity.AddressBook
     **/
    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.getById(id);
    }

    /**
     * @description:    根据id修改地址
     * @author: liangguang
     * @date: 2024/9/5 0005 14:35
     * @param: [addressBook]
     * @return: void
     **/
    @Override
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    /**
     * @description:    设置默认地址
     * @author: liangguang
     * @date: 2024/9/5 0005 14:49
     * @param: [addressBook]
     * @return: void
     **/
    @Override
    public void setDefault(AddressBook addressBook) {
        //将当前用户的所有地址都修改为非默认地址  update address_book set is_default = ? where user_id = ?
        addressBook.setIsDefault(0);
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.updateIsDefaultByUserId(addressBook);

        //将当前地址修改为默认地址  update address_book set is_default = ? where id = ?
        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
    }

    /**
     * @description:    根据id删除地址
     * @author: liangguang
     * @date: 2024/9/5 0005 14:54
     * @param: [id]
     * @return: void
     **/
    @Override
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }
}
