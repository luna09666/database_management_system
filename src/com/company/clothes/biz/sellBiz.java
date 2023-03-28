package com.company.clothes.biz;

import com.company.clothes.bean.*;
import com.company.clothes.dao.clothesDao;
import com.company.clothes.dao.sellDao;
import com.company.clothes.util.DBHelper;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class sellBiz {
    sellDao  sellDao  = new sellDao();


    public List<Sell> getSellByType(String type,int userid) throws Exception  {
        try {
            return sellDao.getSellByType(type,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public int addSell(String name,String type,float buyprice,float sellprice,String size,float waist,float len,
                       String color,String describe1,String specs,String pic,String selltime,int userid) throws Exception  {
       int count = 0;
        try {
            count =  sellDao.addSell(name,type,buyprice,sellprice,size,waist,len,color,describe1,
                    specs,pic,selltime,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;

    }


    public int addSell(Sell sell,int userid) throws Exception{
        return addSell(sell.getName(),sell.getType(),sell.getBuyprice(),sell.getSellprice(),sell.getSize(),sell.getWaist(),
        		sell.getLen(),sell.getColor(),sell.getDescribe1(),
        		sell.getSpecs(),sell.getPic(),sell.getSelltime(),userid);
		
    }

    public int modifySell(int id,String name,String type,float buyprice,float sellprice,String size,float waist,float len,
                          String color,String describe1,String specs,String pic,String selltime,int userid) throws Exception  {
        int count = 0;
        try {
            count = sellDao.modifySell(id,name,type,buyprice,sellprice,size,waist,len,color,describe1,
                    specs,pic,selltime,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }
    
    public int modifySell(Sell sell,int userid) throws Exception  {
        int count = 0;
        try {
            count = sellDao.modifySell(sell.getId(),sell.getName(),sell.getType(),sell.getBuyprice(),sell.getSellprice(),
                    sell.getSize(),sell.getWaist(), sell.getLen(),sell.getColor(),sell.getDescribe1(),
            		sell.getSpecs(),sell.getPic(),sell.getSelltime(),userid);
    	} catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }


    public int removeSell(int id,int userid) throws Exception {
        int count = 0;
        try {
            
            //2.删除
            count = sellDao.removeSell(id,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;

    }
   
    public Sell getSellById(int id,int userid) throws Exception {
        Sell sell = null;

        try {
              sell = sellDao.getSellById(id,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return sell;
    }

    /**
     * 由行数算页数
     * @return
     * @throws Exception
     */
    public int  getSellPageCount(int pageSize,int userid) throws Exception {
        int pageCount = 0;
        try {
            //1.获取行数
            int rowCount = sellDao.getSellCount(userid);
            //2.根据行数得到页数,每页多少条
            pageCount =  (rowCount-1)/pageSize+1;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return pageCount;
    }

    public List<Sell>  getSellByPage(int pageIndex,int pageSize,String type,int userid) throws Exception {
        List<Sell> sells = null;
        try {
            sells = sellDao.getSellByPage(pageIndex,pageSize,type,userid);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sells;

    }

    public List<Sell> getSellByKeyAndPage(int pageIndex,int pageSize,String key,int userid,String type_s) throws Exception {
        List<Sell> sells = null;
        try {
            sells = sellDao.getSellByKeyAndPage(key,userid,type_s,pageIndex,pageSize);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sells;
    }

    public int getKeyPageCount(int pageSize,String key,String type_s,int userid) throws Exception {
        int pageCount = 0;
        try {
            //1.获取行数
            int rowCount = sellDao.getKeyCount(key,type_s,userid);
            //2.根据行数得到页数,每页多少条
            pageCount =  (rowCount-1)/pageSize+1;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return pageCount;
    }

    public static void main(String[] args) {
        try {
            sellBiz sellBiz = new sellBiz();
            List<Sell> sells = sellBiz.getSellByKeyAndPage(1,10,"格裙",1,"type");
            System.out.println(sells.size());
            for(Sell sell : sells){
                System.out.println(sell);
                System.out.println(sell.getCreatetime());
            }
            int count = sellBiz.getKeyPageCount(1,"格裙","type",1);
            System.out.println(count);


            //System.out.println(count);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

    }
}
