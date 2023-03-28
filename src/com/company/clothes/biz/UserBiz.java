package com.company.clothes.biz;

import com.company.clothes.bean.User;
import com.company.clothes.dao.UserDao;
import java.sql.SQLException;
import java.util.List;

public class UserBiz {
    UserDao userDao = new UserDao();
    public User getUser(String phonenumber, String password) throws Exception{
        User  user = null;
        try {
            user  = userDao.getUser(phonenumber, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }
    public int modifyPwd(int id, String password) throws Exception{
        int count = 0;
        try {
            count = userDao.modifyPwd(id,password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }

    public int addUser(String phonenumber, String password) throws Exception {
        int count=0;
        count=userDao.addUser(phonenumber,password);
        return count;
    }

    public int removeUser(int id) throws Exception {
        int count = 0;
        try {

            //2.删除
            count = userDao.removeUser(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }


    public int modifyUser(String name,String position, String sex,
                          String describe1,String pic, int id) throws Exception {
        int count=0;
        count=userDao.modifyUser(name,position,sex,describe1,pic,id);
        return count;
    }

    public int modifyUser(User user) throws Exception {
        int count=0;
        count=userDao.modifyUser(user.getName(),user.getPosition(),user.getSex(),user.getDescribe1()
                ,user.getPic(),user.getId());
        return count;
    }

    public User getUserById(int id) throws Exception {
        User user = null;
        try {
            user = userDao.getUserById(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return user;
    }

    public List<User> getUserByPage(int pageIndex, int pageSize) throws Exception {
        List<User> users;
        try {
            users = userDao.getUserByPage(pageIndex,pageSize);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return users;
    }

    public int  getUserPageCount(int pageSize) throws Exception {
        int pageCount = 0;
        try {
            //1.获取行数
            int rowCount = userDao.getUserCount();
            //2.根据行数得到页数,每页多少条
            pageCount =  (rowCount-1)/pageSize+1;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return pageCount;
    }

    public static void main(String[] args) throws Exception {
        User user = null;
        try {
            user = new UserDao().getUser("18273114857","12345");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(user);
    }
}
