package cn.com.edtechhub.workopenapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.com.edtechhub.workopenapi.model.entity.User;
import cn.com.edtechhub.workopenapi.service.UserService;
import cn.com.edtechhub.workopenapi.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author ljp
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2025-07-08 10:55:06
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




