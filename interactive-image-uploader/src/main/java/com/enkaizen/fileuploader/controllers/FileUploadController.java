package com.enkaizen.fileuploader.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enkaizen.fileuploader.commons.FileUpload;
import com.enkaizen.fileuploader.config.MessagingConfig;
import com.enkaizen.fileuploader.consumer.Consumer;
import com.enkaizen.fileuploader.dto.Status;
import com.enkaizen.fileuploader.models.File;
import com.enkaizen.fileuploader.repositories.FileRepository;

@CrossOrigin("*")
@RestController
public class FileUploadController {
	@Autowired
	FileRepository fileRepository;
	@Autowired
	private RabbitTemplate template;
	@Autowired
	Consumer consumer;
	private static final Path PHOTO_URL = Paths.get("/home/aslam/SImages");

	@RequestMapping(value = "/upload/files")
	public String addtProductFile(@RequestParam("files") MultipartFile[] multipartFile) {
		Status status = new Status();
		for (MultipartFile file : multipartFile) {
			String imageCode = UUID.randomUUID().toString().replace("-", "");
			try {
				FileUpload.interactiveFileUpload(file, imageCode);
				fileRepository.save(new File(imageCode));
			} catch (IOException e) {
				status.setStatus("Fail");
				status.setMessage("File upload fail");
				template.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, status);
				return consumer.getMessage();
			}
		}
		status.setStatus("Success");
		status.setMessage("File upload success");
		template.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, status);
		return consumer.getMessage();
	}

	@MessageMapping(value = "/notification")
	@SendTo("/queue/" + MessagingConfig.QUEUE)
	public String getNotification() {
		return consumer.getMessage();
	}

	@RequestMapping(value = "/files")
	public ResponseEntity<List<File>> getAllFiles() {
		return ResponseEntity.ok().body(fileRepository.findAll());
	}

	@RequestMapping("/getPhotos/{photoCode}")
	public ResponseEntity<Resource> getAllPhotos(@PathVariable("photoCode") String photoCode) {

		Resource file = loadAllPhotos(photoCode);
		return ResponseEntity.ok().body(file);
	}

	public Resource loadAllPhotos(String photoCode) {
		try {
			Path file = PHOTO_URL.resolve(photoCode + ".jpeg");
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("FAIL!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("FAIL!");
		}
	}
}
