package com.increff.assure.controller;

import com.increff.assure.dto.BinDto;
import com.increff.assure.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import model.form.BinSkuForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/bin")
public class BinController {
    @Autowired
    private BinDto binDto;

    @ApiOperation(value = "Create specified number of Bins")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public List<Long> add(@RequestParam Integer numberOfBins) throws ApiException {
        return binDto.add(numberOfBins);
    }

    @ApiOperation(value = "Get list of all Bin IDs")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Long> getAll() {
        return binDto.getAllBins();
    }

    @ApiOperation(value = "Add a Bin Inventory List")
    @RequestMapping(path = "/binSku/list", method = RequestMethod.POST)
    public void addList(@RequestBody List<BinSkuForm> formList) throws ApiException {
        binDto.addBinWiseInventory(formList);
    }

}
