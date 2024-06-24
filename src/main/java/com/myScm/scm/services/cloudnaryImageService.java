package com.myScm.scm.services;


import org.springframework.web.multipart.MultipartFile;


public interface cloudnaryImageService {

public String uploadOnCloud(MultipartFile file , String fileName);

public String getUrlOfImage(String publicId);

}
