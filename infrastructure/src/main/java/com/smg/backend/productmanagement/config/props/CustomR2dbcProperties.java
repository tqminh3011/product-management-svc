package com.smg.backend.productmanagement.config.props;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;

@Data
@EqualsAndHashCode(callSuper = false)
public class CustomR2dbcProperties extends R2dbcProperties {
    private String connection;
    private String host;
    private String database;

    private Integer queryTimeoutInMs;
}
