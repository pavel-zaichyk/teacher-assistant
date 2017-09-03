package com.grsu.teacherassistant.services;

import com.grsu.teacherassistant.utils.FileUtils;
import org.primefaces.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @author Pavel Zaychick
 */
public class StudentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

	private static final String PERSONNEL_NUMBER_URL = "http://api.grsu.by/1.x/app3/getStudentByCard?cardid=";
	private static final String PERSONNEL_NUMBER_NAME = "TN";
	private static final String STUDENT_PHOTO_URL = "https://intra.grsu.by/photos/";
	private static final String STUDENT_PHOTO_EXTENSION = ".jpg";

	public static String getPersonnelNumber(Integer cardId) {
		JSONObject json = readJsonFromUrl(PERSONNEL_NUMBER_URL + Integer.toString(cardId));
		if (json != null) {
			return (String) json.get(PERSONNEL_NUMBER_NAME);
		}
		return null;
	}

	public static boolean storeImage(String personnelNumber, String cardUid) {
		try {
			URL url = new URL(STUDENT_PHOTO_URL + personnelNumber + STUDENT_PHOTO_EXTENSION);
			BufferedImage image = ImageIO.read(url);

			ImageIO.write(image, "jpg", FileUtils.getFile(FileUtils.STUDENTS_PHOTO_FOLDER_PATH, cardUid, FileUtils.STUDENTS_PHOTO_EXTENSION));
			return true;
		} catch (IOException e) {
			LOGGER.info(STUDENT_PHOTO_URL + personnelNumber + STUDENT_PHOTO_EXTENSION);
			e.printStackTrace();
		}
		return false;
	}

	private static JSONObject readJsonFromUrl(String url) {
		try {
			InputStream is = new URL(url).openStream();
			try {
				BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
				String jsonText = readAll(rd).replaceAll("\\[", "").replaceAll("\\]", "");
				return new JSONObject(jsonText);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				is.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
}
