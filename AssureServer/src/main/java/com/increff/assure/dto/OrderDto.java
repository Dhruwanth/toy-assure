package com.increff.assure.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.increff.assure.pojo.*;
import com.increff.assure.service.*;
import com.increff.assure.util.DateUtil;
import com.increff.assure.util.PdfGenerateUtil;
import com.increff.assure.util.XmlUtil;
import model.PartyType;
import model.InvoiceType;
import model.OrderStatus;
import model.data.OrderData;
import model.data.OrderItemData;
import model.data.OrderItemInvoiceData;
import model.data.OrderInvoiceData;
import model.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.increff.assure.util.ConvertUtil.convert;

@Service
public class OrderDto extends AbstractDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private BinSkuService binSkuService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private PartyService partyService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productMasterService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ChannelListingService channelListingService;
    @Autowired
    private ClientWrapper clientWrapper;

    public void add(OrderForm orderForm) throws ApiException {
        checkValid(orderForm);

        OrderPojo orderPojo = convert(orderForm, OrderPojo.class);
        validateOrder(orderPojo);
        orderService.add(orderPojo);

        addOrderItemsForOrder(orderForm.getOrderItemList(), orderPojo.getId(), orderPojo.getClientId());//TODO refactor this method(change validation mechanism)
    }

    public void addOrderFromChannel(ChannelOrderForm orderForm) throws ApiException {
        checkValid(orderForm);

        OrderPojo orderPojo = convert(orderForm, OrderPojo.class);
        validateOrder(orderPojo);
        orderService.add(orderPojo);

        addChannelOrderItemsForOrder(orderForm.getOrderItemList(), orderForm.getChannelId(), orderPojo.getId(), orderPojo.getClientId());
    }

    private void addOrderItemsForOrder(List<OrderItemForm> orderItemFormList, Long orderId, Long clientId) throws ApiException {
        List<OrderItemPojo> orderItemList = convertFormToPojo(orderItemFormList, orderId, clientId);
        for (OrderItemPojo orderItem : orderItemList)
            validateOrderItem(orderItem);
        orderItemService.addList(orderItemList);
    }

    private void addChannelOrderItemsForOrder(List<ChannelOrderItemForm> orderItemFormList, Long channelId, Long orderId, Long clientId) throws ApiException {
        List<OrderItemPojo> orderItemList = convertFormToPojo(orderItemFormList, channelId, orderId, clientId);
        for (OrderItemPojo orderItem : orderItemList)
            validateOrderItem(orderItem);// do all validations together
        orderItemService.addList(orderItemList);
    }

    public void validateOrderItem(OrderItemPojo orderItemPojo) throws ApiException {
        productService.getCheckId(orderItemPojo.getGlobalSkuId());

        Long clientId = orderService.getCheckId(orderItemPojo.getOrderId()).getClientId();
        checkTrue(clientId.equals(productService.getClientIdOfProduct(orderItemPojo.getGlobalSkuId())),
                "Invalid Client for Product(ID: " + orderItemPojo.getGlobalSkuId() + ")");

        Long channelId = orderService.getCheckId(orderItemPojo.getOrderId()).getChannelId();
        if (!channelService.getName(channelId).equals("INTERNAL"))
            checkNotNull(channelListingService.getByChannelIdAndGlobalSku(channelId, orderItemPojo.getGlobalSkuId()),
                    "Channel does not provide the mentioned Product");
    }

    public OrderData get(Long id) throws ApiException {
        return convertPojoToData(orderService.getCheckId(id));
    }

    public boolean isOrderAllocated(Long orderId) {
        for (OrderItemPojo orderItem : orderItemService.getByOrderId(orderId)) {
            if (!orderItem.getOrderedQuantity().equals(orderItem.getAllocatedQuantity()))
                return false;
        }
        return true;
    }

    @Transactional(rollbackFor = ApiException.class)
    public void runAllocation(Long orderId) throws ApiException {
        checkFalse(orderService.getCheckId(orderId).getStatus().equals(OrderStatus.ALLOCATED), "Order is already ALLOCATED");//This error should not be there(idempotency)
        checkFalse(orderService.getCheckId(orderId).getStatus().equals(OrderStatus.FULFILLED), "Cannot Allocate FULFILLED Order");

        long globalSkuOfOrderItem, orderedQuantity, allocatedOrderItemQuantity;//change orderedquantity to quantitytoallocate

        for (OrderItemPojo orderItem : orderItemService.getByOrderId(orderId)) {
            globalSkuOfOrderItem = orderItem.getGlobalSkuId();
            orderedQuantity = orderItem.getOrderedQuantity() - orderItem.getAllocatedQuantity();

            allocatedOrderItemQuantity = binSkuService.allocateFromAllBins(globalSkuOfOrderItem, orderedQuantity);
            if (allocatedOrderItemQuantity == 0)
                continue;
            inventoryService.allocateAvailableItems(globalSkuOfOrderItem, allocatedOrderItemQuantity);
            orderItemService.allocateOrderItems(orderItem, allocatedOrderItemQuantity);
        }
        updateOrderStatusToAllocated(orderId);
    }

    public void updateOrderStatusToAllocated(Long orderId) throws ApiException {
        if (isOrderAllocated(orderId))
            orderService.updateStatus(orderId, OrderStatus.ALLOCATED);
    }

    public String fulfillOrder(Long orderId) throws ApiException, JsonProcessingException {
        OrderPojo order = orderService.getCheckId(orderId);
        checkFalse(order.getStatus().equals(OrderStatus.CREATED), "Order is not Allocated");

        if (order.getStatus().equals(OrderStatus.ALLOCATED))
            orderItemService.fulfillOrderItems(orderId);

        if (channelService.getInvoiceType(order.getChannelId()).equals(InvoiceType.SELF))
            return Base64.getEncoder().encodeToString(generateInvoicePdf(order));//maintain idempotency for fulfilled orders

        return clientWrapper.fetchInvoiceFromChannel(createOrderInvoice(order));
    }

    private byte[] generateInvoicePdf(OrderPojo order) throws ApiException {
        OrderInvoiceData orderReceipt = createOrderInvoice(order);
        return PdfGenerateUtil.generate(XmlUtil.generate(orderReceipt));
    }

    private OrderInvoiceData createOrderInvoice(OrderPojo order) throws ApiException {//instead of getting it again just pass as parameters
        OrderInvoiceData orderInvoice = new OrderInvoiceData();
        orderInvoice.setOrderId(order.getId());
        orderInvoice.setChannelName(channelService.getName(order.getChannelId()));
        orderInvoice.setChannelOrderId(order.getChannelOrderId());
        orderInvoice.setClientName(partyService.getName(order.getClientId()));
        orderInvoice.setCustomerName(partyService.getName(order.getCustomerId()));
        orderInvoice.setOrderCreationTime(order.getCreatedAt().format(DateTimeFormatter.ofPattern(DateUtil.getDateFormat())));

        List<OrderItemInvoiceData> orderItems = new ArrayList<>();
        for (OrderItemPojo orderItem : orderItemService.getByOrderId(order.getId())) {
            OrderItemInvoiceData orderItemReceipt = new OrderItemInvoiceData();

            orderItemReceipt.setClientSkuId(productService.getCheckId(orderItem.getGlobalSkuId()).getClientSkuId());
            orderItemReceipt.setOrderItemId(orderItem.getId());
            orderItemReceipt.setQuantity(orderItem.getFulfilledQuantity());

            ProductPojo product = productService.getCheckId(orderItem.getGlobalSkuId());
            orderItemReceipt.setMrp(product.getMrp());
            orderItemReceipt.setTotal((long) (orderItem.getFulfilledQuantity() * product.getMrp()));

            if (!channelService.getName(order.getChannelId()).equals("INTERNAL"))
                orderItemReceipt.setChannelSkuId(channelListingService.getByChannelIdAndGlobalSku(order.getChannelId(), product.getId()).getChannelSkuId());

            orderItems.add(orderItemReceipt);
        }
        orderInvoice.setOrderItems(orderItems);
        return orderInvoice;
    }

    public void validateOrderForm(OrderValidationForm validationForm) throws ApiException {
        validateOrder(convert(validationForm, OrderPojo.class));
    }

    public List<OrderData> getByChannel(Long channelId) throws ApiException {
        channelService.getCheckId(channelId);
        return convertPojoToData(orderService.getByChannel(channelId));
    }

    public void validateList(List<OrderItemForm> formList, Long clientId, Long channelId) throws ApiException {
        partyService.getCheckClient(clientId);
        channelService.getCheckId(channelId);

        StringBuilder errorDetailString = new StringBuilder();
        HashSet<String> clientSkus = new HashSet<>();
        for (int index = 0; index < formList.size(); index++) {
            try {
                OrderItemForm form = formList.get(index);
                Long globalSkuId = productService.getByClientAndClientSku(clientId, form.getClientSkuId()).getId();

                checkValid((form));
                checkTrue(form.getOrderedQuantity() > 0, "Quantity must be positive");
                checkFalse(clientSkus.contains(form.getClientSkuId()), "Duplicate Client SKU");
                checkNotNull(inventoryService.getByGlobalSku(globalSkuId), "Product not in Inventory");
                checkTrue(form.getOrderedQuantity() <= inventoryService.getByGlobalSku(globalSkuId).getAvailableQuantity(),
                        "Insufficient Stock. " + inventoryService.getByGlobalSku(globalSkuId).getAvailableQuantity() + " items left");
                if (!channelService.getName(channelId).equals("INTERNAL"))
                    checkNotNull(channelListingService.getByChannelIdAndGlobalSku(channelId, globalSkuId),
                            "Channel does not provide the mentioned Product");

                clientSkus.add(form.getClientSkuId());
            } catch (ApiException e) {
                errorDetailString.append("Error in Line: ").append(index + 1).append(": ").append(e.getMessage()).append("<br \\>");
            }
        }
        if (errorDetailString.length() > 0)
            throw new ApiException(errorDetailString.toString());
    }

    public List<OrderData> getSearch(OrderSearchForm form) throws ApiException {
        if (Objects.nonNull(form.getClientId()))
            partyService.getCheckClient(form.getClientId());
        if (Objects.nonNull(form.getCustomerId()))
            partyService.getCheckCustomer(form.getCustomerId());
        if (Objects.nonNull(form.getChannelId()))
            channelService.getCheckId(form.getChannelId());

        DateUtil.checkDateFilters(form.getFromDate(), form.getToDate());
        ZonedDateTime[] dateList = DateUtil.setStartEndDates(form.getFromDate(), form.getToDate());

        return convertPojoToData(orderService.getSearch(form.getClientId(),
                form.getCustomerId(), form.getChannelId(), dateList[0], dateList[1]));
    }

    public List<OrderItemData> getByOrderId(Long orderId) throws ApiException {
        List<OrderItemData> orderItemDataList = new ArrayList<>();

        for (OrderItemPojo pojo : orderItemService.getByOrderId(orderId)) {
            OrderItemData orderData = convert(pojo, OrderItemData.class);
            orderData.setClientSkuId(productService.getCheckId(pojo.getGlobalSkuId()).getClientSkuId());

            if (!channelService.getCheckId(orderService.getCheckId(orderId).getChannelId()).getName().equals("INTERNAL"))
                orderData.setChannelSkuId(channelListingService.getByChannelIdAndGlobalSku(orderService.getCheckId(orderId).
                        getChannelId(), pojo.getGlobalSkuId()).getChannelSkuId());

            orderItemDataList.add(orderData);
        }
        return orderItemDataList;
    }

    public void validateChannelOrderItemForm(OrderItemValidationForm validationForm) throws ApiException {
        checkValid(validationForm);

        ChannelListingPojo listing = channelListingService.getByChannelChannelSkuAndClient(validationForm.getChannelId(),
                validationForm.getChannelSkuId(), validationForm.getClientId());
        checkFalse(Objects.isNull(listing), "Channel listing does not exist");

        Long globalSkuId = listing.getGlobalSkuId();
        productService.getCheckId(globalSkuId);
        checkFalse(Objects.isNull(inventoryService.getByGlobalSku(globalSkuId)), "Product Not in Inventory");
        checkFalse(validationForm.getOrderedQuantity() > inventoryService.getByGlobalSku(globalSkuId).getAvailableQuantity(),
                "Insufficient Stock" + inventoryService.getByGlobalSku(globalSkuId).getAvailableQuantity() + " items left");
    }

    public void setClientWrapper(ClientWrapper clientWrapper) {
        this.clientWrapper = clientWrapper;
    }

    private void validateOrder(OrderPojo orderPojo) throws ApiException {
        checkTrue(partyService.getCheckId(orderPojo.getClientId()).getType().equals(PartyType.CLIENT), "Invalid ClientID");
        checkTrue(partyService.getCheckId(orderPojo.getCustomerId()).getType().equals(PartyType.CUSTOMER), "Invalid CustomerID");
        channelService.getCheckId(orderPojo.getChannelId());//check with channel name instead of id
        orderService.checkDuplicateOrders(orderPojo);
    }

    private OrderData convertPojoToData(OrderPojo order) throws ApiException {
        OrderData orderData = convert(order, OrderData.class);
        orderData.setOrderItemList(convert(orderItemService.getByOrderId(order.getId()), OrderItemForm.class));
        orderData.setCreatedAt(order.getCreatedAt());
        orderData.setClientName(partyService.getName(order.getClientId()));
        orderData.setCustomerName(partyService.getName(order.getCustomerId()));
        orderData.setChannelName(channelService.getName(order.getChannelId()));
        return orderData;
    }

    private List<OrderItemPojo> convertFormToPojo(List<ChannelOrderItemForm> orderItemFormList, Long channelId, Long orderId, Long clientId) {
        List<OrderItemPojo> orderItemList = new ArrayList<>();
        for (ChannelOrderItemForm form : orderItemFormList) {
            OrderItemPojo pojo = new OrderItemPojo();
            pojo.setOrderId(orderId);
            pojo.setGlobalSkuId(channelListingService.getByChannelChannelSkuAndClient(channelId, form.getChannelSkuId(), clientId).getGlobalSkuId());//get the globalsku with channelsku with the help of a mapping
            pojo.setOrderedQuantity(form.getOrderedQuantity());
            orderItemList.add(pojo);
        }
        return orderItemList;
    }

    private List<OrderItemPojo> convertFormToPojo(List<OrderItemForm> orderItemFormList, Long orderId, Long clientId) throws ApiException {
        List<OrderItemPojo> orderItemList = new ArrayList<>();
        for (OrderItemForm form : orderItemFormList) {
            OrderItemPojo pojo = new OrderItemPojo();
            pojo.setOrderId(orderId);
            pojo.setGlobalSkuId(productMasterService.getByClientAndClientSku(clientId, form.getClientSkuId()).getId());
            pojo.setOrderedQuantity(form.getOrderedQuantity());
            orderItemList.add(pojo);
        }
        return orderItemList;
    }

    private List<OrderData> convertPojoToData(List<OrderPojo> orderPojoList) throws ApiException {
        List<OrderData> orderDataList = new ArrayList<>();
        for (OrderPojo pojo : orderPojoList)
            orderDataList.add(convertPojoToData(pojo));
        return orderDataList;
    }
}