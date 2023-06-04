package com.smg.backend.productmanagement.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@SuperBuilder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Auditable {
    
    @Column("created_date")
    @CreatedDate
    protected LocalDateTime createdDate;
    
    @Column("updated_date")
    @LastModifiedDate
    protected LocalDateTime updatedDate;

    @Column("creator")
    @CreatedBy
    protected String creator;

    @Column("updator")
    @LastModifiedBy
    protected String updator;
}