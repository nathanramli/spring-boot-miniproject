package com.nathan.miniproject.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HelloRequest implements Serializable {
    private static final long serialVersionUID = 6905489752333851853L;

    private Long id;
    private String hello;
}
