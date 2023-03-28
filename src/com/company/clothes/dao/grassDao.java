package com.company.clothes.dao;

import com.company.clothes.bean.Clothes;
import com.company.clothes.bean.Grass;
import com.company.clothes.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class grassDao {
    QueryRunner runner = new QueryRunner();

    /**
     * 根据类型查询对应的衣柜服装信息
     * @param type
     * @return
     * @throws Exception
     */
    public List<Grass> getGrassByType(String type,int userid) throws Exception {
        Connection conn = DBHelper.getConnection();
        String sql = "select * from grass where type = ? and userid=?";
        List<Grass> grasses = runner.query(conn,sql,new BeanListHandler<Grass>(Grass.class),type,userid);
        DBHelper.close(null, null, conn);
        return grasses;

    }



    /**
     * 添加衣服进衣柜
     * @return
     * @throws Exception
     */
    public int addGrass(String name,String type,float price,String size,float waist,float len,
                           String color,String describe1,String specs,String pic,
                        float frontprice,float tailprice,String buytime,String flag,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql = "insert into grass (name,type,price,size,waist,len,color,describe1,createtime,"
                + "updatetime,specs,pic,frontprice,tailprice,buytime,flag,userid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) on duplicate key update id = last_insert_id(id);";

        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        String time = sdf.format(date);

        int count = runner.update(conn,sql,name,type,price,size,waist,len,color,describe1,
                time,time,specs,pic,frontprice,tailprice,buytime,flag,userid);
        DBHelper.close(null, null, conn);
        return count;

    }


    /**
     * 修改衣柜衣服
     * @throws Exception
     */
    public int modifyGrass(int id,String name,String type,float price,String size,float waist,float len,String color,
                              String describe1,String specs,String pic,float frontprice,float tailprice,String buytime,
                           String flag,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql = "update grass set name=?,type=?,price=?,size=?,len=?,waist=?,color=?,specs=?,describe1=?," +
                "updatetime=?,pic=?,frontprice=?,tailprice=?,buytime=?,flag=? where id=? and userid=?";

        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        String time = sdf.format(date);

        int count = runner.update(conn,sql,name,type,price,size,len,waist,color,specs,describe1,time,pic,frontprice,
                tailprice,buytime,flag,id,userid);
        DBHelper.close(null, null, conn);
        return count;
    }



    /**
     * 根据id查找衣柜服饰
     * @param id
     * @return
     * @throws Exception
     */
    public Grass getGrassById(int id,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql="select * from grass where id=? and userid=?";
        Grass grass = runner.query(conn,sql,new BeanHandler<Grass>(Grass.class),id,userid);
        DBHelper.close(null, null, conn);
        return grass;
    }


    /**
     * 获取衣柜服饰数量
     * @return
     * @throws Exception
     */
    public int  getGrassCount(int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql = "select count(id)from grass where userid=?";
        Object data = runner.query(conn,sql,new ScalarHandler<>(),userid);
        int count = (int)((long)data);
        DBHelper.close(null, null, conn);
        return count;
    }

    /**
     *页面查询
     * @param pageIndex 第几页,从1开始
     * @param pageSize 每页多少行
     * @return 当前页的信息
     * @throws Exception
     */
    public List<Grass>  getGrassByPage(int pageIndex,int pageSize, String type, String type_q_g,int userid)
            throws Exception {
        Connection conn  = DBHelper.getConnection();
        List<Grass> grasses;
        String typea = translate(type);
        if("front".equals(type_q_g)){
            if("all".equals(type)){
                String sql = "select * from grass where userid=? and (flag = ? or flag = ?) limit ?,?";
                int offset = (pageIndex-1)*pageSize;
                grasses = runner.query(conn,sql,new BeanListHandler<Grass>(Grass.class),userid,"预售 未付定金","预售 已付定金",offset,pageSize);
            }
            else{
                String sql = "select * from grass where userid=? and (flag = ? or flag = ?) and type = ? limit ?,?";
                int offset = (pageIndex-1)*pageSize;
                grasses = runner.query(conn,sql,new BeanListHandler<Grass>(Grass.class),userid,"预售 未付定金","预售 已付定金",typea,offset,pageSize);
            }
        }
        else if("present".equals(type_q_g)){
            if("all".equals(type)){
                String sql = "select * from grass where userid=? and flag = ? limit ?,?";
                int offset = (pageIndex-1)*pageSize;
                grasses = runner.query(conn,sql,new BeanListHandler<Grass>(Grass.class),userid,"现货",offset,pageSize);
            }
            else{
                String sql = "select * from grass where userid=? and flag = ? and type = ? limit ?,?";
                int offset = (pageIndex-1)*pageSize;
                grasses = runner.query(conn,sql,new BeanListHandler<Grass>(Grass.class),userid,"现货",typea,offset,pageSize);
            }
        }
        else{
            if("all".equals(type)){
                String sql = "select * from grass where userid=? limit ?,?";
                int offset = (pageIndex-1)*pageSize;
                grasses = runner.query(conn,sql,new BeanListHandler<Grass>(Grass.class),userid,offset,pageSize);
            }
            else{
                String sql = "select * from grass where where userid=? and type = ? limit ?,?";
                int offset = (pageIndex-1)*pageSize;
                grasses = runner.query(conn,sql,new BeanListHandler<Grass>(Grass.class),userid,typea,offset,pageSize);
            }
        }

        DBHelper.close(null, null, conn);
        return grasses;
    }

    public int getKeyCount(String key,String type_s,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        int count;
        if("name".equals(type_s)){
            String sql = "select count(id)from grass where userid = ? and name=?";
            Object data = runner.query(conn,sql,new ScalarHandler<>(),userid,key);
            count = (int)((long)data);
        }
        else if("type".equals(type_s)){
            String sql = "select count(id)from grass where userid = ? and type=?";
            Object data = runner.query(conn,sql,new ScalarHandler<>(),userid,key);
            count = (int)((long)data);
        }
        else if("id".equals(type_s)){
            String sql = "select count(id)from grass where userid = ? and id=?";
            Object data = runner.query(conn,sql,new ScalarHandler<>(),userid,key);
            count = (int)((long)data);
        }
        else{
            String sql = "select count(id)from grass where userid = ? and (id=? or type=? or name=?)";
            Object data = runner.query(conn,sql,new ScalarHandler<>(),userid,key,key,key);
            count = (int)((long)data);
        }
        DBHelper.close(null, null, conn);
        return count;
    }


    public List<Grass> getGrassByKeyAndPage(String key,int userid,String type_s,int pageIndex,int pageSize) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql = null;
        List<Grass> grasses;
        if("name".equals(type_s)){
            sql="select * from grass where name=? and userid=? limit ?,?";
            int offset = (pageIndex-1)*pageSize;
            grasses = runner.query(conn,sql,new BeanListHandler<Grass>(Grass.class),key,userid,offset,pageSize);
        }
        else if("type".equals(type_s)){
            sql="select * from grass where type=? and userid=? limit ?,?";
            int offset = (pageIndex-1)*pageSize;
            grasses = runner.query(conn,sql,new BeanListHandler<Grass>(Grass.class),key,userid,offset,pageSize);
        }
        else if("id".equals(type_s)){
            sql="select * from grass where id=? and userid=? limit ?,?";
            int offset = (pageIndex-1)*pageSize;
            grasses = runner.query(conn,sql,new BeanListHandler<Grass>(Grass.class),key,userid,offset,pageSize);
        }
        else{
            sql="select * from grass where id=? or name=? or type=? and userid=? limit ?,?";
            int offset = (pageIndex-1)*pageSize;
            grasses = runner.query(conn,sql,new BeanListHandler<Grass>(Grass.class),key,key,key,userid,offset,pageSize);
        }
        DBHelper.close(null, null, conn);
        return grasses;
    }

    //删除（根据id）
    public int removeGrass(int id,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql="delete from grass where userid=? and id=? ";
        int count = runner.update(conn,sql,userid,id);
        DBHelper.close(null, null, conn);
        return count;
    }



    public int buyGrass(String name, String type, float price, String size, float waist, float len, String color,
                        String describe1, String specs, String pic, String flag,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        int count = 0;
        String sql = "insert into wardrobe (name,type,price,size,waist,len,color,describe1,createtime,"
                    + "updatetime,specs,pic,userid) values (?,?,?,?,?,?,?,?,?,?,?,?,?) on duplicate key update id = last_insert_id(id);";

        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        String time = sdf.format(date);

        count = runner.update(conn,sql,name,type,price,size,waist,len,color,describe1,
                    time,time,specs,pic,userid);


        DBHelper.close(null, null, conn);
        return count;
    }

    public int payFrontGrass(int id, String flag,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        int count = 0;
        if("预售 未付定金".equals(flag)){
            String sql = "update grass set flag = '预售 已付定金' where id=? and userid=?";
            count = runner.update(conn,sql,id,userid);
        }
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
            grassDao grassDao = new grassDao();
            List<Grass> grasses = grassDao.getGrassByKeyAndPage("格裙",1,"type",1,10);
            int count = grassDao.getKeyCount("格裙","type",1);
            System.out.println(grasses.size());
            for(Grass grass : grasses){
                System.out.println(grass);
                System.out.println(grass.getLen());
            }
            System.out.println(count);

            //System.out.println(count);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

    }
}
