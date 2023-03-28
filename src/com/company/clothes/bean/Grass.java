package com.company.clothes.bean;

import java.io.Serializable;


public class Grass implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String type;
	private float price;
	private float frontprice;
	private float tailprice;
	private String buytime;
	private String size;
	private float waist;
	private float len;
	private String color;
	private String describe1;
	private String createtime;
	private String updatetime;
	private String specs;
	private String pic;
	private String flag;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

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

	public float getPrice() {
    return price;
  }

  public void setPrice(float price) {
    this.price = price;
  }

	public float getFrontprice() {
		return frontprice;
	}

	public void setFrontprice(float frontprice) {
		this.frontprice = frontprice;
	}

	public float getTailprice() {
		return tailprice;
	}

	public void setTailprice(float tailprice) {
		this.tailprice = tailprice;
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
  public String getBuyTime() {
	  	return buytime;
		}

  public void setBuyTime(String buytime) {
	  	this.buytime = buytime;
		}
  
  public String getSpecs() {
	  	return specs;
		}	

  public void setSpecs(String specs) {
	  	this.specs = specs;
		}

	public String getBuytime() {
		return buytime;
	}

	public void setBuytime(String buytime) {
		this.buytime = buytime;
	}

	@Override
  public String toString() {
    return "Grass{" +
    		"id="+id+
    		"name="+name + '\'' +
    		",type = "+type + '\'' +
    		",price="+price+
    		",frontprice="+frontprice+
    		",tailprice="+tailprice+
    		",size="+size + '\'' +
    		",len="+len+
    		",waist="+waist+
    		",color="+color + '\'' +
    		",specs="+specs + '\'' +
    		",describe1="+describe1 + '\'' +
    		",specs="+specs + '\'' +
    		",buytime="+buytime + '\'' +
    		'}';
  }
}
