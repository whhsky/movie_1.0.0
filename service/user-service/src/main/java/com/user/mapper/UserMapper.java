package com.user.mapper;

import com.user.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    SysUser getUserByName(String UserName);
    SysUser getUserByEMail(String eMail);

    void userRegister( SysUser sysUser);

    void updateSysUser(SysUser sysUser);

    void setUserOverage(@Param("overage") Double overage, @Param("id") Integer id);

    Double selectsetUserOverage(Integer id);
}
