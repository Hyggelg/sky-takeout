package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatPayUtil weChatPayUtil;

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * @description:    用户下单
     * @author: liangguang
     * @date: 2024/9/11 0011 13:35
     * @param: [ordersSubmitDTO]
     * @return: com.sky.vo.OrderSubmitVO
     **/
    @Override
    @Transactional/*添加事务注解*/
    public OrderSubmitVO submitOreder(OrdersSubmitDTO ordersSubmitDTO) {

        //1.处理业务异常 （地址簿为空，购物车数据为空） 判断
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            //抛出业务异常   地址为空，不能下单
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        //查询当前用户的购物车数据
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);

        if (shoppingCartList == null && shoppingCartList.size() == 0) {
            //抛出业务异常  购物车为空，不能下单
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }


        //2.向订单表插入一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);/*属性拷贝*/
        orders.setOrderTime(LocalDateTime.now());    /*当前时间*/
        orders.setPayStatus(Orders.UN_PAID);         /*未付款*/
        orders.setStatus(Orders.PENDING_PAYMENT);    /*订单状态*/
        orders.setNumber(String.valueOf(System.currentTimeMillis()));/*订单号*/
        orders.setPhone(addressBook.getPhone());     /*手机号*/
        orders.setConsignee(addressBook.getConsignee());/*收货人*/
        orders.setUserId(userId);
        orderMapper.insert(orders);


        //3.向订单明细表插入多条数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart: shoppingCartList){
            OrderDetail orderDetail = new OrderDetail();//订单明细
            BeanUtils.copyProperties(cart, orderDetail);/*属性拷贝*/
            orderDetail.setOrderId(orders.getId());//设置当前订单明细关联的订单id
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);


        //4.下单成功，清空购物车数据
        shoppingCartMapper.deleteByUserId(userId);
        //封装VO返回结果
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();

        return orderSubmitVO;
    }


    /**
     * @description:    订单支付
     * @author: liangguang
     * @date: 2024/9/11 0011 15:34
     * @param: [ordersPaymentDTO]
     * @return: com.sky.vo.OrderPaymentVO
     **/
    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long id = BaseContext.getCurrentId();
        User user = userMapper.getById(id);

        //调用微信支付接口，生成预支付交易单
       /* JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );*/
        JSONObject jsonObject = new JSONObject();

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * @description:    支付成功，修改订单状态
     * @author: liangguang
     * @date:  15:54
     * @param: [outTradeNo]
     * @return: void
     **/
    @Override
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);

        //通过websocket向客户端浏览器推送消息 type orderId content  来单提醒
        Map map = new HashMap();
        map.put("type",1);  //1表示来单提醒 2表示客户催单
        map.put("orderId", ordersDB.getId());
        map.put("content","订单号: " + outTradeNo);

        String json = JSON.toJSONString(map);//map转换成JSON字符串
        webSocketServer.sendToAllClient(json);//向页面推送消息
    }

    /**
     * @description:    获取历史订单
     * @author: liangguang
     * @date: 2024/9/16 0016 14:16
     * @param: [pageNum, pageSize, status]
     * @return: com.sky.result.PageResult
     **/
    @Override
    public PageResult getHistoryOrders(int pageNum, int pageSize, Integer status) {
        //设置分页   分页查询订单  获取用户id 根据订单的状态查询订单的数据
        PageHelper.startPage(pageNum,pageSize);

        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);

        //分页条件查询
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);
        ArrayList<OrderVO> list = new ArrayList<>();

        //查询出订单明细。并封装OrderVO进行响应
        if(page != null && page.getTotal() > 0){
            for (Orders orders:page){
                Long ordersId = orders.getId();/*订单id*/
                //查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(ordersId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(),list);
    }

    /**
     * @description:    查询订单详情
     * @author: liangguang
     * @date: 2024/9/11 0011 22:41
     * @param: [orderId]
     * @return: com.sky.vo.OrderVO
     **/
    @Override
    public OrderVO details(Long id) {
        //根据id查询订单
        Orders orders = orderMapper.getById(id);

        //查询订单对应的菜品/套餐明细
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orders.getId());

        //将订单详情封装到OrderVO返回
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders,orderVO);
        orderVO.setOrderDetailList(orderDetails);

        //设置配送地址
        Long addressBookId = orders.getAddressBookId();
        String addrass = getAddrass(addressBookId);
        orders.setAddress(addrass);

        return orderVO;
    }

    /**
     * @description:    取消订单
     * @author: liangguang
     * @date: 2024/9/11 0011 23:10
     * @param: [id]
     * @return: void
     **/
    @Override
    public void cancel(Long id) throws Exception{
        //根据id查询订单
        Orders  ordersDB = orderMapper.getById(id);

        //检验订单是否存在 订单数据为空，抛出订单不存在异常
        if(ordersDB == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消    订单状态异常
        if(ordersDB.getStatus() > 2){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());

        // 订单处于待接单状态下取消，需要进行退款     获取订单状态，判断是否为待接单状态
        if (ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            //调用微信支付退款接口
            weChatPayUtil.refund(
                    ordersDB.getNumber(), //商户订单号
                    ordersDB.getNumber(), //商户退款单号
                    new BigDecimal(0.01),//退款金额，单位 元
                    new BigDecimal(0.01));//原订单金额

            //支付状态修改为 退款
            orders.setPayStatus(Orders.REFUND);
        }
        // 更新订单状态、取消原因、取消时间
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);

    }

    /**
     * @description:    再来一单
     * @author: liangguang
     * @date: 2024/9/11 0011 23:28
     * @param: [id]
     * @return: void
     **/
    @Override
    public void repetition(Long id) {

    }

    /**
     * @description:    用户催单
     * @author: liangguang
     * @date: 2024/9/16 0016 14:29
     * @param: [id]
     * @return: void
     **/
    @Override
    public void reminder(Long id) {
        //根据id查询订单
        Orders ordersDB = orderMapper.getById(id);

        //校验订单是否存在
        if (ordersDB == null){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        HashMap map = new HashMap();//type orderId content
        map.put("type",2);  //1表示来单提醒 2表示客户催单
        map.put("orderId", ordersDB.getId());
        map.put("content","订单号: " + ordersDB.getNumber());

        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);
    }

    /**
     * @description: 订单搜索
     * @author: liangguang
     * @date: 2024/9/16 0016 17:05
     * @param: [ordersPageQueryDTO]
     * @return: com.sky.result.PageResult
     **/
    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        //分页查询
        PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);
        //部分订单状态需要额外返回订单菜品信息，将Orders转化为OrderVO
        List<OrderVO> orderVOList = getOrderVOList(page);
        return new PageResult(page.getTotal(), orderVOList);
    }

    //将Orders转化为OrdersVO返回
    private List<OrderVO> getOrderVOList(Page<Orders> page) {
        //需要返回订单菜品信息，自定义OrderVO响应结果
        List<OrderVO> orderVOList = new ArrayList<>();
        List<Orders> ordersList = page.getResult();
        if(!CollectionUtils.isEmpty(ordersList)){
            for (Orders orders : ordersList) {
                //将共同字段复制到VO
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                String orderDishes = getOrderDishesStr(orders);
                //将订单菜品信息分装到orderVO中，并添加到orderVOList
                orderVO.setOrderDishes(orderDishes);
                orderVOList.add(orderVO);
            }
        }
        return orderVOList;
    }
    //根据订单id获取菜品信息字符串
    private String getOrderDishesStr(Orders orders) {
        //查询订单菜品详情信息(订单中的菜品和数量)
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());
        //将每一条订单菜品信息拼接为字符串(格式:宫保鸡丁*3)
        List<String> orderDishList = orderDetailList.stream().map(x -> {
            String orderDish = x.getName() + "*" + x.getNumber() + ";";
            return orderDish;
        }).collect(Collectors.toList());
        //将该订单对应的所有菜品此信息拼接在一起
        return String.join("",orderDishList);
    }

    /**
     * @description: 各个状态的订单数量统计
     * @author: liangguang
     * @date: 2024/9/16 0016 17:35
     * @param: []
     * @return: com.sky.vo.OrderOverViewVO
     **/
    @Override
    public OrderStatisticsVO statistics() {
        //根据订单状态查询出待接单，带派送，派送中的订单数量
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);//待接单
        Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);//带派送
        Integer deleveryInProcess = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);//派送中
        //将查询到的数据封装到ordersStatisticsVO中响应
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deleveryInProcess);

        return orderStatisticsVO;
    }

    /**
     * @description:    接单
     * @author: liangguang
     * @date: 2024/9/16 0016 17:53
     * @param: [ordersConfirmDTO]
     * @return: void
     **/
    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();
        orderMapper.update(orders);
    }

    /**
     * @description:    拒单
     * @author: liangguang
     * @date: 2024/9/16 0016 17:58
     * @param: [ordersRejectionDTO]
     * @return: void
     **/
    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) {
        //根据id查询订单
        Orders ordersDB = orderMapper.getById(ordersRejectionDTO.getId());
        //只有订单存在且状态为2才可以拒单
        if(ordersDB == null && !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //根据订单id更新订单状态，拒单原因，取消时间
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * @description:    商家取消订单
     * @author: liangguang
     * @date: 2024/9/16 0016 18:11
     * @param: [ordersCancelDTO]
     * @return: void
     **/
    @Override
    public void cancelOrder(OrdersCancelDTO ordersCancelDTO) {
        //根据id查询订单
        Orders ordersDB = orderMapper.getById(ordersCancelDTO.getId());
        //管理端取消需要退款，根据订单id更新订单状态，取消原因，取消时间
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * @description:    派送订单
     * @author: liangguang
     * @date: 2024/9/16 0016 18:19
     * @param: [id]
     * @return: void
     **/
    @Override
    public void delivery(Long id) {
        //根据id查询订单
        Orders ordersDB = orderMapper.getById(id);
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * @description:    完成订单
     * @author: liangguang
     * @date: 2024/9/16 0016 18:25
     * @param: [id]
     * @return: void
     **/
    @Override
    public void complete(Long id) {
        //根据id查询订单
        Orders ordersDB = orderMapper.getById(id);
        //订单是否存在，并且状态为4
        if(ordersDB == null && !ordersDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.COMPLETED);
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }


    //获取订单配送地址
    private String getAddrass(Long addressBookId){
        AddressBook addressBook = addressBookMapper.getById(addressBookId);
        String address = addressBook.getProvinceName() + addressBook.getCityName() +
                addressBook.getDistrictName() + addressBook.getDetail();
        return address;
    }

}
