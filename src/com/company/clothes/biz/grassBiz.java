package com.company.clothes.biz;

import com.company.clothes.bean.*;
import com.company.clothes.dao.grassDao;
import com.company.clothes.util.DBHelper;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class grassBiz {
    grassDao  grassDao  = new grassDao();


    public List<Grass> getGrassByType(String type,int userid) throws Exception  {
        try {
            return grassDao.getGrassByType(type, userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public int addGrass(String name,String type,float price,String size,float waist,float len,
    		String color,String describe1,String specs,String pic,
    		float frontprice,float tailprice,String buytime,String flag,int userid) throws Exception  {
       int count = 0;
        try {
            count =  grassDao.addGrass(name,type,price,size,waist,len,
                    color,describe1,specs,pic,frontprice,tailprice,buytime,flag,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;

    }
    public int addGrass(Grass Grass,int userid) throws Exception{
        return addGrass(Grass.getName(),Grass.getType(),Grass.getPrice(),Grass.getSize(),Grass.getWaist(),
        		Grass.getLen(),Grass.getColor(),Grass.getDescribe1(),
        		Grass.getSpecs(),Grass.getPic(),Grass.getFrontprice(),Grass.getTailprice(),
                Grass.getBuyTime(),Grass.getFlag(),userid);
		
    }

    public int payFrontGrass(int id, String flag,int userid) throws Exception {
        int count = 0;
        try {
            count = grassDao.payFrontGrass(id,flag,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }

    public int payFrontGrass(Grass Grass,int userid){
        int count = 0;
        try {
            count = grassDao.payFrontGrass(Grass.getId(),Grass.getFlag(),userid);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return count;
    }

    public int buyGrass(String name,String type,float price,String size,Float waist,Float len,
                        String color,String describe1,String specs,String pic,String flag,int userid) throws Exception {
        int count = 0;
        try {
            count = grassDao.buyGrass(name,type,price,size,waist,len,color,describe1,specs,pic,flag,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }

    public int buyGrass(Grass Grass,int userid){
        int count = 0;
        try {
            count = grassDao.buyGrass(Grass.getName(),Grass.getType(),Grass.getPrice(),Grass.getSize(),Grass.getWaist(),
                    Grass.getLen(),Grass.getColor(),Grass.getDescribe1(),
                    Grass.getSpecs(),Grass.getPic(),Grass.getFlag(),userid);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return count;
    }

    public int modifyGrass(int id,String name,String type,float price,String size,float waist,float len,String color,
                              String describe1,String specs,String pic,float frontprice,float tailprice,String buytime,
                           String flag,int userid) throws Exception  {
        int count = 0;
        try {
            count = grassDao.modifyGrass(id,name,type,price,size,waist,len,color,describe1,specs,pic,frontprice,
                    tailprice,buytime,flag,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }

    public int modifyGrass(Grass grass,int userid) throws Exception  {
        int count = 0;
        try {
            count = grassDao.modifyGrass(grass.getId(),grass.getName(),grass.getType(),
                    grass.getPrice(),grass.getSize(),grass.getWaist(),grass.getLen(),
                    grass.getColor(),grass.getDescribe1(),grass.getSpecs(),grass.getPic(),
                    grass.getFrontprice(),grass.getTailprice(),grass.getBuytime(),grass.getFlag(),userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }


    public int removeGrass(int id,int userid) throws Exception {
        int count = 0;
        try {
            //2.删除
            count = grassDao.removeGrass(id,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }
   
    public Grass getGrassById(int id,int userid) throws Exception {
        Grass grass = null;

        try {
              grass = grassDao.getGrassById(id,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return grass;
    }
    /**
     * 由行数算页数
     * @return
     * @throws Exception
     */
    public int  getGrassPageCount(int pageSize,int userid) throws Exception {
        int pageCount = 0;
        try {
            //1.获取行数
            int rowCount = grassDao.getGrassCount(userid);
            //2.根据行数得到页数,每页多少条
            pageCount =  (rowCount-1)/pageSize+1;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return pageCount;
    }



    public List<Grass> getGrassByKeyAndPage(int pageIndex,int pageSize,String key,int userid,String type_s) throws Exception {
        List<Grass> grasses = null;
        try {
            grasses = grassDao.getGrassByKeyAndPage(key,userid,type_s,pageIndex,pageSize);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return grasses;
    }

    public int getKeyPageCount(int pageSize,String key,String type_s,int userid) throws Exception {
        int pageCount = 0;
        try {
            //1.获取行数
            int rowCount = grassDao.getKeyCount(key,type_s,userid);
            //2.根据行数得到页数,每页多少条
            pageCount =  (rowCount-1)/pageSize+1;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return pageCount;
    }

    public List<Grass>  getGrassByPage(int pageIndex,int pageSize,String type,String type_g,int userid) throws Exception {
        List<Grass> grasses = null;
        try {
            grasses = grassDao.getGrassByPage(pageIndex,pageSize,type,type_g,userid);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return grasses;

    }

    public static void main(String[] args) {
        try {
            grassBiz grassBiz = new grassBiz();
            List<Grass> grasses = grassBiz.getGrassByKeyAndPage(1,10,"格裙",1,"type");
            System.out.println(grasses.size());
            for(Grass grass : grasses){
                System.out.println(grass);
                System.out.println(grass.getLen());
            }
            int count = grassBiz.getKeyPageCount(1,"格裙","type",1);
            System.out.println(count);

            //System.out.println(count);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

    }

}
