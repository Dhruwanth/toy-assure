package model.form;

import lombok.Getter;
import lombok.Setter;
import model.PartyType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PartyForm {
    @NotNull
    @Size(min=1, max=255)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PartyType type;
}
