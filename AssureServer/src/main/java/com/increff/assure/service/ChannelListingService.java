package com.increff.assure.service;

import com.increff.assure.dao.ChannelListingDao;
import com.increff.assure.pojo.ChannelListingPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ChannelListingService extends AbstractService {
    @Autowired
    private ChannelListingDao channelListingDao;
 
    public List<ChannelListingPojo> getAll() {
        return channelListingDao.selectAll();
    }

    public ChannelListingPojo getCheckId(Long id) throws ApiException {
        ChannelListingPojo listingPojo = channelListingDao.select(id);
        checkNotNull(listingPojo, "Channel Listing (ID:" + id + ") does not exist.");
        return listingPojo;
    }

    public void addChannelListings(List<ChannelListingPojo> channelListingPojos) throws ApiException {
        for (ChannelListingPojo pojo : channelListingPojos) {
        	ChannelListingPojo existingChannel = channelListingDao.selectByChannelIdChannelSkuAndClient(pojo.getChannelId(), pojo.getChannelSkuId(), pojo.getClientId());
            if (existingChannel != null) {
                if (existingChannel.getGlobalSkuId() == pojo.getGlobalSkuId())
                    return;

                throw new ApiException("Channel SKU:" + pojo.getChannelSkuId() + " is not valid");
            }

            channelListingDao.insert(pojo);
        }
    }

    public ChannelListingPojo getByChannelIdAndGlobalSku(Long channelId, Long globalSkuId) {
        return channelListingDao.selectByChannelAndGlobalSku(channelId, globalSkuId);
    }

    public ChannelListingPojo getByChannelChannelSkuAndClient(Long channelId, String channelSkuId, Long clientId) {
        return channelListingDao.selectByChannelIdChannelSkuAndClient(channelId, channelSkuId, clientId);
    }

    public Map<Long, String> getByChannelIdAndGlobalSkuIds(Long channelId, List<Long> globalSkuIds){

        List<ChannelListingPojo> list = channelListingDao.selectByChannelIdAndGlobalSkuIds(channelId, globalSkuIds);
        return list.stream().collect(Collectors.toMap(value->value.getGlobalSkuId(), value->value.getChannelSkuId()));
    }

}