package com.increff.assure.dao;

import com.increff.assure.pojo.ChannelPojo;
import org.springframework.stereotype.Repository;

@Repository
public class ChannelDao extends AbstractDao<ChannelPojo> {
    ChannelDao() {
        super(ChannelPojo.class);
    }

}