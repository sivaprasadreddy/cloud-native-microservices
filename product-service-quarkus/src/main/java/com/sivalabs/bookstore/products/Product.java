package com.sivalabs.bookstore.products;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotEmpty(message = "Code is required")
    @Column(nullable = false, unique = true)
    public String code;

    @NotEmpty(message = "Name is required")
    @Column(nullable = false)
    public String name;

    public String description;

    @Column(name = "image_url")
    public String imageUrl;

    @NotNull(message = "Price is required")
    public BigDecimal price;
}
