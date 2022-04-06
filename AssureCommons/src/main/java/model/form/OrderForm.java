package model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
public class OrderForm {
    @NotNull
    private Long clientId;

    @NotNull
    private Long customerId;

    @NotNull
    private Long channelId;//TODO use channel name 

    @NotNull
    @Size(min=1, max=255)
    private String channelOrderId;

    @NotNull
    @NotEmpty
    private List<OrderItemForm> orderItemList;
}
