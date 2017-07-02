package com.grsu.teacherassistant.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;

import static com.grsu.teacherassistant.utils.FileUtils.CONFIG_FILE_PATH;

public class PropertyUtils {
	public static String getProperty(String name) {
		Properties props = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(CONFIG_FILE_PATH);
			props.load(input);

			return props.getProperty(name);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void setProperty(String name, String value) {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(CONFIG_FILE_PATH);
			Properties props = new Properties();
			props.load(in);
			in.close();

			out = new FileOutputStream(CONFIG_FILE_PATH);
			props.setProperty(name, value);

			// http://stackoverflow.com/a/17011319
			Properties tmp = new Properties() {
				@Override
				public synchronized Enumeration<Object> keys() {
					return Collections.enumeration(new TreeSet<Object>(super.keySet()));
				}
			};
			tmp.putAll(props);
			tmp.store(out, "Last updated at:");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
