package com.increff.assure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.increff.assure.dto.OrderDto;
import com.increff.assure.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import model.data.*;
import model.form.ChannelOrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/order")//TODO add channel to make them recognise that they are from channel
public class ChannelOrderController {

    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "Adds an Order")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void add(@Valid @RequestBody ChannelOrderForm form) throws ApiException, JsonProcessingException {
        orderDto.add(form);
    }

    @ApiOperation(value = "Gets all Orders for a Channel")
    @RequestMapping(path = "/channel/{channelId}", method = RequestMethod.GET)
    public List<OrderData>  getByChannel(@PathVariable Long channelId) throws ApiException {
        return orderDto.getByChannel(channelId);
    }//change the url

    @ApiOperation(value = "Generate Order Invoice")
    @RequestMapping(path = "/api/order/invoice", method = RequestMethod.POST)
    public ChannelInvoiceResponse generateReceipt(@RequestBody OrderInvoiceData orderReceiptData) throws ApiException {
        return orderDto.generateInvoice(orderReceiptData);
    }

    @ApiOperation(value = "Gets a list of all Clients")
    @RequestMapping(path = "/clients", method = RequestMethod.GET)
    public List<PartyData> getAllClients() throws ApiException {
        return orderDto.getAllClients();
    }

    @ApiOperation(value = "Gets a list of all Channels")
    @RequestMapping(path = "/channels", method = RequestMethod.GET)
    public List<ChannelData> getAllChannels() throws ApiException {
        return orderDto.getAllChannels();
    }

//    @ApiOperation(value = "Gets list of all Order-Items")
//    @RequestMapping(path = "/orderId/{orderId}", method = RequestMethod.GET)
//    public List<OrderItemData> getAllByOrderId(@PathVariable Long orderId) throws ApiException {
//        return orderDto.getByOrderId(orderId);
//    }
}