package com.increff.assure.dto;

import com.increff.assure.pojo.ChannelListingPojo;
import com.increff.assure.pojo.ChannelPojo;
import com.increff.assure.service.*;
import com.increff.assure.util.ConvertUtil;
import com.increff.assure.util.NormalizeUtil;
import model.data.ChannelData;
import model.form.ChannelForm;
import model.form.ChannelListingForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.increff.assure.util.ConvertUtil.convert;

@Service
public class ChannelDto extends AbstractDto {
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ChannelListingService channelListingService;
    @Autowired
    private PartyService partyService;

    public ChannelData get(Long id) throws ApiException {
        return convert(channelService.getCheckId(id), ChannelData.class);
    }

    public void add(ChannelForm channelForm) throws ApiException {
        checkValid(channelForm);
        NormalizeUtil.normalize(channelForm);

        channelService.add(convert(channelForm, ChannelPojo.class));
    }

    @Transactional(readOnly = true)
    public List<ChannelData> getAll() throws ApiException {
        return convert(channelService.getAll(), ChannelData.class);
    }

    public void addChannelListings(List<ChannelListingForm> formList, Long channelId, Long clientId) throws ApiException {
        channelService.getCheckId(channelId);
        partyService.getCheckClient(clientId);

        List<ChannelListingPojo> channelListingPojos = new ArrayList<>();
        for (ChannelListingForm listingForm : formList) {
            NormalizeUtil.normalize(listingForm);
            checkValid((listingForm));

            channelListingPojos.add(convertFormToPojo(listingForm, channelId, clientId));
        }
        channelListingService.addChannelListings(channelListingPojos);
    }

    public void validateFormList(List<ChannelListingForm> formList, Long channelId, Long clientId) throws ApiException {
        channelService.getCheckId(channelId);
        partyService.getCheckClient(clientId);

        StringBuilder errorDetailString = new StringBuilder();
        HashSet<String> channelSkus = new HashSet<>();
        HashSet<String> clientAndClientSkus = new HashSet<>();
        for (int index = 0; index < formList.size(); index++) {
            try {
                ChannelListingForm form = formList.get(index);
                checkValid(form);
                checkFalse(channelSkus.contains(form.getChannelSkuId()), "Duplicate Channel SKU present");
                checkFalse(clientAndClientSkus.contains(clientId + "," + form.getClientSkuId()), "Duplicate Client, ClientSKU present");

                channelSkus.add(form.getChannelSkuId());
                clientAndClientSkus.add(clientId + "," + form.getClientSkuId());
            } catch (ApiException e) {
                errorDetailString.append("Error in Line: ").append(index + 1).append(": ").append(e.getMessage()).append("<br \\>");
            }
        }
        checkFalse(errorDetailString.length() > 0, errorDetailString.toString());
    }

    private ChannelListingPojo convertFormToPojo(ChannelListingForm listingForm, Long channelId, Long clientId) throws ApiException {
        ChannelListingPojo listingPojo = ConvertUtil.convert(listingForm, ChannelListingPojo.class);
        listingPojo.setChannelId(channelId);
        listingPojo.setClientId(clientId);
        listingPojo.setGlobalSkuId(productService.getByClientAndClientSku(clientId, listingForm.getClientSkuId()).getId());
        return listingPojo;
    }

}