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
		<li><a class="for_a_global" href="wardrobe?type=query&pageIndex=1&type_q=all&userid=${userid}">衣柜</a></li>
		<li><a class="for_a_global" href="grass?type=query&pageIndex=1&type_q_g=all&type_q=all&userid=${userid}">草单</a></li>
		<li><a class="for_a_global active" href="sell?type=query&pageIndex=1&type_q=all&userid=${userid}">卖出</a></li>
		<li><a class="for_a_global" href="user?type=sta&type_sta=all&userid=${userid}">统计</a></li>
		<li><a class="for_a_global" href="wear?type=query&pageIndex=1&userid=${userid}">穿搭</a></li>
	</div>
</ul>
<div class="container">
	<div style="margin-top:20px;margin-bottom:40px;border-style:dashed;border-width:5px 5px;border-color: lightpink;border-radius: 0.5em;">
		<div style="margin:50px;">
			<h4 class="text-center" style="color:#f86a83;"><strong>购物信息添加</strong></h4><br/>
			<form action="sell?type=add&userid=${userid}" role="form" method="post" enctype="multipart/form-data">
				<div class="form-group">
					<label>名称</label><span class="need_26EMK">*</span>
					<input type="text" class="form-control" name="name_a" value="${name_a}" readonly>
				</div>

				<div class="form-group">
					<label>卖出价格</label>
					<input type="text" class="form-control" name="sellprice" value="${sellprice}">
				</div>
				<div class="form-group">
					<label>买入价格</label>
					<input type="text" class="form-control" name="buyprice" value="${buyprice}">
				</div>
				<div class="form-group">
					<label>卖出时间</label>
					<input name="selltime" type="datetime-local" class="form-control" value="${selltime}">
				</div>
				<div class="form-group">
					<label>类型</label><br/>
					<input type="text" class="form-control" name="type_a" value="${type_a}" readonly>
				</div>
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
  			    <div class="form-group">
          	    	<label>腰围</label><br/>
          	    	<input type="text" class="form-control" name="waist" value="68">
          	    </div>
          	    <div class="form-group">
          	    	<label>裙长</label><br/>
          	    	<input type="text" class="form-control" name="len" value="42">
          	    </div>
          	    <div class="form-group">
          	    	<label>裙褶</label><br/>
          	    	<input type=radio name="specs" value="轮褶" checked="checked">轮褶&nbsp;&nbsp;
          	    	<input type=radio name="specs" value="箱褶">箱褶&nbsp;&nbsp;
          	    	<input type=radio name="specs" value="前箱褶">前箱褶&nbsp;&nbsp;
          	    	<input type=radio name="specs" value="无褶">无褶&nbsp;&nbsp;
          	    	<input type=radio name="specs" value="其它">其它
          	    </div>
				<c:if test="${'格裙'.equals(type_a)}">
					<div class="form-group">
						<label>颜色</label><br/>
						<input type=radio name="colora" value="红格" checked="checked"}>红格&nbsp;&nbsp;
						<input type=radio name="colora" value="粉格">粉格&nbsp;&nbsp;
						<input type=radio name="colora" value="橙格">橙格&nbsp;&nbsp;
						<input type=radio name="colora" value="黄格">黄格&nbsp;&nbsp;
						<input type=radio name="colora" value="绿格">绿格&nbsp;&nbsp;
						<input type=radio name="colora" value="蓝格">蓝格&nbsp;&nbsp;
						<input type=radio name="colora" value="紫格">紫格&nbsp;&nbsp;
						<input type=radio name="colora" value="棕格">棕格&nbsp;&nbsp;
						<input type=radio name="colora" value="黑灰格">黑灰格&nbsp;&nbsp;
					</div>
					<div class="form-group">
						<label>深浅</label><br/>
						<input type=radio name="colorb" value="深格" checked="checked"}>深格&nbsp;&nbsp;
						<input type=radio name="colorb" value="浅格" }>浅格&nbsp;&nbsp;
					</div>
				</c:if>
				<c:if test="${'纯色裙'.equals(type_a)}">
					<div class="form-group">
						<label>颜色</label><br/>
						<input type="text" class="form-control" name="color" value="红色">
					</div>
				</c:if>
          	    <div class="form-group">
          	    	<label>描述</label><br/>
          	    	<input type="text" class="form-control" name="describe" placeholder="写下与这条裙子结缘的经历吧！">
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
					<input type="submit" class="button_for_text" value="添加" onclick="return confirm('确认添加');">
				</div>
  			</form>
		</div></div></div>
</body>
</html>
