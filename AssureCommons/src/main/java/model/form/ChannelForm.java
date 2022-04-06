package model.form;

import lombok.Getter;
import lombok.Setter;
import model.InvoiceType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChannelForm {
    @NotNull
    @Size(min=1,max=255)
    private String name;

    @NotNull
    private InvoiceType invoiceType;
}
