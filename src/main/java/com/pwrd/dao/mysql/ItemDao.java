package com.pwrd.dao.mysql;

import com.pwrd.model.Item;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ItemDao extends CrudRepository<Item, Long> {
    List<Item> getByItemName(String itemName);
}
