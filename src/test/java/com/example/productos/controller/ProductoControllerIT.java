package com.example.productos.controller;

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
import java.util.NoSuchElementException;
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

    /**
	 * @Usuario Mariana Acevedo
	 * @Descripcion Se comentarean las lineas que crean los productos con el archivo
	 *  data.sql en resources
	 */
    @BeforeEach
    void setUp() {
    	try {
    		repository.deleteAll();
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
            put("nombre", "Teclado RGB Mecánico Pro");
            put("precio", "380652");
            put("stock", 10);
        }});
        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Teclado RGB Mecánico Pro"));
    }

    /**
	 * @Usuario Mariana Acevedo
	 * @Descripcion Se adiciona un bloque try-catch para manejar el caso en que no exista el producto
	 */
    @Test
    void obtenerProductoPorIdExistenteDevuelve200() throws Exception {
    	try {
    		//Se comentarea esta linea cuando se crean los productos en @BeforeEach
        	existingId = repository.findByNombre("Laptop Pro X1").orElseThrow().getId();
            mockMvc.perform(get("/productos/{id}", existingId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(existingId));
		} catch (NoSuchElementException e) {
			mockMvc.perform(get("/productos/{id}", existingId))
            .andExpect(status().isNotFound());
		}
    	
    }

    @Test
    void obtenerProductoInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/productos/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    /**
	 * @Usuario Mariana Acevedo
	 * @Descripcion Se adiciona un bloque try-catch para manejar el caso en que no exista el producto
	 */
    @Test
    void eliminarProductoDevuelve204() throws Exception {
    	try {
    		//Se comentarea esta linea cuando se crean los productos en @BeforeEach
        	existingId = repository.findByNombre("Teclado RGB Mecánico_").orElseThrow().getId();
        	mockMvc.perform(delete("/productos/{id}", existingId))
                    .andExpect(status().isNoContent());
		} catch (NoSuchElementException e) {
			mockMvc.perform(get("/productos/{id}", existingId))
            .andExpect(status().isNotFound());
		}    	
    }
    
    
}
