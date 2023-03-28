<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title >穿搭信息添加</title>
	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="css/global.css">
	<script src="js/jquery-3.5.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script>
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
		<li><a class="for_a_global" href="sell?type=query&pageIndex=1&type_q=all&userid=${userid}">卖出</a></li>
		<li><a class="for_a_global" href="user?type=sta&type_sta=all&userid=${userid}">统计</a></li>
		<li><a class="for_a_global active" href="wear?type=query&pageIndex=1&userid=${userid}">穿搭</a></li>
	</div>
</ul>
<div class="container">
	<div style="margin-top:20px;margin-bottom:40px;border-style:dashed;border-width:5px 5px;border-color: lightpink;border-radius: 0.5em;">
		<div style="margin:50px;">
	<h4 class="text-center"><strong>穿搭信息添加</strong></h4><br/>
	<form action="wear?type=add&userid=${userid}" role="form" method="post" enctype="multipart/form-data">
		<div class="form-group">
			<label>标题</label><span class="need_26EMK">*</span>
			<input type="text" class="form-control" name="title" placeholder="请输入标题">
		</div>
		<div class="form-group">
			<label>内容</label><span class="need_26EMK">*</span>
			<input type="text" class="form-control" name="content" placeholder="请输入内容">
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
		<button type='submit' class="button_for_text" onclick="return confirm('确认添加');">确定</button>
	</form>
		</div></div></div>

</body>
</html>
