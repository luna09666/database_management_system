package com.company.clothes.bean;


import java.io.Serializable;


public class Sell implements Serializable {

    /**
     *
     */
    private int id;
    private String name;
    private String type;
    private float buyprice;
    private float sellprice;
    private String selltime;
    private String size;
    private float waist;
    private float len;
    private String color;
    private String describe1;
    private String createtime;
    private String updatetime;
    private String specs;
    private String pic;


    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = (int) id;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLen() {
        return len;
    }

    public void setLen(float len) {
        this.len = len;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public float getWaist() {
        return waist;
    }

    public void setWaist(float waist) {
        this.waist = waist;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescribe1() {
        return describe1;
    }

    public void setDescribe1(String describe) {
        this.describe1 = describe;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public float getBuyprice() {
        return buyprice;
    }

    public void setBuyprice(float buyprice) {
        this.buyprice = buyprice;
    }

    public float getSellprice() {
        return sellprice;
    }

    public void setSellprice(float sellprice) {
        this.sellprice = sellprice;
    }

    public String getSelltime() {
        return selltime;
    }

    public void setSelltime(String selltime) {
        this.selltime = selltime;
    }

    @Override
    public String toString() {
        return "Sell{" +
                "id="+id+
                "name='"+name + '\'' +
                ",type = '"+type + '\'' +
                ",buyprice="+buyprice+
                ",sellprice="+sellprice+
                ",size='"+size + '\'' +
                ",len="+len+
                ",waist="+waist+
                ",color='"+color + '\'' +
                ",specs='"+specs + '\'' +
                ",describe1='"+describe1 + '\'' +
                ",specs='"+specs + '\'' +
                ",selltime='"+selltime + '\'' +
                ",createtime='" + createtime + '\'' +
                ",updatetime='" + updatetime + '\'' +
                '}';
    }
    public static void main(String[] args) {
        try {
            System.out.println("中文");
            Sell sell = new Sell();
            sell.setName("中文");
            System.out.println(sell.getName());
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }
}
