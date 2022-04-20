package com.increff.assure.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.increff.assure.model.data.ChannelOrderItemInvoiceData;
import com.increff.assure.model.data.ChannelOrderInvoiceData;
import com.increff.assure.util.ConvertUtil;
import model.data.*;
import model.form.ChannelOrderForm;
import model.form.OrderItemValidationForm;
import model.form.OrderValidationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ClientWrapper {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    HttpHeaders httpHeaders;

    @Value("${assure.server.url}")
    private String assureServerUrl;

    public <T> HttpEntity<String> getHttpRequest(T object) throws ApiException {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        try {
            return new HttpEntity<>(mapper.writeValueAsString(object), httpHeaders);
        } catch (JsonProcessingException e) {
            throw new ApiException(e.getMessage());
        }
    }

    public void addOrder(ChannelOrderForm orderForm) throws ApiException {
        new RestTemplate().postForObject(assureServerUrl + "/api/order/channel", getHttpRequest(orderForm), String.class);
    }

    public ChannelOrderInvoiceData convert(OrderInvoiceData orderInvoiceData) throws ApiException {
        ChannelOrderInvoiceData channelInvoice = ConvertUtil.convert(orderInvoiceData, ChannelOrderInvoiceData.class);
        List<ChannelOrderItemInvoiceData> channelInvoiceItemList = new ArrayList<>(ConvertUtil.convert(orderInvoiceData.getOrderItems(), ChannelOrderItemInvoiceData.class));
        channelInvoice.setOrderItems(channelInvoiceItemList);
        return channelInvoice;
    }

    public List<OrderData> getOrdersByChannel(Long channelId) throws ApiException {
        String urlGETList = assureServerUrl + "/api/order/channel/"+channelId;

        ResponseEntity<OrderData[]> responseEntity = new RestTemplate().getForEntity(urlGETList, OrderData[].class);
        OrderData[] objects = responseEntity.getBody();
        List<OrderData> orderDataList = Arrays.asList(objects);

        return ConvertUtil.convert(orderDataList, OrderData.class);
    }

    public List<PartyData> getClients() {
        String urlGETList = assureServerUrl + "/api/party/clients";

        ResponseEntity<PartyData[]> responseEntity = new RestTemplate().getForEntity(urlGETList, PartyData[].class);
        PartyData[] objects = responseEntity.getBody();

        return Arrays.asList(objects);
    }

    public List<ChannelData> getChannels() {
        String urlGETList = assureServerUrl + "/api/channel";

        ResponseEntity<ChannelData[]> responseEntity = new RestTemplate().getForEntity(urlGETList, ChannelData[].class);
        ChannelData[] objects = responseEntity.getBody();

        return Arrays.asList(objects);
    }

    public List<OrderItemData> getOrderItems(Long orderId) {
        String urlGETList = assureServerUrl + "/api/order/orderItem/orderId/"+orderId;

        ResponseEntity<OrderItemData[]> responseEntity = new RestTemplate().getForEntity(urlGETList, OrderItemData[].class);
        OrderItemData[] objects = responseEntity.getBody();

        return Arrays.asList(objects);
    }

    public void setRestTemplate(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public void validateOrder(OrderValidationForm validationForm) throws ApiException {
        new RestTemplate().postForObject(assureServerUrl + "/api/order/validate", getHttpRequest(validationForm), String.class);
    }

    public void validateOrderItem(OrderItemValidationForm validationForm) throws ApiException {
        new RestTemplate().postForObject(assureServerUrl + "/api/order/orderItem/channel/validate", getHttpRequest(validationForm), String.class);
    }
}