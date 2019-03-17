package pl.edu.wszib.springwithtests.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wszib.springwithtests.dto.ProductDTO;
import pl.edu.wszib.springwithtests.model.Product;

import java.util.List;

@Repository
public interface ProductDao extends CrudRepository<Product, Integer> {

    List<Product> findAll();

   public void deleteById(ProductDTO integer);
}
