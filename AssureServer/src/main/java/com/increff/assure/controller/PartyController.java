package com.increff.assure.controller;

import com.increff.assure.dto.PartyDto;
import com.increff.assure.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import model.data.PartyData;
import model.form.PartyForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/party") 
public class PartyController {
    @Autowired
    PartyDto partyDto;

    @ApiOperation(value = "Add a Party")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void add(@RequestBody PartyForm partyForm) throws ApiException {
        partyDto.add(partyForm);
    }

    @ApiOperation(value = "Gets list of all Parties")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<PartyData> getAll() throws ApiException {
        return partyDto.getAll();
    }
    @ApiOperation(value = "Gets a list of all Clients")
    @RequestMapping(path = "/clients", method = RequestMethod.GET)
    public List<PartyData> getAllClients() throws ApiException {
        return partyDto.getAllClients();
    }
    @ApiOperation(value = "Gets a list of all Customers")
    @RequestMapping(path = "/customers", method = RequestMethod.GET)
    public List<PartyData> getAllCustomers() throws ApiException {
        return partyDto.getAllCustomers();
    }
}