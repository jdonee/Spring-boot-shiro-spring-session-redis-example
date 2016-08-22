package org.jdonee.cooking.service;

import java.util.List;

import org.jdonee.cooking.domain.ModuleInfo;

public interface ModuleService {
	/**
	 * 获取角色模块
	 * 
	 * @param userId
	 * @return
	 */
	List<ModuleInfo> findModuleByUserId(int userId);
}
