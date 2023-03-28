package com.company.clothes.dao;

import com.company.clothes.bean.Clothes;
import com.company.clothes.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class clothesDao {
    QueryRunner runner = new QueryRunner();


    /**
     * ????·??????
     * @return
     * @throws Exception 
     */
    public int addWardrobe(String name,String type,float price,String size,float waist,float len,
    		String color,String describe1,String specs,String pic,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql = "insert into wardrobe (name,type,price,size,waist,len,color,describe1,createtime,"
        		+ "updatetime,specs,pic,userid) values (?,?,?,?,?,?,?,?,?,?,?,?,?) on duplicate key update id = last_insert_id(id);";

        SimpleDateFormat sdf = new SimpleDateFormat();// ????????
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a?am/pm????
        Date date = new Date();// ?????????
        String time = sdf.format(date);

        int count = runner.update(conn,sql,name,type,price,size,waist,len,color,describe1,
        		time,time,specs,pic,userid);
        DBHelper.close(null, null, conn);
        return count;

    }
    

    /**
     * ???????·?
     * @throws Exception 
     */
    public int modifyWardrobe(int id,String name,String type,float price,String size,float waist,float len,String color,
    		String describe1,String specs,String pic,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql = "update wardrobe set name=?,type=?,price=?,size=?,len=?,waist=?,color=?,specs=?,describe1=?," +
                "updatetime=?,pic=? where id=? and userid=?";

        SimpleDateFormat sdf = new SimpleDateFormat();// ????????
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a?am/pm????
        Date date = new Date();// ?????????
        String time = sdf.format(date);

        int count = runner.update(conn,sql,name,type,price,size,len,waist,color,specs,describe1,time,pic,id,userid);
        DBHelper.close(null, null, conn);
        return count;
    }
  


    /**
     * ????id??????????
     * @param id
     * @return
     * @throws Exception 
     */
    public Clothes getWardrobeById(int id,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql="select * from wardrobe where id=? and userid=?";
        Clothes clothes = runner.query(conn,sql,new BeanHandler<Clothes>(Clothes.class),id,userid);
        DBHelper.close(null, null, conn);
        return clothes;
    }
    


    public int  getWardrobeCount(int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql = "select count(id)from wardrobe where userid = ?";
        Object data = runner.query(conn,sql,new ScalarHandler<>(),userid);
        int count = (int)((long)data);
        DBHelper.close(null, null, conn);
        return count;
    }

    public int getKeyCount(String key,String type_s,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        int count;
        if("name".equals(type_s)){
            String sql = "select count(id)from wardrobe where userid = ? and name=?";
            Object data = runner.query(conn,sql,new ScalarHandler<>(),userid,key);
            count = (int)((long)data);
        }
        else if("type".equals(type_s)){
            String sql = "select count(id)from wardrobe where userid = ? and type=?";
            Object data = runner.query(conn,sql,new ScalarHandler<>(),userid,key);
            count = (int)((long)data);
        }
        else if("id".equals(type_s)){
            String sql = "select count(id)from wardrobe where userid = ? and id=?";
            Object data = runner.query(conn,sql,new ScalarHandler<>(),userid,key);
            count = (int)((long)data);
        }
        else{
            String sql = "select count(id)from wardrobe where userid = ? and (id=? or type=? or name=?)";
            Object data = runner.query(conn,sql,new ScalarHandler<>(),userid,key,key,key);
            count = (int)((long)data);
        }
        DBHelper.close(null, null, conn);
        return count;
    }

    public List<Clothes> getWardrobeByPage(int pageIndex,int pageSize, String type,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        List<Clothes> cclothes;
        String typea = translate(type);
        if("all".equals(type)){
            String sql = "select * from wardrobe where userid=? limit ?,?";
            int offset = (pageIndex-1)*pageSize;
            cclothes = runner.query(conn,sql,new BeanListHandler<Clothes>(Clothes.class),userid,offset,pageSize);
        }
        else{
            String sql = "select * from wardrobe where type = ? and userid=? limit ?,?";
            int offset = (pageIndex-1)*pageSize;
            cclothes = runner.query(conn,sql,new BeanListHandler<Clothes>(Clothes.class),typea,userid,offset,pageSize);
        }
        DBHelper.close(null, null, conn);
        return cclothes;
    }

    public List<Clothes> getWardrobeByType(String type,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        List<Clothes> cclothes;
        if(type==null){
            String sql = "select * from wardrobe where userid=?";
            cclothes = runner.query(conn,sql,new BeanListHandler<Clothes>(Clothes.class),userid);
        }
        else{
            String typea = translate(type);
            if("all".equals(type)){
                String sql = "select * from wardrobe where userid=?";
                cclothes = runner.query(conn,sql,new BeanListHandler<Clothes>(Clothes.class),userid);
            }
            else{
                String sql = "select * from wardrobe where type = ? and userid=?";
                cclothes = runner.query(conn,sql,new BeanListHandler<Clothes>(Clothes.class),typea,userid);
            }
        }

        DBHelper.close(null, null, conn);
        return cclothes;
    }


    public List<Clothes> getAllWardrobe(int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        List<Clothes> cclothes;
        String sql = "select * from wardrobe where userid=?";
        cclothes = runner.query(conn,sql,new BeanListHandler<Clothes>(Clothes.class),userid);
        DBHelper.close(null, null, conn);
        return cclothes;
    }


    public List<Clothes> getWardrobeByKeyAndPage(String key,int userid,String type_s,int pageIndex,int pageSize) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql = null;
        List<Clothes> cclothes;
        if("name".equals(type_s)){
            sql="select * from wardrobe where name=? and userid=? limit ?,?";
            int offset = (pageIndex-1)*pageSize;
            cclothes = runner.query(conn,sql,new BeanListHandler<Clothes>(Clothes.class),key,userid,offset,pageSize);
        }
        else if("type".equals(type_s)){
            sql="select * from wardrobe where type=? and userid=? limit ?,?";
            int offset = (pageIndex-1)*pageSize;
            cclothes = runner.query(conn,sql,new BeanListHandler<Clothes>(Clothes.class),key,userid,offset,pageSize);
        }
        else if("id".equals(type_s)){
            sql="select * from wardrobe where id=? and userid=? limit ?,?";
            int offset = (pageIndex-1)*pageSize;
            cclothes = runner.query(conn,sql,new BeanListHandler<Clothes>(Clothes.class),key,userid,offset,pageSize);
        }
        else{
            sql="select * from wardrobe where id=? or name=? or type=? and userid=? limit ?,?";
            int offset = (pageIndex-1)*pageSize;
            cclothes = runner.query(conn,sql,new BeanListHandler<Clothes>(Clothes.class),key,key,key,userid,offset,pageSize);
        }
        DBHelper.close(null, null, conn);
        return cclothes;
    }


    public int removeWardrobe(int id,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql="delete from wardrobe where userid=? and id=? ";
        int count = runner.update(conn,sql,userid,id);
        DBHelper.close(null, null, conn);
        return count;
    }


    public int sellWardrobe(String name,String type,float buyprice,float sellprice,String selltime,
                            String size,float waist,float len,String color,
                            String describe1,String specs,String pic,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql="insert into sell (name,type,buyprice,sellprice,selltime,size,waist," +
                "len,color,describe1,createtime,updatetime,specs,pic,userid)" +
                " values (?,?,?,?,?,?,?,?,?,?,?,?) on duplicate key update id = last_insert_id(id);";

        SimpleDateFormat sdf = new SimpleDateFormat();// ????????
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a?am/pm????
        Date date = new Date();// ?????????
        String time = sdf.format(date);

        int count = runner.update(conn,sql,name,type,buyprice,sellprice,selltime,size,waist,
                len,color,describe1,time,time,specs,pic,userid);
        DBHelper.close(null, null, conn);
        return count;
    }

    public String translate(String name){
        String name_t;
        if("skirt".equals(name)){
            name_t = "格裙";
        }
        else if("shirt".equals(name)){
            name_t = "衬衫";
        }
        else if("little".equals(name)){
            name_t = "小物";
        }
        else if("sweater".equals(name)){
            name_t = "毛衣";
        }
        else if("vest".equals(name)){
            name_t = "马甲";
        }
        else if("suit".equals(name)){
            name_t = "西服";
        }
        else if("shoes".equals(name)){
            name_t = "鞋子";
        }
        else if("seawoman".equals(name)){
            name_t = "水手服";
        }
        else if("pureskirt".equals(name)){
            name_t = "纯色裙";
        }
        else if("dress".equals(name)){
            name_t = "连衣裙";
        }
        else{
            name_t = "其它";
        }
        return name_t;
    }



    public static void main(String[] args) {
        try {
            clothesDao clothesDao = new clothesDao();
            List<Clothes> cclothes = clothesDao.getWardrobeByPage(1,3,"skirt",1);
            System.out.println(cclothes.size());
	          for(Clothes clothes : cclothes){
	              System.out.println(clothes);
                  System.out.println(clothes.getCreatetime());
	          }
			Clothes clothes0 = clothesDao.getWardrobeById(2,1);
            System.out.println(clothes0);
            System.out.println(clothes0.getCreatetime());

            //System.out.println(count);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

    }
}
