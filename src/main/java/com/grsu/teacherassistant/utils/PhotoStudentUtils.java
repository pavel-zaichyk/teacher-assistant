package com.grsu.teacherassistant.utils;

import com.grsu.teacherassistant.entities.Student;
import com.grsu.teacherassistant.services.StudentService;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pavel on 2/25/17.
 */
public class PhotoStudentUtils {
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
				System.out.println("Start load photo for " + student);
				if (StudentService.storeImage(personnelNumber, student.getCardUid())) {
					System.out.println("Photo load for " + student);
				} else {
					System.err.println("Photo not load for " + student);
				}
			}
		}
	}
}
