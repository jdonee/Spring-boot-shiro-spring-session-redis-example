package org.jdonee.cooking.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class UserInfo implements Serializable {

	private static final long serialVersionUID = -2736075850489736894L;

	private int id;
	private String account;
	private String password;
	private String name;
	private Date createTime;

}