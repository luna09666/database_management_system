package com.company.clothes.dao;

import com.company.clothes.bean.User;
import com.company.clothes.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 创建用户
 */
public class UserDao {
	QueryRunner runner = new QueryRunner();
    public User getUser(String phonenumber, String password) throws Exception {

        Connection  conn = DBHelper.getConnection();
        String sql="select * from users where phonenumber=? and password=?";
        User user = runner.query(conn,sql,new BeanHandler<User>(User.class),phonenumber,password);

        DBHelper.close(null, null, conn);

        return user;
    }

    public List<User> getUserByPage(int pageIndex, int pageSize) throws Exception {
        Connection conn  = DBHelper.getConnection();
        List<User> users;
        String sql = "select * from users limit ?,?";
        int offset = (pageIndex-1)*pageSize;
        users = runner.query(conn,sql,new BeanListHandler<User>(User.class),offset,pageSize);
        DBHelper.close(null, null, conn);
        return users;
    }

    public int  getUserCount() throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql = "select count(id)from users";
        Object data = runner.query(conn,sql,new ScalarHandler<>());
        int count = (int)((long)data);
        DBHelper.close(null, null, conn);
        return count;
    }

    public User getUserById(int id) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql="select * from users where id=?";
        User user = runner.query(conn,sql,new BeanHandler<User>(User.class),id);
        DBHelper.close(null, null, conn);
        return user;
    }

    public int addUser(String phonenumber, String password) throws Exception {
        Connection conn  = DBHelper.getConnection();
        SimpleDateFormat sdf = new SimpleDateFormat();// ????????
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a?am/pm????
        java.util.Date date = new Date();// ?????????
        String time = sdf.format(date);

        String sql = "insert into users (phonenumber, password,createtime) values" +
                " (?,?,?) on duplicate key update id = last_insert_id(id);";

        int count = runner.update(conn,sql,phonenumber,password,time);
        DBHelper.close(null, null, conn);
        return count;
    }

    public int modifyUser(String name,String position, String sex,
                          String describe1,String pic, int id) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql = "update users set name=?,position=?,sex=?,describe1=?,pic=? where id=?";

        int count = runner.update(conn,sql,name,position,sex,describe1,pic,id);
        DBHelper.close(null, null, conn);
        return count;
    }


    /**
     * 修改密码
     * @param id  需要修改密码的用户编号
     * @param password  新的密码
     * @return  修改的数据行
     * @throws Exception 
     */
    public int modifyPwd(int id, String password) throws Exception {
        String sql="update users set password = ? where id=?";
        Connection conn = DBHelper.getConnection();
        int count = runner.update(conn,sql,password,id);
        DBHelper.close(null, null, conn);
        return count;
    }

    public int removeUser(int id) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql="delete from users where id=? ";
        int count = runner.update(conn,sql,id);
        DBHelper.close(null, null, conn);
        return count;
    }



    public static void main(String[] args) throws Exception {
        int count = new UserDao().modifyPwd(1,"12345");
        System.out.println(count);
    }

}
