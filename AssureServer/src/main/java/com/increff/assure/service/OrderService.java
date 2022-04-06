package com.increff.assure.service;

import com.increff.assure.dao.OrderDao;
import com.increff.assure.pojo.OrderPojo;
import com.increff.assure.pojo.OrderItemPojo;
import com.increff.assure.dao.OrderItemDao;
import model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderService extends AbstractService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;

    public void add(OrderPojo orderPojo) throws ApiException {
        checkDuplicateOrders(orderPojo);
        orderPojo.setStatus(OrderStatus.CREATED);
        orderDao.insert(orderPojo);
    }

    public void checkDuplicateOrders(OrderPojo order) throws ApiException {
        OrderPojo exists = orderDao.selectByChannelAndChannelOrderId(order.getChannelId(), order.getChannelOrderId());
        checkNull(exists, "Order with ChannelID & ChannelOrderID pair already exist.");
    }

    public List<OrderPojo> getAll() {
        return orderDao.selectAll();
    }

    public OrderPojo getCheckId(Long id) throws ApiException {
        OrderPojo orderPojo = orderDao.select(id);
        checkNotNull(orderPojo, "Order (ID:" + id + ") does not exist.");
        return orderPojo;
    }

    public void updateStatus(Long orderId, OrderStatus status) throws ApiException {
        OrderPojo matchedPojo = getCheckId(orderId);
        matchedPojo.setStatus(status);
    }

    public Long getOrderClient(Long orderId) throws ApiException {
        return getCheckId(orderId).getClientId();
    }

    public List<OrderItemPojo> getItemsByOrderId(Long orderId){
        return orderItemDao.selectByOrderId(orderId);
    }

    public List<OrderPojo> getByChannel(Long channelId) {
        return orderDao.selectByChannel(channelId);
    }

    public List<OrderPojo> getSearch(Long clientId, Long customerId, Long channelId, ZonedDateTime fromDate, ZonedDateTime toDate) {
        return orderDao.getSearch(clientId, customerId, channelId, fromDate, toDate);
    }

    public void updateFulfilledQty(Long orderId){
        List<OrderItemPojo> list = orderItemDao.selectByOrderId(orderId);
        for(OrderItemPojo orderItemPojo: list){
            orderItemPojo.setAllocatedQuantity(orderItemPojo.getAllocatedQuantity()-orderItemPojo.getOrderedQuantity());
            orderItemPojo.setFulfilledQuantity(orderItemPojo.getFulfilledQuantity()+orderItemPojo.getOrderedQuantity());
        }
        OrderPojo orderPojo = orderDao.select(orderId);
        orderPojo.setStatus(OrderStatus.FULFILLED);
    }
}