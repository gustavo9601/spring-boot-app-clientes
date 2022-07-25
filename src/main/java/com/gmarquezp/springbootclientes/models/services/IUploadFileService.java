package com.gmarquezp.springbootclientes.models.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface IUploadFileService {
    public Resource load(String fileName) throws MalformedURLException;

    public String store(MultipartFile file) throws IOException;

    public boolean delete(String fileName);

    public void deleteAll();

    public void initDirectory();
}
