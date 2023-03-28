package com.company.clothes.dao;

import com.company.clothes.bean.Sell;
import com.company.clothes.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class sellDao {
    QueryRunner runner = new QueryRunner();

    /**
     * �������Ͳ�ѯ��Ӧ���¹��װ��Ϣ
     * @param type
     * @return
     * @throws Exception
     */
    public List<Sell> getSellByType(String type,int userid) throws Exception {
        Connection conn = DBHelper.getConnection();
        String sql = "select * from sell where type = ? and userid=?";
        List<Sell> sells = runner.query(conn,sql,new BeanListHandler<Sell>(Sell.class),type,userid);
        DBHelper.close(null, null, conn);
        return sells;

    }

    /**
     * ����·����¹�
     * @return
     * @throws Exception
     */
    public int addSell(String name,String type,float buyprice,float sellprice,String size,float waist,float len,
                           String color,String describe1,String specs,String pic,String selltime,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql = "insert into sell (name,type,buyprice,sellprice,size,waist,len,color,describe1,createtime,"
                + "updatetime,specs,pic,selltime,userid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) on duplicate key update id = last_insert_id(id);";

        SimpleDateFormat sdf = new SimpleDateFormat();// ��ʽ��ʱ��
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// aΪam/pm�ı��
        Date date = new Date();// ��ȡ��ǰʱ��
        String time = sdf.format(date);

        int count = runner.update(conn,sql,name,type,buyprice,sellprice,size,waist,len,color,describe1,
                time,time,specs,pic,selltime,userid);
        DBHelper.close(null, null, conn);
        return count;

    }


    /**
     * �޸��¹��·�
     * @throws Exception
     */
    public int modifySell(int id, String name, String type, float buyprice, float sellprice, String size, float waist, float len,
                          String color, String describe1, String specs, String pic, String selltime,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql = "update sell set name=?,type=?,buyprice=?,sellprice=?,size=?,len=?,waist=?,color=?,specs=?,describe1=?," +
                "updatetime=?,pic=?,selltime=? where id=? and userid=?";

        SimpleDateFormat sdf = new SimpleDateFormat();// ��ʽ��ʱ��
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// aΪam/pm�ı��
        Date date = new Date();// ��ȡ��ǰʱ��
        String time = sdf.format(date);

        int count = runner.update(conn,sql,name,type,buyprice,sellprice,size,len,waist,color,specs,describe1,
                time,pic,selltime,id,userid);
        DBHelper.close(null, null, conn);
        return count;
    }



    /**
     * ����id�����¹����
     * @param id
     * @return
     * @throws Exception
     */
    public Sell getSellById(int id,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql="select * from sell where id=? and userid=?";
        Sell sell = runner.query(conn,sql,new BeanHandler<Sell>(Sell.class),id,userid);
        DBHelper.close(null, null, conn);
        return sell;
    }


    /**
     * ��ȡ�¹��������
     * @return
     * @throws Exception
     */
    public int  getSellCount(int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql = "select count(id)from sell where userid=?";
        Object data = runner.query(conn,sql,new ScalarHandler<>(),userid);
        int count = (int)((long)data);
        DBHelper.close(null, null, conn);
        return count;
    }

    /**
     *ҳ���ѯ
     * @param pageIndex �ڼ�ҳ,��1��ʼ
     * @param pageSize ÿҳ������
     * @return ��ǰҳ����Ϣ
     * @throws Exception
     */
    public List<Sell> getSellByPage(int pageIndex,int pageSize, String type,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        List<Sell> sells;
        String typea = translate(type);
        if("all".equals(type)){
            int offset = (pageIndex-1)*pageSize;
            String sql = "select * from sell where userid=? limit ?,?";
            sells = runner.query(conn,sql,new BeanListHandler<Sell>(Sell.class),userid,offset,pageSize);
        }
        else{
            int offset = (pageIndex-1)*pageSize;
            String sql = "select * from sell where userid=? and type = ? limit ?,?";
            sells = runner.query(conn,sql,new BeanListHandler<Sell>(Sell.class),userid,typea,offset,pageSize);
        }
        DBHelper.close(null, null, conn);
        return sells;
    }

    public List<Sell> getAllSell(int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        List<Sell> sells;
        String sql = "select * from sell where userid=?";
        sells = runner.query(conn,sql,new BeanListHandler<Sell>(Sell.class),userid);
        DBHelper.close(null, null, conn);
        return sells;
    }

    public List<Sell> getSellByKeyAndPage(String key, int userid, String type_s,int pageIndex,int pageSize) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql = null;
        List<Sell> sells;
        if("name".equals(type_s)){
            sql="select * from sell where name=? and userid=? limit ?,?";
            int offset = (pageIndex-1)*pageSize;
            sells = runner.query(conn,sql,new BeanListHandler<Sell>(Sell.class),key,userid,offset,pageSize);
        }
        else if("type".equals(type_s)){
            sql="select * from sell where type=? and userid=? limit ?,?";
            int offset = (pageIndex-1)*pageSize;
            sells = runner.query(conn,sql,new BeanListHandler<Sell>(Sell.class),key,userid,offset,pageSize);
        }
        else if("id".equals(type_s)){
            sql="select * from sell where id=? and userid=? limit ?,?";
            int offset = (pageIndex-1)*pageSize;
            sells = runner.query(conn,sql,new BeanListHandler<Sell>(Sell.class),key,userid,offset,pageSize);
        }
        else{
            sql="select * from sell where id=? or name=? or type=? and userid=? limit ?,?";
            int offset = (pageIndex-1)*pageSize;
            sells = runner.query(conn,sql,new BeanListHandler<Sell>(Sell.class),key,key,key,userid,offset,pageSize);
        }
        DBHelper.close(null, null, conn);
        return sells;
    }

    public int getKeyCount(String key,String type_s,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        int count;
        if("name".equals(type_s)){
            String sql = "select count(id)from sell where userid = ? and name=?";
            Object data = runner.query(conn,sql,new ScalarHandler<>(),userid,key);
            count = (int)((long)data);
        }
        else if("type".equals(type_s)){
            String sql = "select count(id)from sell where userid = ? and type=?";
            Object data = runner.query(conn,sql,new ScalarHandler<>(),userid,key);
            count = (int)((long)data);
        }
        else if("id".equals(type_s)){
            String sql = "select count(id)from sell where userid = ? and id=?";
            Object data = runner.query(conn,sql,new ScalarHandler<>(),userid,key);
            count = (int)((long)data);
        }
        else{
            String sql = "select count(id)from sell where userid = ? and (id=? or type=? or name=?)";
            Object data = runner.query(conn,sql,new ScalarHandler<>(),userid,key,key,key);
            count = (int)((long)data);
        }
        DBHelper.close(null, null, conn);
        return count;
    }

    //ɾ��������id��
    public int removeSell(int id,int userid) throws Exception {
        Connection conn  = DBHelper.getConnection();
        String sql="delete from sell where id=? and userid=?";
        int count = runner.update(conn,sql,id,userid);
        DBHelper.close(null, null, conn);
        return count;
    }


    public String translate(String name){
        String name_t;
        if("skirt".equals(name)){
            name_t = "��ȹ";
        }
        else if("shirt".equals(name)){
            name_t = "����";
        }
        else if("little".equals(name)){
            name_t = "С��";
        }
        else if("sweater".equals(name)){
            name_t = "ë��";
        }
        else if("vest".equals(name)){
            name_t = "���";
        }
        else if("suit".equals(name)){
            name_t = "����";
        }
        else if("shoes".equals(name)){
            name_t = "Ь��";
        }
        else if("seawoman".equals(name)){
            name_t = "ˮ�ַ�";
        }
        else if("pureskirt".equals(name)){
            name_t = "��ɫȹ";
        }
        else if("dress".equals(name)){
            name_t = "����ȹ";
        }
        else{
            name_t = "����";
        }
        return name_t;
    }



    public static void main(String[] args) {
        try {
            sellDao sellDao = new sellDao();
            List<Sell> sells = sellDao.getSellByKeyAndPage("��ȹ",1,"type",1,10);
            System.out.println(sells.size());
            for(Sell sell : sells){
                System.out.println(sell);
                System.out.println(sell.getCreatetime());
            }
            int count = sellDao.getKeyCount("��ȹ","type",1);
            System.out.println(count);


            //System.out.println(count);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

    }

}
