package com.increff.assure.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelOrderItemInvoiceData {
    Long orderItemId;
    String channelSkuId;
    Long quantity;
    Double mrp;
    Long total;
}