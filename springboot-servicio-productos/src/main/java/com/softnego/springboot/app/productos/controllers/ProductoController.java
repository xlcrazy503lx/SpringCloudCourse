package com.softnego.springboot.app.productos.controllers;

import com.softnego.springboot.app.productos.models.entity.Producto;
import com.softnego.springboot.app.productos.models.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
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
    public Producto detalle(@PathVariable Long id) //throws Exception
    {
        Producto producto = productoService.findById(id);
        producto.setPort(getPort());
        /*
        *Por si Hay fallo para detectar hystrix en el servicio item
        *
        * boolean ok =false;
        if (!ok){
          throw new Exception("No se pudo cargar el producto ");
        }*/
        //Thread.sleep(2000L);
        return producto;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crearProducto(@RequestBody Producto producto){
        return productoService.save(producto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto editarProducto(@RequestBody Producto producto, @PathVariable Long id){
        Producto productoDb = productoService.findById(id);
        productoDb.setNombre(producto.getNombre());
        productoDb.setPrecio(producto.getPrecio());
        return productoService.save(productoDb);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarProducto(@PathVariable Long id){
        productoService.deleteById(id);
    }

    private Integer getPort() {
        return port;
    }
}
