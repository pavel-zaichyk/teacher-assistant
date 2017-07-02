package com.grsu.teacherassistant.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.teacherassistant.utils.PropertyUtils.getProperty;

public class FileUtils {
	private static final ClassLoader classLoader = FileUtils.class.getClassLoader();
	public static final String separator = File.separator;

	private static final String TOMCAT_PATH = System.getProperty("catalina.base");
	private static final String APP_FILES_DIR_NAME = "app_files";
	private static final String WEBAPPS_DIR_NAME = "webapps";

	public static final String APP_FILES_PATH = buildPath(TOMCAT_PATH, APP_FILES_DIR_NAME);
	public static final String WEBAPPS_FILES_PATH = buildPath(TOMCAT_PATH, WEBAPPS_DIR_NAME);
	public static final String CONFIG_FILE_PATH = buildPath(APP_FILES_PATH, "config", "config.properties");

	public static final String CSV_FOLDER_PATH = buildPath(APP_FILES_PATH, "csv");
	public static final String CSV_EXTENSION = ".csv";

	public static final String DATABASE_PATH = buildPath(APP_FILES_PATH, "database", getProperty("db.name"));

	public static final String STUDENTS_PHOTO_FOLDER_PATH = buildPath(WEBAPPS_FILES_PATH, "photo", "students");
	public static final String STUDENTS_PHOTO_EXTENSION = ".jpg";
	/**
	 * Builds system-dependent path with specific separators
	 *
	 * @param separatorPrefix
	 * @param separatorPostfix
	 * @param args
	 */
	public static String buildPath(boolean separatorPrefix, boolean separatorPostfix, String... args) {
		StringBuilder sb = new StringBuilder();
		if (separatorPrefix) {
			sb.append(separator);
		}
		for (String arg : args) {
			sb.append(arg);
			sb.append(separator);
		}
		if (!separatorPostfix) {
			sb.setLength(sb.length() - separator.length());
		}
		return sb.toString();
	}

	/**
	 * Builds system-dependent path without trailing separators
	 *
	 * @param args
	 * @return
	 */
	public static String buildPath(String... args) {
		return buildPath(false, false, args);
	}

	/**
	 * Builds system-dependent absolute path to resource file
	 *
	 * @param name
	 * @param directories
	 */
	@Deprecated
	public static String getAbsolutePathToResourceFile(String name, String... directories) {
		StringBuilder fullPath = new StringBuilder(separator);
		for (String dir : directories) {
			fullPath.append(dir);
			fullPath.append(separator);
		}
		fullPath.append(name);
		return classLoader.getResource(fullPath.toString()).getPath();
	}

	public static List<File> getCSVFilesFromAppFilesFolder() {
		return getFilesFromFolder(CSV_FOLDER_PATH, CSV_EXTENSION);
	}

	public static List<File> getFilesFromFolder(String path, String extension) {
		List<File> files = new ArrayList<>();
		collectFilesFromFolder(new File(path), extension, files);
		return files;
	}

	public static File getFile(String path, String fileName, String extension) {
		String filePath = buildPath(path, fileName);
		return new File(filePath + extension);
	}

	private static void collectFilesFromFolder(final File folder, String extension, List<File> files) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				collectFilesFromFolder(fileEntry, extension, files);
			} else {
				if (extension != null && fileEntry.getName().endsWith(extension)) {
					files.add(fileEntry);
				} else if (extension == null) {
					files.add(fileEntry);
				}
			}
		}
	}
}
