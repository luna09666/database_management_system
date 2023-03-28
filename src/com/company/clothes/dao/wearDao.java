package com.company.clothes.dao;

import com.company.clothes.bean.Clothes;
import com.company.clothes.bean.Wear;
import com.company.clothes.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class wearDao {
    QueryRunner runner = new QueryRunner();
    public Wear getWearById(int id, int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql="select * from wear where id=? and userid=?";
        Wear wear = runner.query(conn,sql,new BeanHandler<Wear>(Wear.class),id,userid);
        DBHelper.close(null, null, conn);
        return wear;
    }

    /**
     * 添加衣服进衣柜
     * @return
     * @throws Exception 
     */
    public int addWear(String title,String content,String pic,
                       int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql = "insert into wear (title,content,pic," +
                "userid,createtime,updatetime) values (?,?,?,?,?,?) on duplicate key update id = last_insert_id(id);";

        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        String time = sdf.format(date);

        int count = runner.update(conn,sql,title,content,pic,userid,time,time);
        DBHelper.close(null, null, conn);
        return count;

    }


    public int  getWearCount(int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql = "select count(id)from wear where userid = ?";
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
    public List<Wear> getWearByPage(int pageIndex,int pageSize,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        List<Wear> wears;
        String sql = "select * from wear where userid=? limit ?,?";
        int offset = (pageIndex-1)*pageSize;
        wears = runner.query(conn,sql,new BeanListHandler<Wear>(Wear.class),userid,offset,pageSize);
        DBHelper.close(null, null, conn);
        return wears;
    }




    //删除（根据id）
    public int removeWear(int id,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql="delete from wear where userid=? and id=? ";
        int count = runner.update(conn,sql,userid,id);
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
            wearDao wearDao = new wearDao();
            List<Wear> wears = wearDao.getWearByPage(1,3,1);
            System.out.println(wears.size());
	          for(Wear wear : wears){
	              System.out.println(wear);
                  System.out.println(wear.getCreatetime());
	          }

            //System.out.println(count);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

    }
}
