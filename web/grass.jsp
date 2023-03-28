<%@ page contentType="text/html"%>  
<%@page pageEncoding="UTF-8"%>  
<%@page import="java.sql.*" %>  <%--导入java.sql包--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title >title</title>
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
		<li><a class="for_a_global active" href="grass?type=query&pageIndex=1&type_q_g=all&type_q=all&userid=${userid}">草单</a></li>
		<li><a class="for_a_global" href="sell?type=query&pageIndex=1&type_q=all&userid=${userid}">卖出</a></li>
		<li><a class="for_a_global" href="user?type=sta&type_sta=all&userid=${userid}">统计</a></li>
		<li><a class="for_a_global" href="wear?type=query&pageIndex=1&userid=${userid}">穿搭</a></li>
	</div>
</ul>

<div style="position:relative;font-size:40px;text-align:left;margin-left:20%;margin-top:1%;color:#ffa8ef;">
	我的草单
</div>

<div style="margin-top:40px;text-align: right;margin-right:4%;">
	<a href="grass?type=addtemp&userid=${userid}"><button class='btn btn-warning add'>添加</button></a>
	<a href="grass?type=searchpre&userid=${userid}"><button class='btn btn-warning add"><button class='btn btn-warning add'>查询</button></a>
</div>


<div class="container">
	<div style="margin-top:20px;margin-bottom:40px;border-style:dashed;border-width:5px 5px;border-color: lightpink;border-radius: 0.5em;">
	<div style="border-style:solid;border-width:20px 10px;border-color: #ffffff;">
		<ul class="for_wardrobe_ul">
			<li><a ${"all".equals(type_q_g)?"class='active'":""} href="grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid=${userid}">全部</a></li>
			<li><a ${"present".equals(type_q_g)?"class='active'":""} href="grass?type=query&pageIndex=1&type_q=all&type_q_g=present&userid=${userid}">现货</a></li>
			<li><a ${"front".equals(type_q_g)?"class='active'":""} href="grass?type=query&pageIndex=1&type_q=all&type_q_g=front&userid=${userid}">预售</a></li>
		</ul>


	<table class="table table-striped table table-hover" >
	<c:if test="${'all'.equals(type_q_g)}">
			<thead>
			<tr>
				<th>名称</th>
				<th>价格</th>
				<th>类型</th>
				<th>状态</th>
				<th>写入时间</th>
				<th>更新时间</th>
				<th>操作</th>
			</tr>
			</thead>
			<c:forEach items="${requestScope.grasses}" var="g">
				<tr align="center" class="d">
					<td>${pageScope.g.name}</td>
					<td>${pageScope.g.price}</td>
					<td>${pageScope.g.type}</td>
					<td>${pageScope.g.flag.split(' ')[0]}</td>
					<td>${pageScope.g.createtime}</td>
					<td>${pageScope.g.updatetime}</td>
					<td><a href="grass?type=modifypre&id=${pageScope.g.id}&userid=${userid}">
						<button class="button_for_detail">详情</button></a>&nbsp;&nbsp;
						<a onclick="return confirm('确认付定金');" href="grass?type=payfront&id=${pageScope.g.id}&userid=${userid}">
							<button class="button_for_payfront">付定金</button></a>
						<a href="grass?type=buypre&id=${pageScope.g.id}&userid=${userid}">
							<button class="button_for_buy">购买</button></a>
						<a onclick="return confirm('确认删除');" href="grass?type=remove&id=${pageScope.g.id}&userid=${userid}">
							<button class="button_for_delete">删除</button></a>
					</td>
				</tr>
			</c:forEach>
	</c:if>

	<c:if test="${'present'.equals(type_q_g)}">
			<thead>
			<tr>
				<th>名称</th>
				<th>价格</th>
				<th>类型</th>
				<th>状态</th>
				<th>写入时间</th>
				<th>更新时间</th>
				<th>操作</th>
			</tr>
			</thead>
			<c:forEach items="${requestScope.grasses}" var="g">
				<tr align="center" class="d">
					<td>${pageScope.g.name}</td>
					<td>${pageScope.g.price}</td>
					<td>${pageScope.g.type}</td>
					<td>${pageScope.g.flag}</td>
					<td>${pageScope.g.createtime}</td>
					<td>${pageScope.g.updatetime}</td>
					<td><a href="grass?type=modifypre&id=${pageScope.g.id}&userid=${userid}">
						<button class="button_for_detail">详情</button></a>&nbsp;&nbsp;
						<a href="grass?type=buypre&id=${pageScope.g.id}&userid=${userid}">
							<button class="button_for_buy">购买</button></a>
						<a onclick="return confirm('确认删除');" href="grass?type=remove&id=${pageScope.g.id}&userid=${userid}">
							<button class="button_for_delete">删除</button></a>
					</td>
				</tr>
			</c:forEach>
	</c:if>

	<c:if test="${'front'.equals(type_q_g)}">
			<thead>
			<tr>
				<th>名称</th>
				<th>总价格</th>
				<th>定金价格</th>
				<th>尾款价格</th>
				<th>再贩时间</th>
				<th>类型</th>
				<th>状态</th>
				<th>操作</th>
			</tr>
			</thead>
			<c:forEach items="${requestScope.grasses}" var="g">
				<tr align="center" class="d">
					<td>${pageScope.g.name}</td>
					<td>${pageScope.g.price}</td>
					<td>${pageScope.g.frontprice}</td>
					<td>${pageScope.g.tailprice}</td>
					<td>${pageScope.g.buytime}</td>
					<td>${pageScope.g.type}</td>
					<td>${pageScope.g.flag.split(' ')[1]}</td>
					<td><a href="grass?type=modifypre&id=${pageScope.g.id}&userid=${userid}">
						<button class="button_for_detail">详情</button></a>&nbsp;&nbsp;
						<a onclick="return confirm('确认付定金');" href="grass?type=payfront&id=${pageScope.g.id}&userid=${userid}">
							<button class="button_for_payfront">付定金</button></a>
						<a href="grass?type=buypre&id=${pageScope.g.id}&userid=${userid}">
							<button class="button_for_buy">购买</button></a>
						<a onclick="return confirm('确认删除');" href="grass?type=remove&id=${pageScope.g.id}&userid=${userid}">
							<button class="button_for_delete">删除</button></a>
					</td>
				</tr>
			</c:forEach>
	</c:if>

</table>
		<tr><td colspan="8" align="center">
			<div class="pager">
				<ul class="clearfix">
					<li><a href="grass?type=query&pageIndex=${param.pageIndex-1}&type_q=${type_q}&type_q_g=${type_q_g}&userid=${userid}">上一页</a></li>
					<c:forEach var="i" begin="1" end="${pageCount}" step="1">
						<c:if test="${i==param.pageIndex}">
							<li class="current"><a href="grass?type=query&pageIndex=${i}&type_q=${type_q}&type_q_g=${type_q_g}&userid=${userid}">${i}</a></li>
						</c:if>
						<c:if test="${i!=param.pageIndex}">
							<li><a href="grass?type=query&pageIndex=${i}&type_q=${type_q}&type_q_g=${type_q_g}&userid=${userid}">${i}</a></li>
						</c:if>
					</c:forEach>
					<li><a href="grass?type=query&pageIndex=${param.pageIndex+1}&type_q=${type_q}&type_q_g=${type_q_g}&userid=${userid}">下一页</a></li>
				</ul>
			</div>
		</td></tr>
	</div>
	</div>
</div>
</body>
</html>