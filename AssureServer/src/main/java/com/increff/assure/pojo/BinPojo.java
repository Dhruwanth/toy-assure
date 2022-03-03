package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class BinPojo extends BaseEntity {
    @TableGenerator(name = "Seq_Bin", table = "Seq_Bin_ID", pkColumnName = "Seq_Bin_Name", valueColumnName = "Seq_Bin_Val",
            pkColumnValue = "Seq_Bin_Colval", initialValue = 1000, allocationSize = 100)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Seq_Bin")
    private Long id;
}
