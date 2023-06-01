package com.smg.backend.productmanagement.repository.entity;

import com.smg.backend.productmanagement.enums.ProductActivity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("product_tracking")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductTrackingEntity {
    @Id
    Long id;

    @Column("product_id")
    String productId;

    @Column("product_owner_id")
    String productOwnerId;

    @Column("activity")
    ProductActivity activity;

    @Column("status")
    Status status;

    @Column("metadata")
    String metadata;

    public enum Status {
        SUCCESS,
        FAILED
    }
}
