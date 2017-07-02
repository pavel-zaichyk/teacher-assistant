package com.grsu.teacherassistant.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ManagedBean(name = "localeBean")
@SessionScoped
public class LocaleBean implements Serializable {

	private Locale locale;

	private static Map<String, Object> locales;
	static {
		locales = new HashMap<>();
		locales.put("English", new Locale("en"));
		locales.put("Русский", new Locale("ru"));
	}

	public Map<String, Object> getAvailableLocales() {
		return locales;
	}

	public Locale getLocale() {
		return locale;
	}

	public String getLanguage() {
		return locale.getLanguage();
	}

	public void setLanguage(String language) {
		this.locale = new Locale(language);
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}

	@PostConstruct
	public void init() {
//		locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		locale = new Locale("ru");
		System.out.println("Using default locale: " + locale);
	}

	//value change event listener
//	public void languageChanged(ValueChangeEvent e){
//
//		String newLocaleValue = e.getNewValue().toString();
//
//		//loop country map to compare the locale code
//		for (Map.Entry<String, Object> entry : locales.entrySet()) {
//
//			if(entry.getValue().toString().equals(newLocaleValue)){
//
//				locale = (Locale)entry.getValue();
//				FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
//
//			}
//		}
//	}

//		//loop country map to compare the locale code
//		countries.entrySet().stream().filter(entry -> entry.getValue().toString().equals(newLocaleValue))
//				.forEach(entry -> {
//
//			FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());
//
//		});
//
//		//loop country map to compare the locale code
//		countries.entrySet().stream().filter(entry -> entry.getValue().toString().equals(newLocaleValue))
//				.forEach(entry ->
//						FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue()));
}
