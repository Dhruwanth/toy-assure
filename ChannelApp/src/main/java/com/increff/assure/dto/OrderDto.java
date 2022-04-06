package com.increff.assure.dto;

import com.increff.assure.model.data.ChannelOrderInvoiceData;
import com.increff.assure.service.ApiException;
import com.increff.assure.service.ClientWrapper;
import com.increff.assure.util.PdfGenerateUtil;
import com.increff.assure.util.XmlGenerateUtil;
import model.data.*;
import model.form.ChannelOrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Base64;

@Service
public class OrderDto {
    @Autowired
    private ClientWrapper clientWrapper;

    public void add(@RequestBody ChannelOrderForm orderForm) throws ApiException {
        clientWrapper.addOrder(orderForm);
    }

    public ChannelInvoiceResponse generateInvoice(OrderInvoiceData orderReceiptData) throws ApiException {
        ChannelOrderInvoiceData orderReceipt = clientWrapper.convert(orderReceiptData);
        ChannelInvoiceResponse invoice = new ChannelInvoiceResponse();
        invoice.setData(Base64.getEncoder().encodeToString(PdfGenerateUtil.generate(XmlGenerateUtil.generate(orderReceipt))));
        return invoice;
    }


    public List<PartyData> getAllClients() {
        return clientWrapper.getClients();
    }

    public List<ChannelData> getAllChannels() {
        return clientWrapper.getChannels();
    }

    public List<OrderData> getByChannel(Long channelId) throws ApiException {
        return clientWrapper.getOrdersByChannel(channelId);
    }
}
