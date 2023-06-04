package com.smg.backend.productmanagement.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("product_owner")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductOwnerEntity extends Auditable {
    @Id
    Long id;

    @Column("name")
    String name;

    @Column("contact")
    String contact;

    @Column("location")
    String location;

    @Version
    Integer version;
}
