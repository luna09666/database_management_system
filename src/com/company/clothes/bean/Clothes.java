package com.company.clothes.bean;

import java.io.Serializable;

public class Clothes implements Serializable {

	/**
	 * 
	 */
	private int id;
	private String name;
	private String type;
	private float price;
	private String size;
	private float waist;
	private float len;
	private String color;
	private String describe1;
	private String createtime;
	private String updatetime;
	private String specs;
	private String pic;



  	public int getId() { return id; }

  	public void setId(int id) { this.id = id; }


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


  	public float getPrice() {
    return price;
  }

  	public void setPrice(float price) {
    this.price = price;
  }
  
  	public String getSize() {
	    return size;
  }

  	public void setSize(String size) {
	    this.size = size;
  }
  	public float getLen() {
	    return len;
  }

  	public void setLen(float len) {
	    this.len = len;
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

  	public String getSpecs() {
	  	return specs;
		}	

  	public void setSpecs(String specs) {
	  	this.specs = specs;
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

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}


  @Override
  public String toString() {
	    return "Clothes{" +
	    		"id="+id+
	    		"name='"+name + '\'' +
	    		",type = '"+type + '\'' +
	    		",price="+price+
	    		",size='"+size + '\'' +
	    		",len="+len+
	    		",waist="+waist+
	    		",color='"+color + '\'' +
				",createtime='" + createtime + '\'' +
				",updatetime='" + updatetime + '\'' +
	    		",specs='"+specs + '\'' +
	    		",describe1='"+describe1 + '\'' +
	    		",specs='"+specs + '\'' +
				",pic='"+pic + '\'' +
	    		'}';
	  }
	}
