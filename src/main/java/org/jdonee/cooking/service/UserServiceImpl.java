package org.jdonee.cooking.service;

import java.util.List;
import java.util.Set;

import org.jdonee.cooking.domain.ModuleInfo;
import org.jdonee.cooking.domain.UserInfo;
import org.jdonee.cooking.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private ModuleService moduleService;

	/**
	 * 根据账号Account查询当前用户
	 * 
	 * @param account
	 * @return
	 */
	@Override
	public UserInfo findByAccount(String account) {
		return userMapper.findByAccount(account);
	}

	/**
	 * 获取资源集合
	 * 
	 * @param account
	 * @return
	 */
	@Override
	public Set<String> findPermissions(String account) {
		Set<String> set = Sets.newHashSet();
		UserInfo user = findByAccount(account);
		List<ModuleInfo> modules = moduleService.findModuleByUserId(user.getId());

		for (ModuleInfo info : modules) {
			set.add(info.getModuleKey());
		}
		return set;
	}

	/**
	 * 获取URL权限
	 * 
	 * @param account
	 * @return
	 */
	@Override
	public List<String> findPermissionUrl(String account) {
		List<String> list = Lists.newArrayList();
		UserInfo user = findByAccount(account);
		List<ModuleInfo> modules = moduleService.findModuleByUserId(user.getId());

		for (ModuleInfo info : modules) {
			if (info.getModuleType() == ModuleInfo.URL_TYPE) {
				list.add(info.getModulePath());
			}
		}
		return list;
	}
}