package com.enkaizen.fileuploader.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enkaizen.fileuploader.models.File;

public interface FileRepository extends JpaRepository<File, Long> {

}
