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
public class User implements Serializable {
  /**
	 * 
	 */


@Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", password='" + password +
            '}';
  }

  private int id;
  private String name;
  private String password;
  private String pic;
  private String describe1;
  private String sex;
  private String position;
  private String phonenumber;
  private String createtime;

  public String getCreatetime() {
    return createtime;
  }

  public void setCreatetime(String createtime) {
    this.createtime = createtime;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public String getPic() {
    return pic;
  }

  public void setPic(String pic) {
    this.pic = pic;
  }

  public String getDescribe1() {
    return describe1;
  }

  public void setDescribe1(String describe1) {
    this.describe1 = describe1;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getPhonenumber() {
    return phonenumber;
  }

  public void setPhonenumber(String phonenumber) {
    this.phonenumber = phonenumber;
  }
}
