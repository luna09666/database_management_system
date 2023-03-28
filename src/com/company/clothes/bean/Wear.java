package com.company.clothes.bean;


import java.io.Serializable;

/**
 * 保存用户的信息
 * 1.Serializable
 * 2.私有的属性
 * 3.getter/setter
 * 4.默认的构造
 *
 */
public class Wear implements Serializable {
  /**
	 * 
	 */


@Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", content='" + content +'\'' +
            ", pic='" + pic +'\'' +
            ", userid='" + userid +
            ", createtime='" + createtime +'\'' +
            ", updatetime='" + updatetime +'\'' +
            '}';
  }

  private int id;
  private String title;
  private String content;
  private String pic;
  private String userid;
  private String createtime;
  private String updatetime;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getPic() {
    return pic;
  }

  public void setPic(String pic) {
    this.pic = pic;
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
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
