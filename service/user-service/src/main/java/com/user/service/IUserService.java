package com.user.service;

import com.movie.utils.DataGridView;
import com.user.domain.entity.SysUser;

public interface IUserService {
    DataGridView userRegister(SysUser sysUser);
    DataGridView userlogin(SysUser sysUser);
    DataGridView logout();

    DataGridView userInfo();

    DataGridView userSetInfo(SysUser sysUser);

    DataGridView purse(Double overage);

    DataGridView selectPurse();
}
