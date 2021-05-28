package com.enkaizen.fileuploader.commons;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUpload {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUpload.class);
	private static final Path PRE_COMPRESSION_ABS_PATH = Paths.get("/home/aslam/PCSImages");
	static String profileImageCode = null;

	public static void interactiveFileUpload(MultipartFile profileImage, String pImageode) throws IOException {
		LOGGER.info("FROM class CarouselController,method: fileUpload()--ENTER--");
		profileImageCode = pImageode;
		FileImageOutputStream output;
		BufferedImage bufferedrealImage = null;
		BufferedImage resizedRealImage = null;
		Files.copy(profileImage.getInputStream(), PRE_COMPRESSION_ABS_PATH.resolve(profileImageCode + ".jpeg"));
		File forCompressionProfileImage = new File("/home/aslam/PCSImages/" + profileImageCode + ".jpeg");

		String prefix = "/home/aslam/PCSImages/";
		String[] ids = { profileImageCode };
		String ext = ".jpeg";
		Image[] images = new Image[ids.length];
		for (int i = 0; i < images.length; i++) {
			String path = prefix + ids[i] + ext;
			bufferedrealImage = ImageIO.read(new File(path));
		}
		resizedRealImage = scale(bufferedrealImage, 200, 200);
		OutputStream os = new FileOutputStream(forCompressionProfileImage);
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
		ImageWriter writer = (ImageWriter) writers.next();
		ImageOutputStream ios = ImageIO.createImageOutputStream(os);
		writer.setOutput(ios);
		ImageWriteParam param = writer.getDefaultWriteParam();
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(1.0F);
		File file = new File("/home/aslam/SImages/" + profileImageCode + ".jpeg");
		output = new FileImageOutputStream(file);
		writer.setOutput(output);
		writer.write(null, new IIOImage(resizedRealImage, null, null), param);
		os.close();
		ios.close();
		writer.dispose();
		File afterCompressionImageForDelete = new File("/home/aslam/PCSImages/" + profileImageCode + ".jpeg");
		afterCompressionImageForDelete.delete();
		LOGGER.info("FROM class CarouselController,method: fileUpload()--DELETED--" + profileImageCode + ".jpeg");

		File afterCompressionDeleteImageNullPacket = new File("/home/aslam/PCSImages/" + profileImageCode + ".jpeg");
		afterCompressionDeleteImageNullPacket.delete();
		LOGGER.info("FROM class CarouselController,method: fileUpload()--DELETED--" + "/home/aslam/PCSImages/"
				+ profileImageCode + ".jpeg");

		profileImageCode = null;
	}

	private static BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight) {
		BufferedImage scaledImage = null;
		if (imageToScale != null) {
			scaledImage = new BufferedImage(dWidth, dHeight, imageToScale.getType());
			Graphics2D graphics2D = scaledImage.createGraphics();
			graphics2D.drawImage(imageToScale, 0, 0, dWidth, dHeight, null);
			graphics2D.dispose();
		}
		return scaledImage;
	}

}