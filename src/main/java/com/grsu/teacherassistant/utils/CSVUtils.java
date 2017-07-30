package com.grsu.teacherassistant.utils;

import com.grsu.teacherassistant.beans.LocaleBean;
import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.GroupDAO;
import com.grsu.teacherassistant.dao.StudentDAO;
import com.grsu.teacherassistant.entities.Department;
import com.grsu.teacherassistant.entities.Group;
import com.grsu.teacherassistant.entities.Student;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

import static com.grsu.teacherassistant.utils.FileUtils.CSV_EXTENSION;
import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.capitalize;

/**
 * This class parses provided *.csv files with group students and adds them to database if needed.
 * Example of *.csv file is named 'Group-Sample.csv' and can be found in resources/samples folder.
 * To be processed files must be located at ../tomcat_home/app_files/csv/.
 */

public class CSVUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVUtils.class);

	private static final char SEPARATOR = ';';

	public static List<Group> updateGroupsFromCSV() {
		List<Group> parsedGroups = new ArrayList<>();

		for (Group group : parseGroups()) {
			if (group.getStudents().isEmpty()) {
				LOGGER.info("Group [ " + group.getName() + " ] is empty and not added to database.");
				continue;
			}

			if (group.getDepartment().getName() != null) {
				Department department = null;
				List<Department> departments = EntityDAO.getAll(Department.class);
				for (Department d : departments) {
					if (group.getDepartment().getName().equals(d.getName())) {
						department = d;
						break;
					}
				}

				if (department == null) {
					department = group.getDepartment();
					EntityDAO.add(department);
				}

				group.setDepartment(department);
			}

			List<Student> students = new ArrayList<>(group.getStudents());
			Group groupFromDB = new GroupDAO().getByName(group.getName());
			if (groupFromDB != null) {
				LOGGER.info("Group [ " + group.getName() + " ] already exists. Updating...");
				group = groupFromDB;
			}
			group.getStudents().clear();
			EntityDAO.save(group);
			processStudents(group, students);
			LOGGER.info("Group [ " + group.getName() + " ] processed.");

			/* return processed groups with students */
			group.setStudents(students);
			parsedGroups.add(group);
		}
		return parsedGroups;
	}

	private static void processStudents(Group group, List<Student> students) {
		for (Student student : students) {
			Student studentFromDB = StudentDAO.getByCardUid(student.getCardUid());
			if (studentFromDB == null) {
				student.setGroups(new ArrayList<>());
				student.getGroups().add(group);
				EntityDAO.add(student);
			} else {
				studentFromDB.setLastName(student.getLastName());
				studentFromDB.setFirstName(student.getFirstName());
				studentFromDB.setPatronymic(student.getPatronymic());
				studentFromDB.setCardId(student.getCardId());
				student = studentFromDB;
				student.getGroups().add(group);
				EntityDAO.update(student);
			}
		}
	}

	private static CSVReader getReader(File file) {
		CSVReader reader = null;
		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(file), "Cp1251")
			);

			reader = new CSVReader(in, SEPARATOR);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return reader;
	}

	private static List<Group> parseGroups() {
		List<File> files = FileUtils.getCSVFilesFromAppFilesFolder();
		if (files.isEmpty()) {
			LOGGER.info("CSV files not found. Processing skipped.");
			return Collections.emptyList();
		}

		LOGGER.info("CSV files founded. Processing...");
		List<Group> groups = new ArrayList<>();
		for (File file : files) {
			Group group = parseGroup(file);
			if (group != null) {
				groups.add(group);
			}
		}
		return groups;
	}

	private static Group parseGroup(File file) {
		CSVReader reader = getReader(file);
		if (reader == null) return null;

		String fileName = file.getName();
		String groupName = fileName.substring(0, fileName.length() - CSV_EXTENSION.length());

		Group group = null;
		String departmentName = null;
		List<Student> students = new ArrayList<>();

		try {
			reader.readNext(); //skip first line, it contains only column headers
			String[] line;
			boolean departmentFound = false;

			while ((line = reader.readNext()) != null) {
				Student student = parseStudent(line);
				if (student != null) {
					students.add(student);

					if (!departmentFound && line[5] != null && !line[5].isEmpty()) {
						departmentName = line[5];
						departmentFound = true;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!students.isEmpty()) {
			group = new Group();
			group.setName(groupName);
			group.setStudents(students);
			Department department = new Department();
			department.setName(departmentName);
			group.setDepartment(department);
		} else {
			LOGGER.info("Group [ " + groupName + " ] is empty and will not be added to database.");
		}

		try {
			reader.close();
			if (file.delete()) {
				LOGGER.info("File [" + fileName + "] is deleted.");
			} else {
				LOGGER.info("Delete operation for file [" + fileName + "] is failed.");
			}
		} catch (IOException e) {
			LOGGER.info("Warning! Close stream for file [" + fileName + "] failed!");
		}

		return group;
	}

	private static Student parseStudent(String[] record) {
		if (record[0] == null || record[0].isEmpty()) {
			return null;
		}
		Student student = new Student();
		student.setLastName(capitalize(lowerCase(record[0])));
		student.setFirstName(capitalize(lowerCase(record[1])));
		student.setPatronymic(capitalize(lowerCase(record[2])));

		String parsedUid = record[3];
		if (parsedUid == null || parsedUid.isEmpty()) {
			LOGGER.info("No card ID for [ " + student.getFullName() + " ]. Need to update UID manually.");
			student.setCardUid("0");
		} else {
			student.setCardId(Integer.parseInt(record[3]));
		}
		return student;
	}
}
