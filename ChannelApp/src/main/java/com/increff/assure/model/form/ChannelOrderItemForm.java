package com.increff.assure.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChannelOrderItemForm {
    @NotNull
    @Size(min=1, max=255)
    private String channelSkuId;

    @NotNull
    @Positive
    private Long orderedQuantity;
}
