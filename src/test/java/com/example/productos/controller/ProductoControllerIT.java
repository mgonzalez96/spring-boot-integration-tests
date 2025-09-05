package com.example.productos.controller;

import com.example.productos.domain.Producto;
import com.example.productos.repository.ProductoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductoControllerIT {
	
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductoRepository repository;
    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;

    @BeforeEach
    void setUp() {
    	try {
    		repository.deleteAll();
    		Producto p = new Producto();
            p = new Producto("Laptop Pro X1", new BigDecimal("15300856"), 20);
            repository.save(p).getId();
            p = new Producto("Monitor Curvo 271", new BigDecimal("3500632"), 45);
            repository.save(p).getId();
            p = new Producto("Teclado RGB Mecánico_", new BigDecimal("320652"), 110);
            repository.save(p).getId();
            p = repository.findByNombre("Teclado RGB Mecánico_").orElseThrow();
            existingId = p.getId();
            System.out.println("setUp: " + existingId);
		} catch (Exception e) {
			System.err.println("Error en setUp: " + e.getMessage());
		}        
    }

    @Test
    void listarProductosDevuelve200() throws Exception {
        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @SuppressWarnings("serial")
	@Test
    void crearProductoDevuelve201() throws Exception {
        var body = objectMapper.writeValueAsString(new java.util.HashMap<String, Object>() {

		{
            put("nombre", "Teclado RGB Mecánico");
            put("precio", "320652");
            put("stock", 110);
        }});
        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Teclado RGB Mecánico"));
    }

    @Test
    void obtenerProductoPorIdExistenteDevuelve200() throws Exception {
    	//Se comentarea esta linea cuando se crean los productos en @BeforeEach
    	//existingId = repository.findByNombre("Teclado RGB Mecánico").orElseThrow().getId();
    	System.out.println("obtenerProductoPorIdExistenteDevuelve200: " + existingId);
        mockMvc.perform(get("/productos/{id}", existingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId));
    }

    @Test
    void obtenerProductoInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/productos/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarProductoDevuelve204() throws Exception {
    	//Se comentarea esta linea cuando se crean los productos en @BeforeEach
    	//existingId = repository.findByNombre("Teclado RGB Mecánico").orElseThrow().getId();
    	System.out.println("eliminarProductoDevuelve204: " + existingId);
        mockMvc.perform(delete("/productos/{id}", existingId))
                .andExpect(status().isNoContent());
    }
}
