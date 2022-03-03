package model.data;

import lombok.Getter;
import lombok.Setter;
import model.form.ProductForm;

@Getter
@Setter
public class ProductData extends ProductForm {
    private Long id;
    private Long clientId;
}
