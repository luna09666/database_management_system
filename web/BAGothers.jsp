<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@page import="java.sql.*" %>  <%--导入java.sql包--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title >购物信息添加</title>

	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="css/global.css">
	<script src="js/jquery-3.5.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script>
		//图片预览
		function getFullPath(obj){
			if (obj){
				//ie
				if (window.navigator.userAgent.indexOf("MSIE") >= 1){
					obj.select();
					return document.selection.createRange().text;
				}else if (window.navigator.userAgent.indexOf("Firefox") >= 1){
					//firefox　
					return window.URL.createObjectURL(obj.files.item(0));
				}else if(navigator.userAgent.indexOf("Chrome")>0){
					//chrome
					return window.URL.createObjectURL(obj.files.item(0));
				}
				return obj.value;
			}
		}
		$(function(){
			$("#pic").change(function(){
				var path = getFullPath($(this)[0]);
				console.log(path);
				$("#imgPic").prop("src",path);
			});
		});
	</script>
</head>
<body>
<ul class="for_ul_global">
	<li class="textlocation">欢迎您!</li>
	<li><img src="${useri.pic}" style="width: 60px;height:60px;border-radius: 10em;border-style:solid;border-width:1.5px;border-color:white;"/></li>
	<a class="for_a_global" href="user?type=show&userid=${userid}"><div style="color: #545353;">${useri.name}</div></a></li>
	<div style="margin: 20px 0 0 0;">
		<li><a class="for_a_global active" href="wardrobe?type=query&pageIndex=1&type_q=all&userid=${userid}">衣柜</a></li>
		<li><a class="for_a_global" href="grass?type=query&pageIndex=1&type_q_g=all&type_q=all&userid=${userid}">草单</a></li>
		<li><a class="for_a_global" href="sell?type=query&pageIndex=1&type_q=all&userid=${userid}">卖出</a></li>
		<li><a class="for_a_global" href="user?type=sta&type_sta=all&userid=${userid}">统计</a></li>
		<li><a class="for_a_global" href="wear?type=query&pageIndex=1&userid=${userid}">穿搭</a></li>
	</div>
</ul>
<div class="container">
	<div style="margin-top:20px;margin-bottom:40px;border-style:dashed;border-width:5px 5px;border-color: lightpink;border-radius: 0.5em;">
		<div style="margin:50px;">
			<h4 class="text-center" style="color:#f86a83;"><strong>购物信息添加</strong></h4><br/>
	<form action="grass?type=add&userid=${userid}" role="form" method="post" enctype="multipart/form-data">
		<div class="form-group">
			<label>名称</label><span class="need_26EMK">*</span>
			<input type="text" class="form-control" name="name_a" value="${name_a}" readonly>
		</div>
		<div class="form-group">
			<label>类型</label><br/>
			<input type="text" class="form-control" name="type_a" value="${type_a}" readonly>
		</div>
		<input type="hidden" class="form-control" name="howtobuy" value="${howtobuy}" readonly>
		<c:choose>
			<c:when test="${'预售'.equals(howtobuy)}">
				<div class="form-group">
					<label>定金价格</label>
					<input type="text" class="form-control" name="frontprice" value="0" placeholder="请输入定金价格">
				</div>
				<div class="form-group">
					<label>尾款价格</label>
					<input type="text" class="form-control" name="tailprice" value="0" placeholder="请输入尾款价格">
				</div>
				<div class="form-group">
					<label>再贩时间</label>
					<input name="buytime" type="datetime-local" class="form-control">
				</div>
				<div class="form-group">
					<label>是否已付定金</label><br/>
					<input type=radio name="flag" value="已付定金">已付定金&nbsp;&nbsp;
					<input type=radio name="flag" value="未付定金" checked="checked">未付定金&nbsp;&nbsp;
				</div>
			</c:when>
			<c:otherwise>
				<div class="form-group">
					<label>价格</label>
					<input type="text" class="form-control" name="price" value="0" placeholder="请输入价格">
				</div>
			</c:otherwise>
		</c:choose>
		<c:if test="${'衬衫'.equals(type_a)}">
			<div class="form-group">
				<label>袖长</label><br/>
				<input type=radio name="specs" value="短袖" checked="checked">短袖&nbsp;&nbsp;
				<input type=radio name="specs" value="中袖">中袖&nbsp;&nbsp;
				<input type=radio name="specs" value="长袖">长袖
			</div>
		</c:if>

		<c:if test="${'马甲'.equals(type_a)}">
			<div class="form-group">
				<label>款式</label><br/>
				<input type=radio name="specs" value="毛衣马甲" checked="checked">毛衣马甲&nbsp;&nbsp;
				<input type=radio name="specs" value="西服马甲">西服马甲
			</div>
		</c:if>

		<c:if test="${'毛衣'.equals(type_a)}">
			<div class="form-group">
				<label>衣领</label><br/>
				<input type=radio name="specsa" value="v领" checked="checked">v领&nbsp;&nbsp;
				<input type=radio name="specsa" value="圆领">圆领&nbsp;&nbsp;
				<input type=radio name="specsa" value="水手领">水手领&nbsp;&nbsp;
				<input type=radio name="specsa" value="其它">其它&nbsp;&nbsp;
			</div>
			<div class="form-group">
				<label>厚度</label><br/>
				<input type=radio name="specsb" value="厚款">厚款&nbsp;&nbsp;
				<input type=radio name="specsb" value="薄款">薄款&nbsp;&nbsp;
				<input type=radio name="specsb" value="适中" checked="checked">适中
			</div>
			<div class="form-group">
				<label>款式</label><br/>
				<input type=radio name="specsc" value="开衫">开衫&nbsp;&nbsp;
				<input type=radio name="specsc" value="套头" checked="checked">套头&nbsp;&nbsp;
			</div>
		</c:if>
		<c:if test="${'西服'.equals(type_a)}">
			<div class="form-group">
				<label>长度</label><br/>
				<input type=radio name="specsa" value="长款">长款&nbsp;&nbsp;
				<input type=radio name="specsa" value="正常" checked="checked">正常&nbsp;&nbsp;
				<input type=radio name="specsa" value="短款">短款
			</div>
			<div class="form-group">
				<label>衣领</label><br/>
				<input type=radio name="specsb" value="正常领" checked="checked">正常领&nbsp;&nbsp;
				<input type=radio name="specsb" value="无领">无领&nbsp;&nbsp;
				<input type=radio name="specsb" value="水手领">水手领&nbsp;&nbsp;
				<input type=radio name="specsb" value="异形领">异形领&nbsp;&nbsp;
			</div>
		</c:if>
		<c:if test="${'水手服'.equals(clothes.type)}">
			<div class="form-group">
				<label>襟线条数</label><br/>
				<input type="text" class="form-control" name="specsa" value="三">本
			</div>
			<div class="form-group">
				<label>款类</label><br/>
				<input type=radio name="specsb" value="夏服">夏服&nbsp;&nbsp;
				<input type=radio name="specsb" value="中间服" checked=checked>中间服&nbsp;&nbsp;
				<input type=radio name="specsb" value="冬服">冬服
			</div>
		</c:if>
		<c:if test="${'连衣裙'.equals(type_a)}">
			<div class="form-group">
				<label>类型</label><br/>
				<input type=radio name="specs" value="护奶裙" checked="checked">护奶裙&nbsp;&nbsp;
				<input type=radio name="specs" value="卡奶裙">卡奶裙&nbsp;&nbsp;
				<input type=radio name="specs" value="连衣裙">连衣裙
				<input type=radio name="specs" value="其它">其它
			</div>
		</c:if>
		<c:if test="${'其它'.equals(type_a)}">
			<div class="form-group">
				<label>自定义类型</label><br/>
				<input type="text" class="form-control" name="specs" value="大衣">
			</div>
		</c:if>
		<c:if test="${'鞋子'.equals(type_a)}">
			<div class="form-group">
				<label>尺码</label><br/>
				<input type="text" class="form-control" name="size" value="37">
			</div>
		</c:if>
		<c:if test="${!('小物'.equals(type_a))&&!('其它'.equals(type_a))&&!('鞋子'.equals(type_a))}">
			<div class="form-group">
				<label>尺码</label><br/>
				<input type=radio name="size" value="xxs">XXS&nbsp;&nbsp;
				<input type=radio name="size" value="xs">XS&nbsp;&nbsp;
				<input type=radio name="size" value="s">S&nbsp;&nbsp;
				<input type=radio name="size" value="m" checked="checked">M&nbsp;&nbsp;
				<input type=radio name="size" value="l">L<br/>
				<input type=radio name="size" value="xl">XL&nbsp;&nbsp;
				<input type=radio name="size" value="xxl">XXL&nbsp;&nbsp;
				<input type=radio name="size" value="均码">均码&nbsp;&nbsp;
				<input type=radio name="size" value="其它">其它
			</div>
		</c:if>
		<div class="form-group">
			<label>颜色</label><br/>
			<input type="text" class="form-control" name="color" value="红色">
		</div>
		<div class="form-group">
			<label>描述</label><br/>
			<input type="text" class="form-control" name="describe" placeholder="写下与它结缘的经历吧！">
		</div>
		<div class="form-group">
			<label>图片</label><br/>
			<input type="file" name="pic" id="pic">
		</div>
		<td rowspan="9" valign="top" >
			<fieldset style="width: 210px; height: 360px;">
				<legend>图片预览</legend>
				<img id="imgPic" src="#" width="200px" height="300px"/>
			</fieldset>
		</td>
		<div>
			<input type="submit" value="添加" class="button_for_text" onclick="return confirm('确认添加');">
		</div>
	</form>
</div>
	</div></div></body>
</html>
