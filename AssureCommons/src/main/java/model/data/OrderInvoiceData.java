package model.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class OrderInvoiceData {

    Long orderId;
    String channelName;
    String channelOrderId;
    String orderCreationTime;
    String clientName;
    String customerName;

    List<OrderItemInvoiceData> orderItems;

    public Long getOrderId() {
        return orderId;
    }

    @XmlElement
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getChannelName() {
        return channelName;
    }

    @XmlElement
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelOrderId() {
        return channelOrderId;
    }

    @XmlElement
    public void setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
    }

    public String getOrderCreationTime() {
        return orderCreationTime;
    }

    @XmlElement
    public void setOrderCreationTime(String orderCreationTime) {
        this.orderCreationTime = orderCreationTime;
    }

    public String getClientName() {
        return clientName;
    }

    @XmlElement
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getCustomerName() {
        return customerName;
    }

    @XmlElement
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<OrderItemInvoiceData> getOrderItems() {
        return orderItems;
    }

    @XmlElement
    public void setOrderItems(List<OrderItemInvoiceData> orderItems) {
        this.orderItems = orderItems;
    }
}