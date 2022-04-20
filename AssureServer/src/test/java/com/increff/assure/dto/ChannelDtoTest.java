package com.increff.assure.dto;

import com.increff.assure.dao.ChannelListingDao;
import com.increff.assure.dao.PartyDao;
import com.increff.assure.dao.ProductDao;
import model.PartyType;
import model.InvoiceType;
import com.increff.assure.service.ApiException;
import model.form.ChannelForm;
import model.form.ChannelListingForm;
import com.increff.assure.pojo.ChannelListingPojo;
import com.increff.assure.pojo.ChannelPojo;
import com.increff.assure.pojo.PartyPojo;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.service.ChannelService;
import com.increff.assure.util.AbstractUnitTest;
import com.increff.assure.util.DataUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ChannelDtoTest extends AbstractUnitTest {
    @Autowired
    private ChannelDto channelDto;

    @Autowired
    private PartyDao partyDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelListingDao listingDao;

    private ChannelForm channelForm;
    private PartyPojo partyPojo;
    private ProductPojo productPojo;
    private ChannelListingPojo channelListingPojo;
    private List<ChannelListingForm> channelListingForm;

    @Before
    public void init() {
        channelForm = DataUtil.createChannelForm("channel", InvoiceType.SELF);
        channelListingForm = new ArrayList<>();
        partyPojo = DataUtil.createPartyPojo("Member", PartyType.CLIENT);
        partyDao.insert(partyPojo);
        productPojo = DataUtil.createProductPojo("Product","brand","clientSku","description",123.2, partyPojo.getId());
        productDao.insert(productPojo);
        ChannelListingForm sku = DataUtil.createChannelListingForm(productPojo.getClientSkuId(),"channelSku1");
        channelListingForm.add(sku);
    }

    @Test
    public void testAddChannel() throws ApiException {
        channelDto.add(channelForm);
        assertEquals(1, channelService.getAll().size());
        ChannelPojo returned = channelService.getAll().get(0);
        assertEquals(channelForm.getName(), returned.getName());
        assertEquals(channelForm.getInvoiceType(), returned.getInvoiceType());
    }

    @Test(expected = ApiException.class)
    public void testAddChannelInvalid() throws ApiException {
        channelDto.add(channelForm);
        channelForm.setInvoiceType(InvoiceType.CHANNEL);
        channelDto.add(channelForm);
    }

    @Test
    public void testAddChannelDefault1() throws ApiException {
        channelForm.setInvoiceType(InvoiceType.SELF);
        channelForm.setName(null);
        channelDto.add(channelForm);
        assertEquals(1, channelService.getAll().size());
        ChannelPojo returned = channelService.getAll().get(0);
        assertEquals("INTERNAL", returned.getName());
        assertEquals(InvoiceType.SELF, returned.getInvoiceType());
    }

    @Test
    public void testGetAllChannel() throws ApiException {
        channelService.add(DataUtil.createChannelPojo("CHANNEL", InvoiceType.SELF));
        assertEquals(1, channelDto.getAll().size());
    }

    @Test
    public void testAddChannelListing() throws ApiException {
        ChannelPojo channel = DataUtil.createChannelPojo("Channel", InvoiceType.SELF);
        channelService.add(channel);
        channelDto.addChannelListings(channelListingForm,channelListingPojo.getChannelId(),channelListingPojo.getClientId());
        assertEquals(1,listingDao.selectAll().size());
        ChannelListingPojo returned=listingDao.selectAll().get(0);
        assertEquals(partyPojo.getId(),returned.getClientId());
        assertEquals(channel.getId(),returned.getChannelId());
        assertEquals(channelListingForm.get(0).getChannelSkuId(),returned.getChannelSkuId());
        assertEquals(productPojo.getId(),returned.getGlobalSkuId());
    }

    @Test(expected = ApiException.class)
    public void testAddChannelListingInvalid1() throws ApiException {
        ChannelPojo channel = DataUtil.createChannelPojo("Channel", InvoiceType.SELF);
        channelService.add(channel);
        List<ChannelListingForm> list=new ArrayList<>();
        list.add(DataUtil.createChannelListingForm(productPojo.getClientSkuId(),"invalid"));
        channelDto.addChannelListings(channelListingForm,channelListingPojo.getChannelId(),channelListingPojo.getClientId());
    }

    @Test
    public void testAddChannelListingSameListing() throws ApiException {
        ChannelPojo channel = DataUtil.createChannelPojo("Channel", InvoiceType.SELF);
        channelService.add(channel);
        channelDto.addChannelListings(channelListingForm,channelListingPojo.getChannelId(),channelListingPojo.getClientId());
        channelDto.addChannelListings(channelListingForm,channelListingPojo.getChannelId(),channelListingPojo.getClientId());
        assertEquals(1,listingDao.selectAll().size());
    }

    @Test(expected = ApiException.class)
    public void testAddChannelListingInvalid() throws ApiException {
        ChannelPojo channel = DataUtil.createChannelPojo("Channel", InvoiceType.SELF);
        channelService.add(channel);
        List<ChannelListingForm> list=new ArrayList<>();
        list.add(DataUtil.createChannelListingForm(productPojo.getClientSkuId(),"invalid"));
        channelDto.addChannelListings(channelListingForm,channelListingPojo.getChannelId(),channelListingPojo.getClientId());
    }

}
