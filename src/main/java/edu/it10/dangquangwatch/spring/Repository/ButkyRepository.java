package edu.it10.dangquangwatch.spring.repository;  

import edu.it10.dangquangwatch.spring.entity.Butky;  
import org.springframework.data.repository.CrudRepository;  
import org.springframework.stereotype.Repository;
import java.util.List;
  

@Repository  
public interface ButkyRepository extends CrudRepository<Butky, Integer> {
  List<Butky> findByTenbutky(String tenbutky);
}