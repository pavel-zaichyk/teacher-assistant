package com.grsu.teacherassistant.models;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.*;

/**
 * @author Pavel Zaychick
 */
public class LazyStudentDataModel extends LazyDataModel<LessonStudentModel> {

	private List<LessonStudentModel> datasource;

	public LazyStudentDataModel(List<LessonStudentModel> datasource) {
		this.datasource = datasource;
	}

	@Override
	public LessonStudentModel getRowData(String rowKey) {
		for (LessonStudentModel student : datasource) {
			if (student.getId().toString().equals(rowKey))
				return student;
		}

		return null;
	}

	@Override
	public Object getRowKey(LessonStudentModel student) {
		return student.getId();
	}

	@Override
	public List<LessonStudentModel> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		List<LessonStudentModel> data = new ArrayList<>();

		//filter
		for (LessonStudentModel student : datasource) {
			boolean match = true;

			if (filters != null) {
				for (Iterator<String> it = filters.keySet().iterator(); it.hasNext(); ) {
					try {
						String filterProperty = it.next();
						Object filterValue = filters.get(filterProperty);
						String fieldValue = String.valueOf(student.getClass().getField(filterProperty).get(student));

						if (filterValue == null || fieldValue.toLowerCase().startsWith(filterValue.toString().toLowerCase())) {
							match = true;
						} else {
							match = false;
							break;
						}
					} catch (Exception e) {
						match = false;
					}
				}
			}

			if (match) {
				data.add(student);
			}
		}

		//sort
		if (sortField != null) {
			Collections.sort(data, new LazyStudentSorter(sortField, sortOrder));
		}

		//rowCount
		int dataSize = data.size();
		this.setRowCount(dataSize);

		//paginate
		if (dataSize > pageSize) {
			try {
				return data.subList(first, first + pageSize);
			} catch (IndexOutOfBoundsException e) {
				return data.subList(first, first + (dataSize % pageSize));
			}
		} else {
			return data;
		}
	}
}
