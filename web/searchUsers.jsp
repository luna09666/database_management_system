<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<%@page import="java.sql.*"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="css/global.css">
	<script src="js/jquery-3.5.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
</head>

<body>
<ul class="for_ul_global">
	<li class="textlocation">欢迎您!管理员</li>
	<a href="user?type=exit"><button class="button_for_pwd">安全退出</button></a>
</ul>



	<div class="container" style="height:800px;">
		<form action="user?type=search" role="form" method="post">
			<div class="same">
				<input type="text" name="key" value="${key}" class="text_for_search" placeholder="请输入查询关键词" />
				<button type='submit' class="button_for_text">确定</button>
			</div>
		</form>
		<div style="margin-top:40px;height:700px;border-style:dashed;border-width:5px 5px;border-color: lightpink;border-radius: 0.5em;">
			<div style="margin:20px 10px;">
				<table class="table table-striped table table-hover">
				<thead>
				<tr>
					<th>id</th>
					<th>电话号码</th>
					<th>昵称</th>
					<th>密码</th>
					<th>注册时间</th>
					<th>操作</th>
				</tr>
				</thead>
				<tr align="center" class="d">
					<td>${user.id}</td>
					<td>${user.phonenumber}</td>
					<td>${user.name}</td>
					<td>${user.password}</td>
					<td>${user.createtime}</td>
					<td><a href="user?type=modifypre&id=${user.id}">
						<button class="button_for_detail">详情</button></a>&nbsp;&nbsp;
						<a onclick="return confirm('确认删除');" href="user?type=remove&id=${user.id}">
							<button class="button_for_delete">删除</button></a>
					</td>
				</tr>
			</table>

			</div>
		</div>
	</div>
</body>
</html>