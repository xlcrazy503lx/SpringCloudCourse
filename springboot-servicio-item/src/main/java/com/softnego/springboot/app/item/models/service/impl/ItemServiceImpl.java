package com.softnego.springboot.app.item.models.service.impl;

import com.softnego.springboot.app.item.models.Item;
import com.softnego.springboot.app.item.models.Producto;
import com.softnego.springboot.app.item.models.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("serviceRestTemplate")
public class ItemServiceImpl implements ItemService {

    @Autowired
    private RestTemplate clienteRest;
    String endPoint= "http://servicio-productos/";
    @Override
    public List<Item> findAll() {
        List<Producto> productos = Arrays.asList(clienteRest.getForObject(endPoint+"productos/listar",Producto[].class));
        return productos.stream().map(p ->
                new Item(p,1)).collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer cantidad) {
        Map<String,String> pathVariables = new HashMap<String,String>();
        pathVariables.put("id",id.toString());
        Producto producto = clienteRest.getForObject(endPoint+"productos/{id}",Producto.class,pathVariables);
        return new Item(producto,cantidad);
    }
}
