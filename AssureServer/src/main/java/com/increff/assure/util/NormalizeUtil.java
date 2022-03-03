package com.increff.assure.util;

import com.increff.assure.pojo.PartyPojo;
import model.form.ChannelForm;
import model.form.ChannelListingForm;
import model.form.PartyForm;
import model.form.ProductForm;

public class NormalizeUtil {
    public static void normalize(PartyPojo partyPojo) {
        partyPojo.setName(StringUtil.toLowerCase(partyPojo.getName()));
    }

    public static void normalize(ChannelForm channelForm) {
        channelForm.setName(StringUtil.toLowerCase(channelForm.getName()));
    }

    public static void normalize(PartyForm partyForm) {
        partyForm.setName(StringUtil.toLowerCase(partyForm.getName()));
    }

    public static void normalize(ProductForm productForm) {
        productForm.setName(StringUtil.trimSpaces(productForm.getName()));
    }

    public static void normalize(ChannelListingForm listingForm) {
        listingForm.setChannelSkuId(StringUtil.trimSpaces(listingForm.getChannelSkuId()));
        listingForm.setClientSkuId(StringUtil.trimSpaces(listingForm.getClientSkuId()));
    }
}