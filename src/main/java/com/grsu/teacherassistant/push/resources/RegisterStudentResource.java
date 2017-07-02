package com.grsu.teacherassistant.push.resources;

import com.grsu.teacherassistant.entities.Student;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

@PushEndpoint("/register")
public class RegisterStudentResource {

	@OnMessage(encoders = {JSONEncoder.class})
	public Student onMessage(Student student) {
		Student st = new Student();
		st.setCardId(student.getCardId());
		st.setCardUid(student.getCardUid());
		st.setLastName(student.getLastName());
		st.setFirstName(student.getFirstName());
		return st;
	}
}
