package org.jdonee.cooking.mapper;

import org.apache.ibatis.annotations.Select;
import org.jdonee.cooking.domain.UserInfo;

public interface UserMapper {

	@Select("select *from t_user where account=#{account}")
	UserInfo findByAccount(String account);
}
