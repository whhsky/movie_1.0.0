package com.user.service.impl;

import com.movie.secutity.UserUtils;
import com.movie.secutity.domain.RedisUser;
import com.movie.utils.*;
import com.user.domain.entity.SysUser;
import com.user.mapper.UserMapper;
import com.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class IUserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 用户注册
     * @param sysUser
     * @return
     */

    @Override
    public DataGridView userRegister(SysUser sysUser) {
        try {
            if(!Objects.isNull(userMapper.getUserByName(sysUser.getUserName()))){
                return Utils.resFailure(400, "用户名已存在");
            }
            sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
            Date date = new Date();
            sysUser.setCreateTime(date);
            sysUser.setUpdateTime(date);
            userMapper.userRegister(sysUser);
            return Utils.resSuccess(200, "注册成功！", null);
        }catch (Exception e){
            return Utils.resFailure(400, "未知错误,注册失败!");
        }
    }

    /**
     * 用户登录
     * @param sysUser
     * @return
     */
    @Override
    public DataGridView userlogin(SysUser sysUser) {
        try {
            // 查询用户信息
            SysUser resultSysUser = userMapper.getUserByName(sysUser.getUserName());
            // 判断登录信息是否有效
            if(!passwordEncoder.matches(sysUser.getPassword(), resultSysUser.getPassword())){
                // 认证没通过
                return Utils.resFailure(400, "用户名或密码错误");
            }
            // 根据userid生产一个jwt
            String token = JWTUtils.generateToken(resultSysUser.getUserName(), resultSysUser.getId());
            // 包装userDetails对象存入redis
            UserDetails userDetails = new RedisUser(resultSysUser, new HashSet<>());
            // 过期时间
            long TokenExpiredTime = 10 * 24 * 60 * 60;
            // 把User信息存入redis userid+id作为key
            redisUtils.set("userid:" + resultSysUser.getId().toString(), userDetails);
            // 把token也存入redis userid+id+token作为key
            redisUtils.set("userid:" + resultSysUser.getId().toString() + "Token", token, TokenExpiredTime);
            // 返回token信息
            HashMap<String, String> map = new HashMap<>();
            map.put("token", token);
            map.put("user_name", resultSysUser.getUserName());
            map.put("icon", resultSysUser.getIcon());
            return Utils.resSuccess(200, "登录成功！", map);
        } catch (NullPointerException e){
            return Utils.resFailure(400, "用户名不存在,请前往注册！");
        } catch (Exception e) {
            log.error("登录出现错误！{}", e.getMessage());
            e.printStackTrace();
            return Utils.resFailure(500, "");
        }
    }

    /**
     * 退出登录
     * @return
     */
    @Override
    public DataGridView logout() {
        try {
            RedisUser redisUser = UserUtils.getLoginUser();
            // 从redis中根据用户id删除用户信息和token
            redisUtils.delete("userid:" + redisUser.getSysUser().getId(), "userid:" + redisUser.getSysUser().getId() + "Token");
            // 把SecurityContextHolder的Context()清楚
            SecurityContextHolder.clearContext();
//            SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken(UUID.randomUUID().toString(), "anonymousUser", AuthorityUtils.createAuthorityList(new String[]{"ROLE_ANONYMOUS"})));
            return Utils.resSuccess(200, "注销成功！", null);
        } catch (Exception e) {
            return Utils.resFailure(400, "注销失败！");
        }
    }

    /**
     * 获取用户信息
     * 1.需要经过认证才能获取用户信息，认证成功后会从redis获取用户信息存入到SecurityContextHolder.getContext()里面
     * 2.从SecurityContextHolder.getContext()里面获取用户信息
     * @return
     */
    @Override
    public DataGridView userInfo() {
        SysUser sysUser;
        try {
            RedisUser redisUser = UserUtils.getLoginUser();
            sysUser = redisUser.getSysUser();
            sysUser.setId(null);
            sysUser.setPassword(null);
            return Utils.resSuccess(200,"", sysUser);
        } catch (Exception e) {
            return Utils.resFailure(200,"出现错误，请重新登录后重试！");
        }
    }

    /**
     * 修改用户信息
     * 1.需要经过认证才能获取用户信息，认证成功后会从redis获取用户信息存入到SecurityContextHolder.getContext()里面
     * 2.从SecurityContextHolder.getContext()里面获取用户信息根据用户信息进行修改操作
     */
    @Override
    public DataGridView userSetInfo(SysUser sysUser) {
        try {
            // 获取已认证的用户信息
            RedisUser redisUser = UserUtils.getLoginUser();
            // 把userid设置为用户信息的userid
            sysUser.setId(redisUser.getSysUser().getId());
            sysUser.setUserName(redisUser.getSysUser().getUserName());
            sysUser.setUpdateTime(new Date());
            if (!Objects.isNull(sysUser.getEMail())){
                SysUser userByEMail = userMapper.getUserByEMail(sysUser.getEMail());
                if (!(Objects.isNull(userByEMail)) && !(userByEMail.getId().equals(sysUser.getId()))){
                    return Utils.resFailure(400, "该邮箱已被占用！");
                }
            }
            // 删除redis中的用户信息 下次查询用户信息直接从数据中查询并存到redis中
            redisUtils.delete("userid:" + redisUser.getSysUser().getId());
            // 从数据库中根据用户id更新用户信息
            userMapper.updateSysUser(sysUser);
            if (!Objects.isNull(sysUser.getIcon())) {
                String icon = redisUser.getSysUser().getIcon();
                // 删除头像图片
                AppFileUtils.deleteFileUsePath('/' + icon);
            }
            return Utils.resSuccess(200,"修改信息成功！", null);
        } catch (Exception e) {
            return Utils.resFailure(500,"出现错误，修改信息失败，请重新登录后重试！");
        }
    }



    /**
     * 钱包充值
     */
    @Override
    public DataGridView purse(Double overage) {
        try {
            RedisUser redisUser = UserUtils.getLoginUser();
            SysUser sysUser = redisUser.getSysUser();
            Double overage_ = userMapper.selectsetUserOverage(sysUser.getId());
            userMapper.setUserOverage(overage_ + overage, sysUser.getId());
            return Utils.resSuccess(200, "充值成功！", null);
        } catch (Exception e) {
            return Utils.resFailure(400, "未知错误,提交失败!");
        }
    }

    /**
     * 钱包查询
     * @return
     */
    @Override
    public DataGridView selectPurse() {
        try {
            RedisUser redisUser = UserUtils.getLoginUser();
            SysUser sysUser = redisUser.getSysUser();
            Double overage_ = userMapper.selectsetUserOverage(sysUser.getId());
            Map<String, Double> map = new HashMap<>();
            map.put("overage", overage_);
            return Utils.resSuccess(200, "提交成功",map);
        } catch (Exception e) {
            return Utils.resFailure(400, "未知错误,提交失败!");
        }
    }
}
