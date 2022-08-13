package edu.sql.practice.lesson6.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
public class Order {

    private Long id;
    private Date date;
    private Map<Product,Long> products;

}
