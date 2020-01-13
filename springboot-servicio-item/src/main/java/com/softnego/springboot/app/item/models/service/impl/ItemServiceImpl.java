package com.softnego.springboot.app.item.models.service.impl;

import com.softnego.springboot.app.item.clients.ProductoClienteRest;
import com.softnego.springboot.app.item.models.Item;
import com.softnego.springboot.app.item.models.Producto;
import com.softnego.springboot.app.item.models.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
    String endPoint= "http://servicio-productos";
    @Override
    public List<Item> findAll() {
        List<Producto> productos = Arrays.asList(clienteRest.getForObject(endPoint+"/listar",Producto[].class));
        return productos.stream().map(p ->
                new Item(p,1)).collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer cantidad) {
        Map<String,String> pathVariables = new HashMap<String,String>();
        pathVariables.put("id",id.toString());
        Producto producto = clienteRest.getForObject(endPoint+"/{id}",Producto.class,pathVariables);
        return new Item(producto,cantidad);
    }

    @Override
    public Producto save(Producto producto) {
        HttpEntity<Producto> body = new HttpEntity<Producto>(producto);
        ResponseEntity<Producto> producto1 = clienteRest.exchange(endPoint+"/", HttpMethod.POST,body,Producto.class);
        return producto1.getBody();
    }

    @Override
    public Producto update(Producto producto, Long id) {
        HttpEntity<Producto> body = new HttpEntity<Producto>(producto);
        Map<String,String> pathVariables = new HashMap<String,String>();
        pathVariables.put("id",id.toString());
        ResponseEntity<Producto> producto1 = clienteRest.exchange(endPoint+"/{id}", HttpMethod.PUT,body,Producto.class,pathVariables);
        return producto1.getBody();
    }

    @Override
    public void delete(Long id) {
        Map<String,String> pathVariables = new HashMap<String,String>();
        pathVariables.put("id",id.toString());
        clienteRest.delete(endPoint+"/{id}",pathVariables);
    }
}
