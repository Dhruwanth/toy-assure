package com.increff.assure.dto;

import com.increff.assure.pojo.PartyPojo;
import com.increff.assure.service.ApiException;
import com.increff.assure.service.PartyService;
import com.increff.assure.util.NormalizeUtil;
import model.PartyType;
import model.data.PartyData;
import model.form.PartyForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.increff.assure.util.ConvertUtil.convert;

@Service
public class PartyDto extends AbstractDto {
    @Autowired
    private PartyService partyService;

    public void add(PartyForm partyForm) throws ApiException {
    	checkValid(partyForm);
        NormalizeUtil.normalize(partyForm);
        partyService.add(convert(partyForm, PartyPojo.class));
    }

    public PartyData get(Long id) throws ApiException {
        return convert(partyService.getCheckId(id), PartyData.class);
    }

    public List<PartyData> getAll() throws ApiException {
        return convert(partyService.getAll(), PartyData.class);
    }

    public List<PartyData> getAllClients() throws ApiException {
        return convert(partyService.getAll(PartyType.CLIENT), PartyData.class);
    }

    public List<PartyData> getAllCustomers() throws ApiException {
        return convert(partyService.getAll(PartyType.CUSTOMER), PartyData.class);
    }
}