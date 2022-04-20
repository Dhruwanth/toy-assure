//package com.increff.assure.spring;
//
//import model.InvoiceType;
//import model.OrderStatus;
//import model.data.ChannelData;
//import model.data.OrderData;
//
//public class TestPojo {
//
//    public static OrderData createOrderData(Long clientId, Long customerId, String channelOrderId,
//                                            OrderStatus os, Long channelId){
//        OrderData data = new OrderData();
//        data.setClientId(clientId);
//        data.setCustomerId(customerId);
//        data.setChannelOrderId(channelOrderId);
//        data.setStatus(os);
//        data.setChannelId(channelId);
//        data.setId(199L);
//        return data;
//    }
//
//    public static ChannelInvoiceData createChannelInvoiceData(String clientName, String channelName, String customerName, String dateTime, Long orderId, Double total) {
//        ChannelInvoiceData data=new ChannelInvoiceData();
//        data.setOrderId(orderId);
//        data.setTotal(total);
//        data.setProductDataList(null);
//        data.setChannelName(channelName);
//        data.setClientName(clientName);
//        data.setCustomerName(customerName);
//        data.setGeneratedDateTime(dateTime);
//        return data;
//    }
//
//    public static ChannelData createChannelData(Long id, String name, InvoiceType invoiceType){
//        ChannelData data = new ChannelData();
//        data.setId(id);
//        data.setName(name);
//        data.setInvoiceType(invoiceType);
//        return data;
//    }
//
//}