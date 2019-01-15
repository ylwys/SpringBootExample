package com.pwrd.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_item")
public class Item extends BaseModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String itemName;

    public Item() {
    }

    public Item(long id, String itemName) {
        super(id);
        this.itemName = itemName;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
