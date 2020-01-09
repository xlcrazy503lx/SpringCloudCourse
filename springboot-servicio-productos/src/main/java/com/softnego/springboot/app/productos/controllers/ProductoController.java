package com.softnego.springboot.app.productos.controllers;

import com.softnego.springboot.app.productos.models.entity.Producto;
import com.softnego.springboot.app.productos.models.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("productos")
public class ProductoController {

    @Autowired
    private Environment env;

    @Autowired
    private IProductoService productoService;

    @Value("${server.port}")
    private Integer port;

    @GetMapping("/listar")
    public List<Producto> listar(){
        return productoService.findAll().stream().map(producto ->{
                producto.setPort(getPort());
                return producto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Producto detalle(@PathVariable Long id){
        Producto producto = productoService.findById(id);
        producto.setPort(getPort());
        return producto;
    }

    private Integer getPort() {
        return port;
    }
}