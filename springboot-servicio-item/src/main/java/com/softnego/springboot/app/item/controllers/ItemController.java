package com.softnego.springboot.app.item.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.softnego.springboot.app.item.models.Item;
import com.softnego.springboot.app.item.models.Producto;
import com.softnego.springboot.app.item.models.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RefreshScope
@RestController
public class ItemController {

    @Autowired
    //@Qualifier("serviceFeign")
    @Qualifier("serviceRestTemplate")
    private ItemService itemService;

    @Value("${configuracion.texto}")
    private String texto;

    @GetMapping("/listar")
    public List<Item> listar(){
        return itemService.findAll();
    }

    @HystrixCommand(fallbackMethod = "metodoAlternativo")
    @GetMapping("/{id}/cantidad/{cantidad}")
    public Item listar(@PathVariable Long id, @PathVariable Integer cantidad){
        return itemService.findById(id,cantidad);
    }

    public Item metodoAlternativo( Long id, Integer cantidad){
        Item item = new Item();
        Producto producto = new Producto();
        item.setCantidad(cantidad);
        producto.setId(id);
        producto.setNombre("Sony");
        producto.setPrecio(500.00);
        item.setProducto(producto);
        return item;
    }

    @GetMapping("/config")
    public ResponseEntity<?> obtenerConfig(@Value("${server.port}") String puerto){
        Map<String,String> json = new HashMap<>();
        json.put("texto",texto);
        json.put("puerto",puerto);
        return new ResponseEntity<Map<String,String>>(json, HttpStatus.OK);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@RequestBody Producto producto){
        return itemService.save(producto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto editar(@RequestBody Producto producto,@PathVariable Long id){
        return itemService.update(producto,id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id){
        itemService.delete(id);
    }

}
