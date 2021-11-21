package com.psi.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.psi.user.pojo.User;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;

/****
 * @Author:ujiuye
 * @Description:User的Dao
 * @Date 2021/2/1 14:19
 *****/
public interface UserMapper extends BaseMapper<User> {

    /***
     * 增加用户积分
     * @param username
     * @param points
     * @return
     */
    @Update("update tb_user set points=points+#{points} where username=#{username}")
    int addUserPoints(@Param("username") String username, @Param("points") Integer points);
}
