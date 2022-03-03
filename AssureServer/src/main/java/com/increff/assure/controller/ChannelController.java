package com.increff.assure.controller;

import com.increff.assure.dto.ChannelDto;
import com.increff.assure.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import model.data.ChannelData;
import model.data.ChannelListingData;
import model.form.ChannelForm;
import model.form.ChannelListingForm;
import model.form.ChannelListingSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/channel")
public class ChannelController {
    @Autowired
    private ChannelDto channelDto;

    @ApiOperation(value = "Add a Channel")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void add(@RequestBody ChannelForm form) throws ApiException {
        channelDto.add(form);
    }

    @ApiOperation(value = "Get list of all channels")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<ChannelData> getAll() throws ApiException {
        return channelDto.getAll();
    }

    @ApiOperation(value = "Add a Channel Listings for given Channel and Client")
    @RequestMapping(path = "/channelListing/list/{channelId}/{clientId}", method = RequestMethod.POST)
    public void addList(@RequestParam Long channelId, @RequestParam Long clientId,
                        @RequestBody List<ChannelListingForm> formList) throws ApiException {
        channelDto.addChannelListing(formList, channelId, clientId);
    }

    @ApiOperation(value = "Validate List of Channel Listings for a Channel")
    @RequestMapping(path = "/channelListing/validate/{channelId}/{clientId}", method = RequestMethod.POST)
    public void validateList(@RequestParam Long channelId, @RequestParam Long clientId,
                             @RequestBody List<ChannelListingForm> formList) throws ApiException {
        channelDto.validateFormList(formList, channelId, clientId);
    }

    @ApiOperation(value = "Search Channel Listing Entries")
    @RequestMapping(path = "/channelListing/search", method = RequestMethod.POST)
    public List<ChannelListingData> getSearch(@RequestBody ChannelListingSearchForm form) throws ApiException {
        return channelDto.getSearch(form);
    }
}