package org.qfox.elasticsearch.document;

import java.math.BigDecimal;

/**
 * 产品
 *
 * @author Payne 646742615@qq.com
 * 2019/8/22 14:34
 */
public class Product {
    private String name;
    private BigDecimal price;

    public Product() {
    }

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
