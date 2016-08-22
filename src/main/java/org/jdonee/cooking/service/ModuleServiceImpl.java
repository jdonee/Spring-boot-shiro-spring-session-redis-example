package org.jdonee.cooking.service;

import java.util.List;

import org.jdonee.cooking.domain.ModuleInfo;
import org.jdonee.cooking.mapper.ModuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleServiceImpl implements ModuleService {

	@Autowired
	private ModuleMapper moduleMapper;

	/**
	 * 获取角色模块
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public List<ModuleInfo> findModuleByUserId(int userId) {
		return moduleMapper.findModuleByUserId(userId);
	}
}
