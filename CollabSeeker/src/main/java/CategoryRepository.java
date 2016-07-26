
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Misan
 */
public interface CategoryRepository extends JpaRepository<String, Long>{
    
    String findById(Long id);
    
}
