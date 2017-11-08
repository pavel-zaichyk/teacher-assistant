package com.grsu.teacherassistant.beans.utility;

import com.grsu.teacherassistant.utils.FileUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Pavel Zaychick
 */
@ManagedBean
@SessionScoped
public class ImageBean implements Serializable {

	public String getImagePath(String cardUid) {
		if (Files.exists(Paths.get(FileUtils.STUDENTS_PHOTO_FOLDER_PATH, cardUid + FileUtils.STUDENTS_PHOTO_EXTENSION))) {
			return "/photo/students/" + cardUid + ".jpg";
		} else {
			return "/resources/images/noavatar.png";
		}
	}
}
