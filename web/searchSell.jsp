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



<div class="container" style="height:800px;">
	<form action="sell?type=search&type_s=all&pageIndex=1&userid=${userid}" role="form" method="post">
		<div class="same">
			<input type="text" name="key" value="${key}" class="text_for_search" placeholder="请输入查询关键词" />
			<button type='submit' class="button_for_text">确定</button>
		</div>
	</form>
	<div style="margin-top:40px;height:700px;border-style:dashed;border-width:5px 5px;border-color: lightpink;border-radius: 0.5em;">
		<div style="margin:20px 10px;">
			<ul class="for_wardrobe_ul">
				<li><a ${"id".equals(type_s)?"class='active'":""} href="sell?type=search&pageIndex=${pageIndex}&userid=${userid}&key=${key}&type_s=id">id</a></li>
				<li><a ${"name".equals(type_s)?"class='active'":""} href="sell?type=search&pageIndex=${pageIndex}&userid=${userid}&key=${key}&type_s=name">名称</a></li>
				<li><a ${"type".equals(type_s)?"class='active'":""} href="sell?type=search&pageIndex=${pageIndex}&userid=${userid}&key=${key}&type_s=type">类型</a></li>
			</ul>

			<table class="table table-striped table table-hover">
		<thead>
		<tr>
			<th>名称</th>
			<th>类型</th>
			<th>卖出价格</th>
			<th>买入价格</th>
			<th>写入时间</th>
			<th>更新时间</th>
			<th>操作</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${requestScope.sells}" var="s">
			<tr align="center" class="d">
				<td>${pageScope.s.name}</td>
				<td>${pageScope.s.type}</td>
				<td>${pageScope.s.sellprice}</td>
				<td>${pageScope.s.buyprice}</td>
				<td>${pageScope.s.createtime}</td>
				<td>${pageScope.s.updatetime}</td>
				<td><a href="sell?type=modifypre&id=${pageScope.s.id}&userid=${userid}">
					<button class="button_for_detail">详情</button></a>&nbsp;&nbsp;
					<a onclick="return confirm('确认删除');" href="sell?type=remove&id=${pageScope.s.id}&userid=${userid}">
						<button class="button_for_delete">删除</button>
					</a>
				</td>
			</tr>
		</c:forEach>


		</tbody>
	</table>
			<tr><td colspan="8" align="center">
				<div class="pager">
					<ul class="clearfix">
						<li><a href="sell?type=search&pageIndex=${param.pageIndex-1}&type_s=${type_s}&userid=${userid}&key=${key}">上一页</a></li>
						<c:forEach var="i" begin="1" end="${pageCount}" step="1">
							<c:if test="${i==param.pageIndex}">
								<li class="current"><a href="sell?type=search&pageIndex=${i}&type_s=${type_s}&userid=${userid}&key=${key}">${i}</a></li>
							</c:if>
							<c:if test="${i!=param.pageIndex}">
								<li><a href="sell?type=search&pageIndex=${i}&type_s=${type_s}&userid=${userid}&key=${key}">${i}</a></li>
							</c:if>
						</c:forEach>
						<li><a href="sell?type=search&pageIndex=${param.pageIndex+1}&type_s=${type_s}&type_q=${type_q}&userid=${userid}&key=${key}">下一页</a></li>
					</ul>
				</div>
			</td></tr>
</div>
	</div>
</div>
</body>

</html>