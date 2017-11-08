package com.grsu.teacherassistant.beans.utility;

import com.grsu.teacherassistant.utils.WebUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(eager = true)
@ApplicationScoped
public class StartupBean {
	@PostConstruct
	public void init() {
		WebUtils.openDefaultPage();
	}
}
