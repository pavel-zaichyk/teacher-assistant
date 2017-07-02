package com.grsu.teacherassistant.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


public class WebUtils {
	/**
	 * Opens web page at specified URI in new system default browser window
	 * https://www.mkyong.com/java/open-browser-in-java-windows-or-linux/
	 *
	 * alternative solution: http://stackoverflow.com/a/10967469
	 *
	 * @param url
	 */
	public static void openWebPage(String url) {
		String os = System.getProperty("os.name").toLowerCase();
		Runtime rt = Runtime.getRuntime();

		try {
			if (os.contains("win")) {
				// this doesn't support showing urls in the form of "page.html#nameLink"
				rt.exec("rundll32 url.dll,FileProtocolHandler " + url);

			} else if (os.contains("mac")) {
				rt.exec("open " + url);

			} else if (os.contains("nix") || os.contains("nux")) {
				// Do a best guess on unix until we get a platform independent way
				// Build a list of browsers to try, in this order.
				String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
						"netscape", "opera", "links", "lynx"};

				// Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
				// cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \"" + url + "\" ");
				StringBuilder cmd = new StringBuilder();
				for (int i = 0; i < browsers.length; i++) {
					cmd.append(i == 0 ? "" : " || ");
					cmd.append(browsers[i]).append(" \"");
					cmd.append(url);
					cmd.append("\" ");
				}

				rt.exec(new String[]{"sh", "-c", cmd.toString()});

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static URI buildURI(String protocol, String baseURL, String port, String relativeURL, List<NameValuePair> params) {
		URIBuilder uri = new URIBuilder();
		uri.setScheme(protocol);
		uri.setHost(baseURL);
		uri.setPort(Integer.valueOf(port));

		if (relativeURL != null && !relativeURL.isEmpty()) {
			uri.setPath("/" + relativeURL);
		}
		if (params != null) {
			uri.setParameters(params);
		}

		try {
			return uri.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static URI buildURI(String protocol, String baseURL, String port, String relativeURL) {
		return buildURI(protocol, baseURL, port, relativeURL, null);
	}

	public static URI buildURI(String protocol, String baseURL, String port) {
		return buildURI(protocol, baseURL, port, null, null);
	}

	public static URI buildURI(String url) {
		try {
			return new URIBuilder(url).build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static URI getDefaultPageURI() {
		return buildURI(
				PropertyUtils.getProperty("web.protocol"),
				PropertyUtils.getProperty("web.base.url"),
				PropertyUtils.getProperty("web.port"),
				PropertyUtils.getProperty("web.relative.url")
		);
	}

	public static void openDefaultPage() {
		openWebPage(getDefaultPageURI().toString());
	}
}
