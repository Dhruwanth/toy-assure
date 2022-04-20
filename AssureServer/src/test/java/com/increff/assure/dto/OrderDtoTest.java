//package com.increff.assure.dto;
//
//import com.increff.assure.dao.BinSkuDao;
//import com.increff.assure.dao.InventoryDao;
//import com.increff.assure.dao.OrderDao;
//import com.increff.assure.dao.OrderItemDao;
//import model.data.OrderData;
//import model.PartyType;
//import model.InvoiceType;
//import model.OrderStatus;
//import com.increff.assure.service.ApiException;
//import model.form.*;
//import com.increff.assure.pojo.*;
//import com.increff.assure.service.*;
//import com.increff.assure.util.AbstractUnitTest;
//import com.increff.assure.util.DataUtil;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
//public class OrderDtoTest extends AbstractUnitTest {
//    @Autowired
//    private OrderDto orderDto;
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private PartyService partyService;
//
//    @Autowired
//    private ProductService productService;
//
//    @Autowired
//    private InventoryDao inventoryDao;
//
//    @Autowired
//    private BinSkuDao binSkuDao;
//
//    @Autowired
//    private OrderItemDao itemDao;
//
//    @Autowired
//    private OrderDao orderDao;
//
//    @Autowired
//    private BinSkuService binSkuService;
//
//    @Autowired
//    private ChannelService channelService;
//
//    @Autowired
//    private ChannelListingService channelListingService;
//
//    private List<OrderItemForm> items;
//    private ChannelPojo channel;
//    private PartyPojo client;
//    private PartyPojo customer;
//    private ProductPojo product;
//    private BinSkuPojo binSku;
//    private OrderForm order;
//    private ChannelOrderForm channelOrder;
//    private List<ChannelOrderItemForm> channelItems;
//    private ChannelListingPojo listing;
//
//    @Before
//    public void init() throws ApiException {
//        client = DataUtil.createPartyPojo("client", PartyType.CLIENT);
//        customer = DataUtil.createPartyPojo("customer", PartyType.CUSTOMER);
//        partyService.add(client);
//        partyService.add(customer);
//        channel = DataUtil.createChannelPojo("INTERNAL", InvoiceType.SELF);
//        channelService.add(channel);
//        channel = DataUtil.createChannelPojo("FLIPKART", InvoiceType.CHANNEL);
//        channelService.add(channel);
//        product = DataUtil.createProductPojo("product1", "brand", "clientSku", "desc", 34.21, client.getId());
//        productService.add(product);
//        binSku = new BinSkuPojo();
//        binSkuDao.insert(binSku);
//        binSku = DataUtil.createBinSkuPojo(100L, product.getId(), binSku.getBinId());
//        binSkuService.addOrUpdate(binSku);
//        inventoryDao.insert(DataUtil.createInventoryPojo(product.getId(),binSku.getQuantity()));
//        items = new ArrayList<>();
//        OrderItemForm item = DataUtil.createOrderItem(product.getClientSkuId(), 5L, 2347.1);
//        items.add(item);
//        listing = DataUtil.createListing(product.getId(), "channel SKU ID", channel.getId(), client.getId());
//        List<ChannelListingPojo> channelList = new ArrayList<>();
//        channelList.add(listing);
//        channelListingService.addChannelListings(channelList);
//        order = DataUtil.createOrderForm("channelOrderId", client.getId(), customer.getId(), items,1L);
//        channelItems = new ArrayList<>();
//        ChannelOrderItemForm lineItem = DataUtil.createChannelItem(listing.getChannelSkuId(), 6L);
//        channelItems.add(lineItem);
//        channelOrder = DataUtil.createChannelOrderForm("channelOrderID", client.getId(), customer.getId(), channel.getId(), channelItems);
//    }
//
//    @Test
//    public void testPlaceOrder() throws ApiException {
//        orderDto.add(order);
//        assertEquals(1, orderService.getAll().size());
//        OrderPojo returned = orderService.getAll().get(0);
//        assertEquals(OrderStatus.CREATED, returned.getStatus());
//        assertEquals(customer.getId(), returned.getCustomerId());
//        assertEquals(client.getId(), returned.getClientId());
//        assertEquals(order.getChannelOrderId(), returned.getChannelOrderId());
//        assertEquals(1, itemDao.selectAll().size());
//        OrderItemPojo returnedItem = itemDao.selectAll().get(0);
//        assertEquals(returned.getId(), returnedItem.getOrderId());
//        OrderItemForm item = items.get(0);
//        assertEquals(item.getOrderedQuantity(), returnedItem.getOrderedQuantity());
//        assertEquals(new Long(0), returnedItem.getAllocatedQuantity());
//        assertEquals(new Long(0), returnedItem.getFulfilledQuantity());
//        assertEquals(product.getId(), returnedItem.getGlobalSkuId());
//    }
//
//    @Test(expected = ApiException.class)
//    public void testPlaceOrderCustomerInvalid() throws ApiException {
//        order.setCustomerId(customer.getId() + 1);
//        orderDto.add(order);
//    }
//
//    @Test(expected = ApiException.class)
//    public void testPlaceOrderClientInvalid() throws ApiException {
//        order.setClientId(client.getId() + 1);
//        orderDto.add(order);
//    }
//
//    @Test(expected = ApiException.class)
//    public void testPlaceOrderItemDuplicate() throws ApiException {
//        items.add(items.get(0));
//        order.setOrderItemList(items);
//        orderDto.add(order);
//    }
//
//    @Test(expected = ApiException.class)
//    public void testPlaceOrderInvalidProduct() throws ApiException {
//        OrderItemForm item = items.get(0);
//        item.setClientSkuId(item.getClientSkuId() + "invalid");
//        items.add(item);
//        order.setOrderItemList(items);
//        orderDto.add(order);
//    }
//
//    @Test
//    public void testPlaceOrderChannel() throws ApiException {
//        orderDto.addOrderFromChannel(channelOrder);
//        assertEquals(1, orderService.getAll().size());
//        OrderPojo returned = orderService.getAll().get(0);
//        assertEquals(OrderStatus.CREATED, returned.getStatus());
//        assertEquals(customer.getId(), returned.getCustomerId());
//        assertEquals(client.getId(), returned.getClientId());
//        assertEquals(channelOrder.getChannelOrderId(), returned.getChannelOrderId());
//        assertEquals(1, itemDao.selectAll().size());
//        OrderItemPojo returnedItem = itemDao.selectAll().get(0);
//        assertEquals(returned.getId(), returnedItem.getOrderId());
//        ChannelOrderItemForm item = channelItems.get(0);
//        assertEquals(item.getOrderedQuantity(), returnedItem.getOrderedQuantity());
//        assertEquals(new Long(0), returnedItem.getAllocatedQuantity());
//        assertEquals(new Long(0), returnedItem.getFulfilledQuantity());
//        assertEquals(product.getId(), returnedItem.getGlobalSkuId());
//    }
//
//    @Test(expected = ApiException.class)
//    public void testPlaceOrderChannelInvalidProduct() throws ApiException {
//        ChannelOrderItemForm item = channelItems.get(0);
//        item.setChannelSkuId(item.getChannelSkuId() + "Invalid");
//        channelItems.add(item);
//        orderDto.addOrderFromChannel(channelOrder);
//    }
//
//    @Test
//    public void testAllocate() throws ApiException {
//        orderDto.add(order);
//        Long orderId=orderDao.selectAll().get(0).getId();
//        orderDto.runAllocation(orderId);
//        assertEquals(OrderStatus.ALLOCATED,orderDao.selectAll().get(0).getStatus());
//    }
//
//    @Test
//    public void testCreateInvoice() throws Exception {
//        orderDto.add(order);
//        Long orderId=orderDao.selectAll().get(0).getId();
//        orderDto.runAllocation(orderId);
//        assertEquals(OrderStatus.ALLOCATED,orderDao.selectAll().get(0).getStatus());
//        OrderPojo orderPojo=orderDao.selectAll().get(0);
//        orderDto.fulfillOrder(orderPojo.getId());
//    }
//
//    @Test
//    public void testGetSearch() throws ApiException {
//        orderDto.add(order);
//        order = DataUtil.createOrderForm("channelOrderId2", client.getId(), customer.getId(), items, 1L);
//        orderDto.add(order);
//        OrderSearchForm form = DataUtil.createOrderSearchForm(null, null, customer.getId(), null);
//        List<OrderData> list = orderDto.getSearch(form);
//        assertEquals(2, list.size());
//    }
//
//}
