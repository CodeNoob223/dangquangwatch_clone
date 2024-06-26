package edu.it10.dangquangwatch.spring.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import edu.it10.dangquangwatch.spring.service.ImageUploadService;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {
  @Autowired
  Cloudinary cloudinary;

  @Override
  public Map<String, String> uploadImage(MultipartFile file) throws IOException {
    var uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
    return Map.of(
        "url", (String) uploadResult.get("url"),
        "public_id", (String) uploadResult.get("public_id"),
        "original_filename", (String) uploadResult.get("original_filename"));
  }
  
  @Override
  public List<Map<String, String>> uploadImages(List<MultipartFile> files) throws IOException {
    List<Map<String, String>> uploadResults = new ArrayList<>();
    for (MultipartFile file : files) {
      var uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
      uploadResults.add(Map.of(
          "url", (String) uploadResult.get("url"),
          "public_id", (String) uploadResult.get("public_id"),
          "original_filename", (String) uploadResult.get("original_filename")));
    }
    return uploadResults;
  }
}
