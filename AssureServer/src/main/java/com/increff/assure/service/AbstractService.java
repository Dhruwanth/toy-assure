package com.increff.assure.service;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AbstractService {
    public void checkNull(Object object, String message) throws ApiException {
        if (Objects.nonNull(object))
            throw new ApiException(message);
    }

    public void checkNotNull(Object object, String message) throws ApiException {
        if (Objects.isNull(object))
            throw new ApiException(message);
    }
}
