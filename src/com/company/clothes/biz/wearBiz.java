package com.company.clothes.biz;

import com.company.clothes.bean.Clothes;
import com.company.clothes.bean.Wear;
import com.company.clothes.dao.wearDao;

import java.sql.SQLException;
import java.util.List;

public class wearBiz {
    wearDao wearDao  = new wearDao();

    public int addWardrobe(String title,String content,String pic,
                           int userid) throws Exception  {
       int count = 0;
        try {
            count =  wearDao.addWear(title,content,pic,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;

    }
    public int addWear(Wear wear,int userid) throws Exception{
        return addWardrobe(wear.getTitle(),wear.getContent(),wear.getPic()
                ,userid);
		
    }


    public int removeWear(int id,int userid) throws Exception {
        int count = 0;
        try {

            //2.删除
            count = wearDao.removeWear(id,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }


    /**
     * 由行数算页数
     * @return
     * @throws Exception 
     */
    public int  getWearPageCount(int pageSize,int userid) throws Exception {
        int pageCount = 0;
        try {
            //1.获取行数
            int rowCount = wearDao.getWearCount(userid);
            //2.根据行数得到页数,每页多少条
           pageCount =  (rowCount-1)/pageSize+1;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return pageCount;
    }

    public Wear getWearById(int id, int userid) throws Exception {
        Wear wear = null;

        try {
            wear = wearDao.getWearById(id,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return wear;
    }
    
    public List<Wear>  getWearByPage(int pageIndex,int pageSize,int userid) throws Exception {
        List<Wear> wears = null;
        try {
        	wears = wearDao.getWearByPage(pageIndex,pageSize,userid);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return wears;
    }

    public static void main(String[] args) {
        try {
            wearBiz clothesBiz = new wearBiz();
            List<Wear> wears = clothesBiz.getWearByPage(1,3,1);
            System.out.println(wears.size());
            int pageCount = clothesBiz.getWearPageCount(3,1);
            for(Wear clothes : wears){
                System.out.println(clothes);
            }
            System.out.println(pageCount);
            @SuppressWarnings("unused")
            wearDao wearDao = new wearDao();

            //System.out.println(count);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

    }

}
