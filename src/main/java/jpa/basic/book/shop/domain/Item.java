package jpa.basic.book.shop.domain;

import javax.persistence.*;

@Entity
public class Item {
    @Id
    @GeneratedValue
    @Column(name = "ITEM_ID", nullable = false)
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
