package com.grsu.teacherassistant.utils;

import org.primefaces.context.RequestContext;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FacesUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FacesUtils.class);

	public static void addInfo(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public static void addInfo(String id, String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(id, message);
	}

	public static void addWarning(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public static void addWarning(String id, String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
		FacesContext.getCurrentInstance().addMessage(id, message);
	}

	public static void addError(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public static void addError(String id, String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
		FacesContext.getCurrentInstance().addMessage(id, message);
	}

	public static void update(String clientId) {
		RequestContext.getCurrentInstance().update(clientId);
	}

	public static void execute(String javascript) {
		RequestContext.getCurrentInstance().execute(javascript);
	}

	public static void closeDialog(String id) {
		execute("PF('" + id + "').hide();");
	}

    public static void showDialog(String id) {
        execute("PF('" + id + "').show();");
    }

	/**
	 * Warning: THEY DON'T LIKE NULLS AND STRING!
	 * Resource (push endpoint) may not work with nulls or strings
	 */
	public static void push(String channel, Object data) {
        LOGGER.info("==> push(); channel = " + channel + "; data = " + data);
		EventBus eventBus = EventBusFactory.getDefault().eventBus();
		eventBus.publish(channel, data);
	}

	public static Object getBean(String beanName) {
		return FacesContext.getCurrentInstance().getViewRoot().getViewMap().get(beanName);
	}
}
