package com.user.controller;

import com.movie.utils.AppFileUtils;
import com.movie.utils.DataGridView;
import com.movie.utils.Utils;
import com.movie.utils.argumentResolverConfig.ParameterConvert;
import com.movie.utils.validated.ValidGroup;
import com.user.domain.entity.SysUser;
import com.user.mapper.UserMapper;
import com.user.service.IUserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@Slf4j
@ApiOperation("用户操作接口")
@Validated
@RestController
@RequestMapping("/app/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserMapper userMapper;


    @ApiOperation("用户注册")
    @PostMapping("/register")
    @ParameterConvert
    public DataGridView register(@RequestBody @Validated({ValidGroup.Insert.class}) SysUser sysUser){
        return userService.userRegister(sysUser);
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    @ParameterConvert
    public DataGridView login(@RequestBody SysUser sysUser){
        return userService.userlogin(sysUser);
    }

    @ApiOperation("用户注销")
    @PostMapping("/logout")
    public DataGridView logout(){
        return userService.logout();
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/user_info")
    @ParameterConvert
    public DataGridView userInfo(){
        return userService.userInfo();
    }

    /**
     * 获取用户信息 服务间调用
     * @param username
     * @return
     */
    @GetMapping("/inner/user_info/{user_name}")
    public SysUser userInfo(@PathVariable("user_name") String username){
        return userMapper.getUserByName(username);
    }

    @ApiOperation("修改用户信息")
    @PutMapping("/user_info")
    @ParameterConvert
    public DataGridView userSetInfo(@Validated({ValidGroup.Update.class}) SysUser sysUser){
        if (!Objects.isNull(sysUser.getIcon())) {
            String fileName = AppFileUtils.updateFileName(sysUser.getIcon(), AppFileUtils.FILE_TEMP);
            sysUser.setIcon(fileName);
        }
        return userService.userSetInfo(sysUser);
    }

    @ApiOperation("钱包充值")
    @PutMapping("/purse")
    public DataGridView purse(Double overage){
        if (overage <=0 ) return Utils.resFailure(400, "充值失败!!!");
        return userService.purse(overage);
    }

    @ApiOperation("查询余额")
    @GetMapping("/purse")
    @ParameterConvert
    public DataGridView purse(){
        return userService.selectPurse();
    }

    /**
     * 查询余额 服务间调用
     * @return
     */
    @GetMapping("/inner/purse/{user_id}")
    public Double iPurse(@PathVariable("user_id") Integer userId){
        Double overage = userMapper.selectsetUserOverage(userId);
        return overage;
    }

    /**
     * 更改钱包余额 服务间调用
     */
    @PutMapping("/inner/set_purse/{overage}/{id}")
    public void setUserOverage(@PathVariable("overage") Double overage, @PathVariable("id") Integer id){
        try {
            userMapper.setUserOverage(overage, id);
        } catch (Exception e) {
            log.debug("修改钱包余额失败，出现异常！！！消息：{}，用户id:{}", e.getMessage(),id);
            throw new RuntimeException("删除失败，出现异常！！！", e);
        }
    }

}
