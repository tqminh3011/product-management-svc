package com.smg.backend.productmanagement.repository.entity;

import com.smg.backend.productmanagement.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table("product")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductEntity extends Auditable {
    @Id
    Long id;

    @Column("external_id")
    String externalId;

    @Column("name")
    String name;

    @Column("type")
    ProductType type;

    @Column("product_owner_id")
    Long productOwnerId;

    @Column("price")
    BigDecimal price;

    @Column("active")
    boolean active;

    @Version
    Integer version;
}
