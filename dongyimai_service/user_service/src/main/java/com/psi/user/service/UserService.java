package com.psi.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.psi.entity.PageResult;
import com.psi.user.pojo.Address;
import com.psi.user.pojo.User;

import java.util.List;

/****
 * @Author:ujiuye
 * @Description:User业务层接口
 * @Date 2021/2/1 14:19
 *****/

public interface UserService extends IService<User> {

    /***
     * User多条件分页查询
     * @param user
     * @param page
     * @param size
     * @return
     */
    PageResult<User> findPage(User user, int page, int size);

    /***
     * User分页查询
     * @param page
     * @param size
     * @return
     */
    PageResult<User> findPage(int page, int size);

    /***
     * User多条件搜索方法
     * @param user
     * @return
     */
    List<User> findList(User user);

    /***
     * 删除User
     * @param id
     */
    void delete(Long id);

    /***
     * 修改User数据
     * @param user
     */
    void update(User user);

    /***
     * 新增User
     * @param user
     */
    void add(User user);

    /**
     * 根据ID查询User
     *
     * @param id
     * @return
     */
    User findById(Long id);

    /***
     * 查询所有User
     * @return
     */
    List<User> findAll();

    /***
     * 生成短信验证码
     * @param phone
     */
    void createSmsCode(String phone);


    /***
     * 判断验证码是否正确
     * @param phone
     * @param code
     * @return
     */
    boolean checkSmsCode(String phone, String code);

    /***
     * 根据用户名查找用户
     * @param username
     * @return
     */
    User findByUsername(String username);


    /***
     * 增加用户积分
     * @param username
     * @param points
     * @return
     */
    int addUserPoints(String username, Integer points);

}
