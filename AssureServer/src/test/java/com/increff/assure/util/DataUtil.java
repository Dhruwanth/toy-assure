package com.increff.assure.util;

import model.PartyType;
import model.InvoiceType;
import model.OrderStatus;
import model.form.*;
import com.increff.assure.pojo.*;

import java.time.ZonedDateTime;
import java.util.List;

public class DataUtil {
    public static PartyPojo createPartyPojo(String name, PartyType type) {
        PartyPojo consumerPojo = new PartyPojo();
        consumerPojo.setName(name);
        consumerPojo.setType(type);
        return consumerPojo;
    }

    public static PartyForm createPartyForm(String name, PartyType type) {
        PartyForm consumerForm = new PartyForm();
        consumerForm.setName(name);
        consumerForm.setType(type);
        return consumerForm;
    }

    public static ProductForm createProductForm(String name, String brandId, String clientSkuId, String desc, Double mrp) {
        ProductForm productForm = new ProductForm();
        productForm.setDescription(desc);
        productForm.setClientSkuId(clientSkuId);
        productForm.setMrp(mrp);
        productForm.setName(name);
        productForm.setBrandId(brandId);
        return productForm;
    }

    public static ProductPojo createProductPojo(String name, String brand, String clientSkuId, String desc, Double mrp, Long clientId) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(name);
        productPojo.setMrp(mrp);
        productPojo.setDescription(desc);
        productPojo.setClientId(clientId);
        productPojo.setClientSkuId(clientSkuId);
        productPojo.setBrandId(brand);
        return productPojo;
    }

    public static BinSkuPojo createBinSkuPojo(Long qty, Long globalSku, Long binId) {
        BinSkuPojo pojo = new BinSkuPojo();
        pojo.setQuantity(qty);
        pojo.setGlobalSkuId(globalSku);
        pojo.setBinId(binId);
        return pojo;
    }
    
    public static BinSkuForm createBinSkuForm(Long binId, Long globalSkuId, Long qty) {
        BinSkuForm form = new BinSkuForm();
        form.setBinId(binId);
        form.setGlobalSkuId(globalSkuId);
        form.setQuantity(qty);
        return form;
    }

    public static InventoryPojo createInventoryPojo(Long globalSkuId, Long qty) {
        InventoryPojo pojo = new InventoryPojo();
        pojo.setGlobalSkuId(globalSkuId);
        pojo.setAllocatedQuantity(0L);
        pojo.setFulfilledQuantity(0L);
        pojo.setAvailableQuantity(qty);
        return pojo;
    }

    public static OrderSearchForm createOrderSearchForm(Long channelId, Long clientId, Long customerId, Long orderId) {
        OrderSearchForm form = new OrderSearchForm();
        form.setChannelId(channelId);
        form.setClientId(clientId);
        form.setCustomerId(customerId);
        ZonedDateTime lt = ZonedDateTime.now();
        form.setFromDate(lt.minusMonths(3));
        form.setToDate(lt.plusMonths(3));
        return form;
    }
    
    public static OrderForm createOrderForm(String channelOrderId, Long clientId, Long customerId, List<OrderItemForm> items, Long channelId) {
        OrderForm form = new OrderForm();
        form.setChannelOrderId(channelOrderId);
        form.setClientId(clientId);
        form.setCustomerId(customerId);
        form.setOrderItemList(items);
        form.setChannelId(channelId);
        return form;
    }

    public static OrderItemForm createOrderItem(String clientSkuId, Long qty, Double price) {
        OrderItemForm form = new OrderItemForm();
        form.setClientSkuId(clientSkuId);
        form.setOrderedQuantity(qty);
        return form;
    }

    public static OrderPojo createOrderPojo(OrderStatus status, Long channelId, String channelOrderId, Long customerId, Long clientId) {
        OrderPojo pojo = new OrderPojo();
        pojo.setStatus(status);
        pojo.setChannelId(channelId);
        pojo.setChannelOrderId(channelOrderId);
        pojo.setCustomerId(customerId);
        pojo.setClientId(clientId);
        return pojo;
    }

    public static OrderItemPojo createOrderItem(Long globalSkuId, Long qty, Long orderId, Double price, Long allocated, Long fulfilled) {
        OrderItemPojo pojo = new OrderItemPojo();
        pojo.setGlobalSkuId(globalSkuId);
        pojo.setOrderedQuantity(qty);
        pojo.setOrderId(orderId);
        pojo.setAllocatedQuantity(allocated);
        pojo.setFulfilledQuantity(fulfilled);
        return pojo;
    }
    
    public static ChannelOrderForm createChannelOrderForm(String channelOrderId, Long clientId, Long customerId,Long channelId, List<ChannelOrderItemForm> items) {
        ChannelOrderForm form = new ChannelOrderForm();
        form.setOrderItemList(items);
        form.setCustomerId(customerId);
        form.setClientId(clientId);
        form.setChannelId(channelId);
        form.setChannelOrderId(channelOrderId);
        return form;
    }
    
    public static ChannelOrderItemForm createChannelItem(String channelSkuId, Long qty) {
        ChannelOrderItemForm item = new ChannelOrderItemForm();
        item.setChannelSkuId(channelSkuId);
        item.setOrderedQuantity(qty);
        return item;
    }

    public static ChannelPojo createChannelPojo(String name, InvoiceType type) {
        ChannelPojo pojo = new ChannelPojo();
        pojo.setInvoiceType(type);
        pojo.setName(name);
        return pojo;
    }

    public static ChannelForm createChannelForm(String name, InvoiceType type) {
        ChannelForm form = new ChannelForm();
        form.setInvoiceType(type);
        form.setName(name);
        return form;
    }
    
    public static ChannelListingForm createChannelListingForm(String clientSkuId, String channelSkuId) {
        ChannelListingForm form = new ChannelListingForm();
        form.setClientSkuId(clientSkuId);
        form.setChannelSkuId(channelSkuId);
        return form;
    }

    public static ChannelListingPojo createListing(Long globalSkuId, String channelSkuId, Long channelId, Long clientId) {
        ChannelListingPojo pojo = new ChannelListingPojo();
        pojo.setGlobalSkuId(globalSkuId);
        pojo.setChannelSkuId(channelSkuId);
        pojo.setChannelId(channelId);
        pojo.setClientId(clientId);
        return pojo;
    }
}
