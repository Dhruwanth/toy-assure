package com.increff.assure.service;

import com.increff.assure.dao.OrderItemDao;
import com.increff.assure.pojo.OrderItemPojo;
import model.OrderStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderItemService extends AbstractService {
    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderService orderService;

    public void add(OrderItemPojo orderItemPojo) throws ApiException {
        checkDuplicate(orderItemPojo);
        orderItemDao.insert(orderItemPojo);
    }

    public void addList(List<OrderItemPojo> orderItemList) throws ApiException {
        for (OrderItemPojo orderItem : orderItemList)
            add(orderItem);
    }

    private void checkDuplicate(OrderItemPojo orderItem) throws ApiException {
        OrderItemPojo matchedOrderItem = orderItemDao.selectByOrderIdAndGlobalSku(orderItem.getOrderId(), orderItem.getGlobalSkuId());
        checkNull(matchedOrderItem, "GlobalSKU, OrderID pair already exists.");
    }

    public List<OrderItemPojo> getAll() {
        return orderItemDao.selectAll();
    }

    public OrderItemPojo getCheckId(Long id) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemDao.select(id);
        checkNotNull(orderItemPojo, "OrderItem (ID:" + id + ") does not exist.");
        return orderItemPojo;
    }

    public OrderItemPojo getCheckOrderAndGlobalSku(Long orderId, Long globalSkuId) throws ApiException {
        OrderItemPojo matchedPojo = orderItemDao.selectByOrderIdAndGlobalSku(orderId, globalSkuId);
        checkNotNull(matchedPojo, "Order Item with OrderID:" + orderId + " GSKU:" + globalSkuId + " does not exist.");
        return matchedPojo;
    }

    public List<OrderItemPojo> getByOrderId(Long orderId) {
        return orderItemDao.selectByOrderId(orderId);
    }

    public void allocateOrderItems(OrderItemPojo orderItem, Long quantityToBeAllocated) throws ApiException {
        orderItem.setAllocatedQuantity(orderItem.getAllocatedQuantity() + quantityToBeAllocated);
    }//TODO check the names of the variables and make necessary corrections, can pass id instead of entire orderitem
    
    public void fulfillOrderItems(Long orderId) throws ApiException {
        Long allocatedOrderItemQuantity;
        for (OrderItemPojo orderItem : getByOrderId(orderId)) {
            allocatedOrderItemQuantity = orderItem.getAllocatedQuantity();
            orderItem.setFulfilledQuantity(orderItem.getFulfilledQuantity() + allocatedOrderItemQuantity);
            orderItem.setAllocatedQuantity(0L);

            inventoryService.fulfillInInventory(orderItem.getGlobalSkuId(), allocatedOrderItemQuantity);
        }
        orderService.getCheckId(orderId).setStatus(OrderStatus.FULFILLED);
    }
}
