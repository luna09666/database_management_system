<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@page import="java.sql.*" %>  <%--导入java.sql包--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title >详情</title>
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
			$("#filePic").change(function(){
				var path = getFullPath($(this)[0]);
				console.log(path);
				$("#imgPic").prop("src",path);
			});
		});
	</script>
</head>
<body>
<ul class="for_ul_global">
	<li class="textlocation">欢迎您!管理员</li>
	<a href="user?type=exit&userid=${userid}"><button class="button_for_pwd">安全退出</button></a>
</ul>
<div class="container">
	<div style="margin-top:20px;margin-bottom:40px;border-style:dashed;border-width:5px 5px;border-color: lightpink;border-radius: 0.5em;">
		<div style="margin:50px;">
	<h4 class="text-center" style="color:#f86a83;"><strong>详情</strong></h4><br/>
	<form action="user?type=modifyuser" role="form" method="post" enctype="multipart/form-data">
		<input type="hidden" name="userid" id="userid" value="${userid}">
		<div class="form-group">
			<label>id</label><span class="need_26EMK">*</span>
			<input type="text" class="form-control" name="id" id="id" value="${userm.id}" readonly>
		</div>
		<div class="form-group">
			<label>电话号码</label><span class="need_26EMK">*</span>
			<input type="text" class="form-control" name="phonenumber" value="${userm.phonenumber}" placeholder="请输入电话号码">
		</div>

		<div class="form-group">
			<label>昵称</label><span class="need_26EMK">*</span>
			<input type="text" class="form-control" name="name" value="${userm.name}" placeholder="请输入昵称">
		</div>

		<div class="form-group">
			<label>密码</label><span class="need_26EMK">*</span>
			<input type="text" class="form-control" name="password" value="${userm.password}" placeholder="请输入密码">
		</div>

		<div class="form-group">
			<label>性别</label><br/>
			<input type=radio name="sex" value="女" ${"女".equals(userm.sex)?"checked=checked":""}>女&nbsp;&nbsp;
			<input type=radio name="sex" value="男" ${"男".equals(userm.sex)?"checked=checked":""}>男&nbsp;&nbsp;
		</div>

		<div class="form-group">
			<label>简介</label><span class="need_26EMK">*</span>
			<input type="text" class="form-control" name="describe1" value="${userm.describe1}" placeholder="请输入简介">
		</div>

		<div class="form-group">
			<label>地址</label>
			<input type="text" class="form-control" name="position" value="${userm.position}">
		</div>

		<div class="form-group">
			<label>头像</label><br/>
			<input type="hidden" name="pic" value="${userm.pic}">
			<input type="file" name="filePic" id="filePic" value="${userm.pic}">
		</div>
		<td rowspan="9" valign="top" >
			<fieldset style="width: 210px; height: 370px;">
				<legend>图片预览</legend>
				<img id="imgPic" src="${userm.pic}" width="200px" height="300px"/>
			</fieldset>
		</td>

		<div style="margin:80px 45% 60px;">
			<input type="submit" value="修 改" class="button_for_text" style="width:150px;height:40px;" onclick="return confirm('确认修改');">
		</div>

	</form>
</div>
<br>
	</div></div>
</body>
</html>
