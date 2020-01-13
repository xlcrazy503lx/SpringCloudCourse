package com.softnego.springboot.app.item.models.service.impl;

import com.softnego.springboot.app.item.clients.ProductoClienteRest;
import com.softnego.springboot.app.item.models.Item;
import com.softnego.springboot.app.item.models.Producto;
import com.softnego.springboot.app.item.models.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("serviceFeign")
public class ItemServiceFeign implements ItemService {

    @Autowired
    private ProductoClienteRest productoClienteRest;

    @Override
    public List<Item> findAll() {
        return productoClienteRest.listar().stream().map(e ->
                new Item(e,1)).collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer cantidad) {
        return new Item(productoClienteRest.detalle(id),cantidad);
    }

    @Override
    public Producto save(Producto producto) {
        return null;
    }

    @Override
    public Producto update(Producto producto, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
