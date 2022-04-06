package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"clientId","clientSkuId"})}
)
public class ProductPojo extends BaseEntity{
    @TableGenerator(name = "Seq_Product", table = "Seq_Product_ID", pkColumnName = "Seq_product_Name", valueColumnName = "Seq_Product_Val", pkColumnValue = "Seq_Product_Colval", initialValue = 10000, allocationSize = 100)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Seq_Product")
    private Long id;

    @Column(nullable = false)
    private String clientSkuId;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brandId;

    @Column(nullable = false)
    private Double mrp;

    private String description;
}