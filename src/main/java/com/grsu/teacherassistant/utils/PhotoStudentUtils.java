package com.grsu.teacherassistant.utils;

import com.grsu.teacherassistant.entities.Student;
import com.grsu.teacherassistant.services.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pavel Zaychick
 */
public class PhotoStudentUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(PhotoStudentUtils.class);

	public static void storeStudentsPhoto(List<Student> students) {
		List<File> photos = FileUtils.getFilesFromFolder(FileUtils.STUDENTS_PHOTO_FOLDER_PATH, FileUtils.STUDENTS_PHOTO_EXTENSION);
		List<String> photoName = photos.stream().map(f -> f.getName().replaceAll("\\" + FileUtils.STUDENTS_PHOTO_EXTENSION, "")).collect(Collectors.toList());

		for (Student student : students) {
			if (!photoName.contains(student.getCardUid())) {
				if (student.getCardId() == null || student.getCardUid() == null) {
					System.err.println("Card uid or card id for " + student + " not set.");
					continue;
				}
				String personnelNumber = StudentService.getPersonnelNumber(student.getCardId());
				if (personnelNumber == null) {
					System.err.println("Personnel Number for " + student + " not load.");
					continue;
				}
				LOGGER.info("Start load photo for " + student);
				if (StudentService.storeImage(personnelNumber, student.getCardUid())) {
					LOGGER.info("Photo load for " + student);
				} else {
					System.err.println("Photo not load for " + student);
				}
			}
		}
	}
}
