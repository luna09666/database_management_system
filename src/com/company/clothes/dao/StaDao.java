package com.company.clothes.dao;

import com.company.clothes.bean.*;
import com.company.clothes.dao.*;
import com.company.clothes.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.poi.poifs.filesystem.Entry;

import java.sql.Connection;
import java.util.*;


public class StaDao {
	QueryRunner runner = new QueryRunner();
    clothesDao clothesDao = new clothesDao();
    sellDao sellDao= new sellDao();



    public Map<String, Integer> getMostColor(String type, int userid) throws Exception {

        Map<String, Integer> m = new HashMap<String, Integer>();
        Map<String, Integer> newm = new LinkedHashMap<String, Integer>();
        List<Clothes> cclothes = new ArrayList<>();
        cclothes = clothesDao.getWardrobeByType(type,userid);
        System.out.println(cclothes);
        if("skirt".equals(type)||"seawoman".equals(type)) {
            for (Clothes clothes : cclothes) { //??????????
                if (!clothes.getColor().isEmpty()) {
                    if (m.containsKey(clothes.getColor().split(" ")[0])) {
                        m.put(clothes.getColor().split(" ")[0], ((int) m.get((clothes.getColor().split(" ")[0])) + 1));
                    } else {
                        m.put(clothes.getColor().split(" ")[0], 1);
                    }
                }
            }
        }
        else{
            for (Clothes clothes : cclothes) {
                if(m.containsKey(clothes.getColor())){
                    m.put(clothes.getColor(),((int) m.get(clothes.getColor()))+1);
                }
                else{
                    m.put(clothes.getColor(),1);
                }
            }
        }
            ArrayList<Map.Entry<String, Integer>> l = new ArrayList<Map.Entry<String, Integer>>(m.entrySet());
            Collections.sort(l, new Comparator<Map.Entry<String, Integer>>() {

                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return (int) (o2.getValue() - o1.getValue());
                }
            });
            for (int i = 0; i < Math.min(l.size(),8); i++) {
                newm.put(l.get(i).getKey(), l.get(i).getValue());
            }

        return newm;
    }

    public Map<String, Integer> getMostSkirtColor(String type, int userid) throws Exception {

        Map<String, Integer> m = new HashMap<String, Integer>();
        Map<String, Integer> newm = new LinkedHashMap<String, Integer>();
        List<Clothes> cclothes = new ArrayList<>();
        cclothes = clothesDao.getWardrobeByType(type,userid);
        if("skirt".equals(type)){
            Map<String, Integer> mcolors1 = new HashMap<String, Integer>();
            for (Clothes clothes : cclothes) { //??????????
                if(!clothes.getColor().isEmpty()){
                    if(mcolors1.containsKey(clothes.getColor().split(" ")[1])){
                        mcolors1.put(clothes.getColor().split(" ")[1],((int) mcolors1.get((clothes.getColor().split(" ")[1]))+1));
                    }else{
                        mcolors1.put(clothes.getColor().split(" ")[1],1);
                    }
                }
            }

            ArrayList<Map.Entry<String, Integer>> lcolors1 = new ArrayList<Map.Entry<String, Integer>>(mcolors1.entrySet());
            Collections.sort(lcolors1, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return (int) (o2.getValue() - o1.getValue());
                }
            });
            for (int i = 0; i < Math.min(lcolors1.size(),8); i++) {
                newm.put(lcolors1.get(i).getKey(), lcolors1.get(i).getValue());
            }
        }
        return newm;
    }



    public Map<String, Float> getMostType(int userid) throws Exception {

        Map<String, Float> m = new HashMap<String, Float>();
        Map<String, Float> newm = new LinkedHashMap<String, Float>();
        List<Clothes> cclothes = new ArrayList<>();
        cclothes = clothesDao.getAllWardrobe(userid);
        for (Clothes clothes : cclothes) {
            if(m.containsKey(clothes.getType())){
                m.put(clothes.getType(),((float) m.get(clothes.getType()))+clothes.getPrice());
            }
            else{
                m.put(clothes.getType(),clothes.getPrice());
            }
        }
        ArrayList<Map.Entry<String, Float>> l = new ArrayList<Map.Entry<String, Float>>(m.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<String, Float>>() {

            @Override
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                return (int) (o2.getValue() - o1.getValue());
            }
        });
        for (int i = 0; i < Math.min(l.size(),8); i++) {
            newm.put(l.get(i).getKey(), l.get(i).getValue());
        }

        return newm;
    }

    public Map<Float, Integer> getMostWaist(int userid) throws Exception {

        Map<Float, Integer> m = new HashMap<Float, Integer>();
        Map<Float, Integer> newm = new LinkedHashMap<Float, Integer>();
        List<Clothes> cclothes = clothesDao.getAllWardrobe(userid);
        for (Clothes clothes : cclothes) {
            if(clothes.getLen()!=0){
                if(m.containsKey(clothes.getWaist())){
                    m.put(clothes.getWaist(),((int) m.get(clothes.getWaist()))+1);
                }
                else{
                    m.put(clothes.getWaist(),1);
                }
            }
            System.out.println(m);
        }
        ArrayList<Map.Entry<Float, Integer>> l = new ArrayList<Map.Entry<Float, Integer>>(m.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<Float, Integer>>() {

            @Override
            public int compare(Map.Entry<Float, Integer> o1, Map.Entry<Float, Integer> o2) {
                return (int) (o2.getValue() - o1.getValue());
            }
        });
        for (int i = 0; i < Math.min(l.size(),8); i++) {
            newm.put(l.get(i).getKey(), l.get(i).getValue());
        }

        return newm;
    }

    public Map<Float, Integer> getMostLen(int userid) throws Exception {

        Map<Float, Integer> m = new HashMap<Float, Integer>();
        Map<Float, Integer> newm = new LinkedHashMap<Float, Integer>();
        List<Clothes> cclothes = clothesDao.getAllWardrobe(userid);
        for (Clothes clothes : cclothes) {
            if(clothes.getLen()!=0){
                if(m.containsKey(clothes.getLen())){
                    m.put(clothes.getLen(),((int) m.get(clothes.getLen()))+1);
                }
                else{
                    m.put(clothes.getLen(),1);
                }
            }
        }
        ArrayList<Map.Entry<Float, Integer>> l = new ArrayList<Map.Entry<Float, Integer>>(m.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<Float, Integer>>() {

            @Override
            public int compare(Map.Entry<Float, Integer> o1, Map.Entry<Float, Integer> o2) {
                return (int) (o2.getValue() - o1.getValue());
            }
        });
        for (int i = 0; i < Math.min(l.size(),8); i++) {
            newm.put(l.get(i).getKey(), l.get(i).getValue());
        }

        return newm;
    }

    public Map<String, Integer> getMostSize(int userid) throws Exception {

        Map<String, Integer> m = new HashMap<String, Integer>();
        Map<String, Integer> newm = new LinkedHashMap<String, Integer>();
        List<Clothes> cclothes = new ArrayList<>();
        cclothes = clothesDao.getAllWardrobe(userid);
        for (Clothes clothes : cclothes) {
            if(m.containsKey(clothes.getSize())){
                m.put(clothes.getSize(),((int) m.get(clothes.getSize()))+1);
            }
            else{
                m.put(clothes.getSize(),1);
            }
        }
        ArrayList<Map.Entry<String, Integer>> l = new ArrayList<Map.Entry<String, Integer>>(m.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<String, Integer>>() {

            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (int) (o2.getValue() - o1.getValue());
            }
        });
        for (int i = 0; i < Math.min(l.size(),8); i++) {
            newm.put(l.get(i).getKey(), l.get(i).getValue());
        }

        return newm;
    }

    public Map<String, Float> getMoneyPerYear(String type,int userid) throws Exception {

        Map<String, Float> m = new HashMap<String, Float>();
        List<Clothes> cclothes = new ArrayList<>();
        if("all".equals(type)){
            cclothes = clothesDao.getAllWardrobe(userid);
        }
        else{
            cclothes = clothesDao.getWardrobeByType(type,userid);
        }
        for (Clothes clothes : cclothes) {
            if(clothes.getCreatetime()!=null){
                if(m.containsKey(clothes.getCreatetime().split("-")[0])){
                    m.put(clothes.getCreatetime().split("-")[0],((float) m.get(clothes.getCreatetime().split("-")[0]))+clothes.getPrice());
                }
                else{
                    m.put(clothes.getCreatetime().split("-")[0],clothes.getPrice());
                }
            }
        }
        return m;
    }

    public Map<String, Float> getSellPerYear(String type,int userid) throws Exception {

        Map<String, Float> m = new HashMap<String, Float>();
        List<Sell> sells = new ArrayList<>();
        if("all".equals(type)){
            sells = sellDao.getAllSell(userid);
        }
        else{
            sells = sellDao.getSellByType(type,userid);
        }
        for (Sell sell : sells) {
            if(sell.getCreatetime()!=null){
                if(m.containsKey(sell.getCreatetime().split("-")[0])){
                    m.put(sell.getCreatetime().split("-")[0],((float) m.get(sell.getCreatetime().split("-")[0]))+sell.getSellprice());
                }
                else{
                    m.put(sell.getCreatetime().split("-")[0],sell.getSellprice());
                }
            }
        }
        return m;
    }

    public Map<String, Float> getMoneyPerMonth(String type,int userid) throws Exception {

        Map<String, Float> m = new HashMap<String, Float>();
        List<Clothes> cclothes = new ArrayList<>();

        if ("all".equals(type)) {
            cclothes = clothesDao.getAllWardrobe(userid);
        }
        else{
            cclothes = clothesDao.getWardrobeByType(type,userid);
        }
        for (Clothes clothes : cclothes) {
            if(clothes.getCreatetime()!=null){
                if(m.containsKey(clothes.getCreatetime().split("-")[0]+"-"+clothes.getCreatetime().split("-")[1])){
                    m.put(clothes.getCreatetime().split("-")[0]+"-"+clothes.getCreatetime().split("-")[1],((float) m.get(clothes.getCreatetime().split("-")[0]+"-"+clothes.getCreatetime().split("-")[1]))+clothes.getPrice());
                }
                else{
                    m.put(clothes.getCreatetime().split("-")[0]+"-"+clothes.getCreatetime().split("-")[1],clothes.getPrice());
                }
            }
        }
        return m;
    }

    public Map<String, Float> getSellPerMonth(String type,int userid) throws Exception {

        Map<String, Float> m = new HashMap<String, Float>();
        List<Sell> sells = new ArrayList<>();
        if ("all".equals(type)) {
            sells = sellDao.getAllSell(userid);
        }
        else{
            sells = sellDao.getSellByType(type,userid);
        }

        for (Sell sell : sells) {
            if(sell.getCreatetime()!=null){
                if(m.containsKey(sell.getCreatetime().split("-")[0]+"-"+sell.getCreatetime().split("-")[1])){
                    m.put(sell.getCreatetime().split("-")[0]+"-"+sell.getCreatetime().split("-")[1],((float) m.get(sell.getCreatetime().split("-")[0]+"-"+sell.getCreatetime().split("-")[1]))+sell.getSellprice());
                }
                else{
                    m.put(sell.getCreatetime().split("-")[0]+"-"+sell.getCreatetime().split("-")[1],sell.getSellprice());
                }
            }
        }
        return m;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new StaDao().getMoneyPerYear("all",1));
    }

}
