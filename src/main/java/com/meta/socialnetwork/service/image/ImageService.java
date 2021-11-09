package com.meta.socialnetwork.service.image;

import com.meta.socialnetwork.model.Image;
import com.meta.socialnetwork.repository.IImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageService implements IImageService{

    @Autowired
    IImageRepo imageRepo;
    @Override
    public Iterable<Image> findAll() {
        return null;
    }

    @Override
    public Optional<Image> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void save(Image image) {
        imageRepo.save(image);
    }

    @Override
    public void remove(Long id) {

    }
}
