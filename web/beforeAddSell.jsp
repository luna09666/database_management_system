<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@page import="java.sql.*" %>  <%--导入java.sql包--%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title >购物信息添加</title>
	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="css/global.css">
	<script src="js/jquery-3.5.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
</head>
<body>
<ul class="for_ul_global">
	<li class="textlocation">欢迎您!</li>
	<li><img src="${useri.pic}" style="width: 60px;height:60px;border-radius: 10em;border-style:solid;border-width:1.5px;border-color:white;"/></li>
	<a class="for_a_global" href="user?type=show&userid=${userid}"><div style="color: #545353;">${useri.name}</div></a></li>
	<div style="margin: 20px 0 0 0;">
		<li><a class="for_a_global" href="wardrobe?type=query&pageIndex=1&type_q=all&userid=${userid}">衣柜</a></li>
		<li><a class="for_a_global" href="grass?type=query&pageIndex=1&type_q_g=all&type_q=all&userid=${userid}">草单</a></li>
		<li><a class="for_a_global active" href="sell?type=query&pageIndex=1&type_q=all&userid=${userid}">卖出</a></li>
		<li><a class="for_a_global" href="user?type=sta&type_sta=all&userid=${userid}">统计</a></li>
		<li><a class="for_a_global" href="wear?type=query&pageIndex=1&userid=${userid}">穿搭</a></li>
	</div>
</ul>
<ul class="for_ul_global">
	<li class="textlocation">欢迎您!</li>
	<li><img src="${useri.pic}" style="width: 60px;height:60px;border-radius: 10em;border-style:solid;border-width:1.5px;border-color:white;"/></li>
	<a class="for_a_global" href="user?type=show&userid=${userid}"><div style="color: #545353;">${useri.name}</div></a></li>
	<div style="margin: 20px 0 0 0;">
		<li><a class="for_a_global active" href="wardrobe?type=query&pageIndex=1&type_q=all&userid=${userid}">衣柜</a></li>
		<li><a class="for_a_global" href="grass?type=query&pageIndex=1&type_q_g=all&type_q=all&userid=${userid}">草单</a></li>
		<li><a class="for_a_global" href="sell?type=query&pageIndex=1&type_q=all&userid=${userid}">卖出</a></li>
		<li><a class="for_a_global" href="user?type=sta&userid=${userid}">统计</a></li>
		<li><a class="for_a_global" href="wear?type=query&pageIndex=1&userid=${userid}">穿搭</a></li>
	</div>
</ul>
<div class="container">
	<div style="margin-top:20px;margin-bottom:40px;border-style:dashed;border-width:5px 5px;border-color: lightpink;border-radius: 0.5em;">
		<div style="margin:50px;">
	<h4 class="text-center" style="color:#f86a83;"><strong>购物信息添加</strong></h4><br/>
	<form action="sell?type=addpre&userid=${userid}" role="form" method="post">
		<div class="form-group">
			<label>名称</label><span class="need_26EMK">*</span>
			<input type="text" class="form-control" name="name_a" placeholder="请输入名称">
		</div>
		<div class="form-group">
			<label>类型</label><br/>
			<select name="type_a">
				<option>格裙</option>
				<option>衬衫</option>
				<option>小物</option>
				<option>毛衣</option>
				<option>马甲</option>
				<option>西服</option>
				<option>鞋子</option>
				<option>水手服</option>
				<option>纯色裙</option>
				<option>连衣裙</option>
				<option>其它</option>
			</select>
		</div>
		<div class="form-group">
			<label>卖出价格</label>
			<input type="text" class="form-control" name="sellprice" placeholder="请输入卖出价格">
		</div>
		<div class="form-group">
			<label>买入价格</label>
			<input type="text" class="form-control" name="buyprice" placeholder="请输入买入价格">
		</div>
		<div class="form-group">
			<label>卖出时间</label>
			<input name="selltime" type="datetime-local" class="form-control">
		</div>
		<button type='submit' class="button_for_text" >确定</button>
	</form>
</div>
	</div></div>
</body>
</html>
