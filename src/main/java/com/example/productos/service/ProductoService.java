package com.example.productos.service;

import com.example.productos.domain.Producto;
import com.example.productos.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> listar() {
        return repository.findAll();
    }

    /**
	 * @Usuario Sergio
	 * @Descripcion Adiciona validacion de nombre no null, valores no iguales a cero y
	 *  que producto no exista	
	 */
    public Producto crear(String nombre, BigDecimal precio, Integer stock) {
    	if(nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("El nombre no puede estar vacío");    		
    	}
        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo o igual a cero");
        }
        if (stock <= 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo o igual a cero");
        }
        if(repository.findByNombre(nombre).isPresent()) {
			throw new IllegalArgumentException("El producto con nombre " + nombre + " ya existe");
        }
        Producto p = new Producto(nombre, precio, stock);
        return repository.save(p);
    }
    
    /**
	 * @Usuario Sergio
	 * @Descripcion Edita los datos de un producto, validacion de nombre no null, valores no iguales a cero y
	 *  que producto exista	
	 */
    public Producto editar(String nombre, BigDecimal precio, Integer stock) {
    	if(nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("El nombre no puede estar vacío");    		
    	}
        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo o igual a cero");
        }
        if (stock <= 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo o igual a cero");
        }
        if(!repository.findByNombre(nombre).isPresent()) {
        	Producto p = new Producto(nombre, precio, stock);            
            return repository.save(p);        	
        }
        else {
        	throw new IllegalArgumentException("El producto con nombre " + nombre + " no existe");
        }
    }

    public Producto obtenerPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Producto no encontrado: " + id));
    }
    
    /**
	 * @Usuario Sergio
	 * @Descripcion Obtiene un producto por su nombre
	 */
    public Producto obtenerPorNombre(String nombre) {
        return repository.findByNombre(nombre).orElseThrow(() -> new NotFoundException("Producto no encontrado: " + nombre));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Producto no encontrado: " + id);
        }
        repository.deleteById(id);
    }
}
