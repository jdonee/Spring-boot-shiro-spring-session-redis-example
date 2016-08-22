package org.jdonee.cooking.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ModuleInfo implements Serializable {

	private static final long serialVersionUID = 2354275897681109437L;

	/** 路径 */
	public static final int URL_TYPE = 1;
	/** 功能点 */
	public static final int FUNCTION_TYPE = 2;

	private int id;
	private String moduleName;
	private String modulePath;
	private int moduleType;
	private String moduleKey;
	private Date createTime;
}