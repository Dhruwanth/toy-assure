package com.increff.assure.service;

import com.increff.assure.dao.ChannelDao;
import com.increff.assure.pojo.ChannelPojo;
import model.InvoiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChannelService extends AbstractService {
    @Autowired
    private ChannelDao channelDao;

    @Transactional(rollbackFor = ApiException.class)
    public void add(ChannelPojo channelPojo) throws ApiException {
        channelDao.insert(channelPojo);
    }

    public List<ChannelPojo> getAll() {
        return channelDao.selectAll();
    }

    public ChannelPojo getCheckId(Long id) throws ApiException {
        ChannelPojo channelPojo = channelDao.select(id);
        checkNotNull(channelPojo, "Channel (ID:" + id + ") does not exist");
        return channelPojo;
    }

    public String getName(Long channelId) throws ApiException {
        return getCheckId(channelId).getName();
    }

    public InvoiceType getInvoiceType(Long channelId) throws ApiException {
        return getCheckId(channelId).getInvoiceType();
    }
}
