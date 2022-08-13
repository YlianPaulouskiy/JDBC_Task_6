package edu.sql.practice.lesson6.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode
public class Product {

    private Long id;
    private String name;
    private String description;
    private Double price;

}
