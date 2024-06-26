package edu.it10.dangquangwatch.spring.controller;  

import edu.it10.dangquangwatch.spring.entity.Anhtrangsuc;
import edu.it10.dangquangwatch.spring.entity.Trangsuc;
import edu.it10.dangquangwatch.spring.service.AnhtrangsucService;
import edu.it10.dangquangwatch.spring.service.TrangsucService;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;  
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;  
import java.util.Optional;  

@Controller
@RequestMapping(path = "/admin/trangsuc")  
public class TrangsucController {  
  @Autowired 
  private TrangsucService trangsucService;  
  @Autowired
  private AnhtrangsucService anhtrangsucService;

  @GetMapping("/")  
  public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("search") Optional<String> search) {  
    int pageNum = 0;
    String searchStr = "";

    if (search.isPresent()) {
      searchStr = search.get();
    }

    if (page.isPresent()) {
      pageNum = page.get() - 1;
    }

    Page<Trangsuc> data = trangsucService.searchTrangsuc(searchStr, pageNum);
    List<Trangsuc> trangsucs = data.getContent();

    model.addAttribute("trangsucs", trangsucs);
    model.addAttribute("page", pageNum);
    model.addAttribute("search", searchStr);
    model.addAttribute("sotrang", data.getTotalPages()); 

    return "/admin/trangsuc/index";  
  }  

  @GetMapping(value = "/add")  
  public String addTrangsuc(Model model) {  
    model.addAttribute("trangsuc", new Trangsuc());  
    return "/admin/trangsuc/addTrangsuc";  
  }  

  @GetMapping(value = "/edit")  
  public String editTrangsuc(@RequestParam("id") Integer matrangsuc, Model model) {  
    Optional<Trangsuc> trangsucEdit = trangsucService.findTrangsucById(matrangsuc);  
    trangsucEdit.ifPresent(trangsuc -> model.addAttribute("trangsuc", trangsuc));  
    return "/admin/trangsuc/editTrangsuc";  
  }  

  @PostMapping(value = "/save")  
  public String save(Trangsuc trangsuc, @RequestParam("file") Optional<MultipartFile> fileData) throws IOException {
    MultipartFile file = null;

    if (fileData.isPresent())
      file = fileData.get();

    if (file != null && !file.isEmpty()) {
      Anhtrangsuc anhtrangsuc = new Anhtrangsuc();
      anhtrangsuc.setTrangsuc(trangsuc);
      anhtrangsuc.setFile(file);

      anhtrangsucService.saveAnhtrangsuc(anhtrangsuc);
    }
    trangsucService.saveTrangsuc(trangsuc);  
    return "redirect:/admin/trangsuc/";  
  }  

  @PostMapping("/uploadimage")
  public String uploadImage(@RequestParam("file") List<MultipartFile> files, @RequestParam("id") Integer matrangsuc,
      Model model) {
    Optional<Trangsuc> trangsuc = trangsucService.findTrangsucById(matrangsuc);

    trangsuc.ifPresent(dh -> {
      for (MultipartFile file : files) {
        Anhtrangsuc anhtrangsuc = new Anhtrangsuc();
        anhtrangsuc.setFile(file);
        anhtrangsuc.setTrangsuc(dh);

        try {
          anhtrangsucService.saveAnhtrangsuc(anhtrangsuc);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });

    return "redirect:/admin/trangsuc/edit?id=" + matrangsuc;
  }

  @GetMapping(value = "/delete")  
  public String deleteTrangsuc(@RequestParam("id") Integer matrangsuc, Model model) {  
    trangsucService.deleteTrangsuc(matrangsuc);  
    return "redirect:/admin/trangsuc/";  
  }  
}