package com.grsu.teacherassistant;

import org.junit.Before;

import java.io.File;

/**
 * @author Pavel Zaychick
 */
public class TestConfiguration {
	@Before
	public void init() {
		System.setProperty("catalina.base", new File("src/test/resources").getAbsolutePath());
	}
}
