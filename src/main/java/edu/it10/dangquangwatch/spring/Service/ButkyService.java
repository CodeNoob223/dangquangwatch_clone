package edu.it10.dangquangwatch.spring.service;  

import edu.it10.dangquangwatch.spring.entity.Butky;  

import java.util.List;  
import java.util.Optional;

import org.springframework.data.domain.Page;  

public interface ButkyService {  
  List<Butky> getAllButky();
  
  Page<Butky> getAllButkyByTenbutky(String tenbutky, Integer pageNum);

  void saveButky(Butky butky);  

  void deleteButky(Integer mabutky);  

  Optional<Butky> findButkyById(Integer mabutky);  
}