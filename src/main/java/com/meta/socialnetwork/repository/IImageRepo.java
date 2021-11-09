package com.meta.socialnetwork.repository;

import com.meta.socialnetwork.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IImageRepo extends JpaRepository<Image, Long> {
}
