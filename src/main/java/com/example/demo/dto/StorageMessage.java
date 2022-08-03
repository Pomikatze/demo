package com.example.demo.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class StorageMessage implements Serializable {

    OrganizationDto organization;

    List<OrderDto> order;
}
