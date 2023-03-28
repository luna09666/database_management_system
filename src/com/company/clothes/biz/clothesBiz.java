package com.company.clothes.biz;

import com.company.clothes.bean.*;
import com.company.clothes.dao.clothesDao;

import java.sql.SQLException;
import java.util.List;

public class clothesBiz {
    clothesDao ClothesDao  = new clothesDao();

    public int addWardrobe(String name,String type,float price,String size,float waist,float len,
    		String color,String describe1,String specs,String pic,int userid) throws Exception  {
       int count = 0;
        try {
            count =  ClothesDao.addWardrobe(name,type,price,size,waist,len,color,describe1,specs,pic,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;

    }
    public int addWardrobe(Clothes clothes,int userid) throws Exception{
        return addWardrobe(clothes.getName(),clothes.getType(),clothes.getPrice(),clothes.getSize(),
                clothes.getWaist(),clothes.getLen(),clothes.getColor(),clothes.getDescribe1(),clothes.getSpecs(),
                clothes.getPic(),userid);
		
    }

    public int sellWardrobe(String name,String type,float buyprice,float sellprice,String selltime,
                            String size,float waist,float len,String color,
                            String describe1,String specs,String pic,int userid) throws Exception  {
        int count = 0;
        try {
            count =  ClothesDao.sellWardrobe(name,type,buyprice,sellprice,selltime,size,waist,
                    len,color,describe1,specs,pic,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;

    }

    public int modifyWardrobe(int id,String name,String type,float price,String size,float waist,float len,String color,
    		String describe1,String specs,String pic,int userid) throws Exception  {
        int count = 0;
        try {
            count = ClothesDao.modifyWardrobe(id,name,type,price,size,waist,len,color,describe1,specs,pic,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }
    
    public int modifyWardrobe(Clothes clothes,int userid) throws Exception  {
        int count = 0;
        try {
            count = ClothesDao.modifyWardrobe(clothes.getId(),clothes.getName(),clothes.getType(),
                    clothes.getPrice(),clothes.getSize(),clothes.getWaist(),clothes.getLen(),
                    clothes.getColor(),clothes.getDescribe1(),clothes.getSpecs(),clothes.getPic(),userid);
    	} catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }


    public int removeWardrobe(int id,int userid) throws Exception {
        int count = 0;
        try {

            //2.删除
            count = ClothesDao.removeWardrobe(id,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }
   
    public Clothes getWardrobeById(int id, int userid) throws Exception {
        Clothes clothes = null;

        try {
              clothes = ClothesDao.getWardrobeById(id,userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return clothes;
    }

    /**
     * 由行数算页数
     * @return
     * @throws Exception 
     */
    public int  getWardrobePageCount(int pageSize,int userid) throws Exception {
        int pageCount = 0;
        try {
            //1.获取行数
            int rowCount = ClothesDao.getWardrobeCount(userid);
            //2.根据行数得到页数,每页多少条
           pageCount =  (rowCount-1)/pageSize+1;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return pageCount;
    }
    
    public List<Clothes>  getWardrobeByPage(int pageIndex,int pageSize,String type,int userid) throws Exception {
        List<Clothes> cclothes = null;
        try {
        	cclothes = ClothesDao.getWardrobeByPage(pageIndex,pageSize,type,userid);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return cclothes;
    }

    public List<Clothes>  getWardrobeByKeyAndPage(int pageIndex,int pageSize,String key,int userid,String type_s) throws Exception {
        List<Clothes> cclothes = null;
        try {
            cclothes = ClothesDao.getWardrobeByKeyAndPage(key,userid,type_s,pageIndex,pageSize);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return cclothes;
    }

    public List<Clothes>  getAllWardrobe(int userid) throws Exception {
        List<Clothes> cclothes = null;
        try {
            cclothes = ClothesDao.getAllWardrobe(userid);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return cclothes;
    }

    public int getKeyPageCount(int pageSize,String key,String type_s,int userid) throws Exception {
        int pageCount = 0;
        try {
            //1.获取行数
            int rowCount = ClothesDao.getKeyCount(key,type_s,userid);
            //2.根据行数得到页数,每页多少条
            pageCount =  (rowCount-1)/pageSize+1;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return pageCount;
    }


    public static void main(String[] args) {
        try {
            clothesBiz clothesBiz = new clothesBiz();
            List<Clothes> cclothes = clothesBiz.getWardrobeByPage(1,3,"all",1);
            System.out.println(cclothes.size());
            int pageCount = clothesBiz.getWardrobePageCount(3,1);
            for(Clothes clothes : cclothes){
                System.out.println(clothes);
            }
            System.out.println(pageCount);
            @SuppressWarnings("unused")
            clothesDao clothesDao = new clothesDao();

            //System.out.println(count);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

    }

}
