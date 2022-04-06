package model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemInvoiceData {
    Long orderItemId;
    String clientSkuId;
    String channelSkuId;
    Long quantity;
    Double mrp;//Todo change it to sellingprice
    Long total;
}