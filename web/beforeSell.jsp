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
	<h4 class="text-center"><strong>详情</strong></h4><br/>
	<form action="sell?type=add&userid=${userid}" role="form" method="post" enctype="multipart/form-data">
		<input type="hidden" name="wardrobe" value="wardrobe">
		<div class="form-group">
			<label>id</label>
			<input type="text" class="form-control" name="id" id="id" value="${clothes.id}" readonly>
		</div>
		<div class="form-group">
			<label>名称</label>
			<input type="text" class="form-control" name="name_a" value="${clothes.name}" placeholder="请输入名称">
		</div>
		<div class="form-group">
			<label>买入价格</label>
			<input type="text" class="form-control" name="buyprice" value="${clothes.price}" placeholder="请输入买入价格">
		</div>
		<div class="form-group">
			<label>卖出价格</label>
			<input type="text" class="form-control" name="sellprice" value="${clothes.price*0.76}" placeholder="请输入卖出价格">
		</div>
		<div class="form-group">
			<label>卖出时间</label>
			<input name="selltime" type="datetime-local" class="form-control">
		</div>
		<div class="form-group">
			<label>类型</label><br/>
			<select id="type_m" name="type_a">
				<option ${"格裙".equals(clothes.type)?"selected":""}>格裙</option>
				<option ${"衬衫".equals(clothes.type)?"selected":""}>衬衫</option>
				<option ${"小物".equals(clothes.type)?"selected":""}>小物</option>
				<option ${"毛衣".equals(clothes.type)?"selected":""}>毛衣</option>
				<option ${"马甲".equals(clothes.type)?"selected":""}>马甲</option>
				<option ${"西服".equals(clothes.type)?"selected":""}>西服</option>
				<option ${"鞋子".equals(clothes.type)?"selected":""}>鞋子</option>
				<option ${"水手服".equals(clothes.type)?"selected":""}>水手服</option>
				<option ${"纯色裙".equals(clothes.type)?"selected":""}>纯色裙</option>
				<option ${"连衣裙".equals(clothes.type)?"selected":""}>连衣裙</option>
				<option ${"其它".equals(clothes.type)?"selected":""}>其它</option>
			</select>
		</div>

		<c:if test="${!'小物'.equals(clothes.type)}">
			<div class="form-group">
				<label>尺码</label><br/>
				<input type=radio name="size" value="xxs" ${"xxs".equals(clothes.size)?"checked=checked":""}>XXS&nbsp;&nbsp;
				<input type=radio name="size" value="xs" ${"xs".equals(clothes.size)?"checked=checked":""}>XS&nbsp;&nbsp;
				<input type=radio name="size" value="s" ${"s".equals(clothes.size)?"checked=checked":""}>S&nbsp;&nbsp;
				<input type=radio name="size" value="m" ${"m".equals(clothes.size)?"checked=checked":""}>M&nbsp;&nbsp;
				<input type=radio name="size" value="l" ${"l".equals(clothes.size)?"checked=checked":""}>L<br/>
				<input type=radio name="size" value="xl" ${"xl".equals(clothes.size)?"checked=checked":""}>XL&nbsp;&nbsp;
				<input type=radio name="size" value="xxl" ${"xxl".equals(clothes.size)?"checked=checked":""}>XXL&nbsp;&nbsp;
				<input type=radio name="size" value="均码" ${"average".equals(clothes.size)?"checked=checked":""}>均码&nbsp;&nbsp;
				<input type=radio name="size" value="其它" ${"else".equals(clothes.size)?"checked=checked":""}>其它
			</div>
		</c:if>

		<c:if test="${'衬衫'.equals(clothes.type)}">
			<div class="form-group">
				<label>规格</label><br/>
				<input type=radio name="specs" value="短袖" ${"短袖".equals(clothes.specs)?"checked=checked":""}>短袖&nbsp;&nbsp;
				<input type=radio name="specs" value="中袖" ${"中袖".equals(clothes.specs)?"checked=checked":""}>中袖&nbsp;&nbsp;
				<input type=radio name="specs" value="长袖" ${"长袖".equals(clothes.specs)?"checked=checked":""}>长袖
			</div>
		</c:if>
		<c:if test="${'马甲'.equals(clothes.type)}">
			<div class="form-group">
				<label>款式</label><br/>
				<input type=radio name="specs" value="毛衣马甲" ${"毛衣马甲".equals(clothes.specs)?"checked=checked":""}>毛衣马甲&nbsp;&nbsp;
				<input type=radio name="specs" value="西服马甲" ${"西服马甲".equals(clothes.specs)?"checked=checked":""}>西服马甲&nbsp;
				<input type=radio name="specs" value="其它" ${"其它".equals(clothes.specs)?"checked=checked":""}>其它
			</div>
		</c:if>
		<c:if test="${'毛衣'.equals(clothes.type)}">
			<div class="form-group">
				<label>衣领</label><br/>
				<input type=radio name="specsa" value="v领" ${"v领".equals((clothes.specs).split(" ")[0])?"checked=checked":""}>v领&nbsp;&nbsp;
				<input type=radio name="specsa" value="圆领" ${"圆领".equals((clothes.specs).split(" ")[0])?"checked=checked":""}>圆领&nbsp;&nbsp;
				<input type=radio name="specsa" value="水手领" ${"水手领".equals((clothes.specs).split(" ")[0])?"checked=checked":""}>水手领&nbsp;&nbsp;
				<input type=radio name="specsa" value="其它" ${"其它".equals((clothes.specs).split(" ")[0])?"checked=checked":""}>其它&nbsp;&nbsp;
			</div>
			<div class="form-group">
				<label>厚度</label><br/>
				<input type=radio name="specsb" value="厚款" ${"厚款".equals((clothes.specs).split(" ")[1])?"checked=checked":""}>厚款&nbsp;&nbsp;
				<input type=radio name="specsb" value="薄款" ${"薄款".equals((clothes.specs).split(" ")[1])?"checked=checked":""}>薄款&nbsp;&nbsp;
				<input type=radio name="specsb" value="适中" ${"适中".equals((clothes.specs).split(" ")[1])?"checked=checked":""}>适中
			</div>
			<div class="form-group">
				<label>款式</label><br/>
				<input type=radio name="specsc" value="开衫" ${"开衫".equals((clothes.specs).split(" ")[2])?"checked=checked":""}>开衫&nbsp;&nbsp;
				<input type=radio name="specsc" value="套头" ${"套头".equals((clothes.specs).split(" ")[2])?"checked=checked":""}>套头&nbsp;&nbsp;
			</div>
		</c:if>
		<c:if test="${'西服'.equals(clothes.type)}">
			<div class="form-group">
				<label>长度</label><br/>
				<input type=radio name="specsa" value="长款" ${"长款".equals((clothes.specs).split(" ")[0])?"checked=checked":""}>长款&nbsp;&nbsp;
				<input type=radio name="specsa" value="正常" ${"正常".equals((clothes.specs).split(" ")[0])?"checked=checked":""}>正常&nbsp;&nbsp;
				<input type=radio name="specsa" value="短款" ${"短款".equals((clothes.specs).split(" ")[0])?"checked=checked":""}>短款
			</div>
			<div class="form-group">
				<label>衣领</label><br/>
				<input type=radio name="specsb" value="正常领" ${"正常领".equals((clothes.specs).split(" ")[1])?"checked=checked":""}>正常领&nbsp;&nbsp;
				<input type=radio name="specsb" value="无领" ${"无领".equals((clothes.specs).split(" ")[1])?"checked=checked":""}>无领&nbsp;&nbsp;
				<input type=radio name="specsb" value="水手领" ${"水手领".equals((clothes.specs).split(" ")[1])?"checked=checked":""}>水手领&nbsp;&nbsp;
				<input type=radio name="specsb" value="异形领" ${"异形领".equals((clothes.specs).split(" ")[1])?"checked=checked":""}>异形领&nbsp;&nbsp;
			</div>
		</c:if>
		<c:if test="${'水手服'.equals(clothes.type)}">
			<div class="form-group">
				<label>襟线条数</label><br/>
				<input type="text" class="form-control" name="specsa" value="${clothes.specs.split("本 ")[0]}">本
			</div>
			<div class="form-group">
				<label>款类</label><br/>
				<input type=radio name="specsb" value="夏服" ${"夏服".equals(clothes.specs.split("本 ")[1])?"checked=checked":""}>夏服&nbsp;&nbsp;
				<input type=radio name="specsb" value="中间服" ${"中间服".equals(clothes.specs)?"checked=checked":""}>中间服&nbsp;&nbsp;
				<input type=radio name="specsb" value="冬服" ${"冬服".equals(clothes.specs)?"checked=checked":""}>冬服
			</div>
		</c:if>
		<c:if test="${'连衣裙'.equals(clothes.type)}">
			<div class="form-group">
				<label>裙型</label><br/>
				<input type=radio name="specs" value="护奶裙" ${"护奶裙".equals(clothes.specs)?"checked=checked":""}>护奶裙&nbsp;&nbsp;
				<input type=radio name="specs" value="卡奶裙" ${"卡奶裙".equals(clothes.specs)?"checked=checked":""}>卡奶裙&nbsp;&nbsp;
				<input type=radio name="specs" value="连衣裙" ${"连衣裙".equals(clothes.specs)?"checked=checked":""}>连衣裙
			</div>
		</c:if>
		<c:if test="${'其它'.equals(clothes.type)}">
			<div class="form-group">
				<label>自定义类型</label><br/>
				<input type="text" class="form-control" name="specs">
			</div>
		</c:if>
		<c:if test="${'格裙'.equals(clothes.type)||'纯色裙'.equals(clothes.type)}">
			<div class="form-group">
				<label>裙褶</label><br/>
				<input type=radio name="specs" value="轮褶" ${"轮褶".equals(clothes.specs)?"checked=checked":""}>轮褶&nbsp;&nbsp;
				<input type=radio name="specs" value="箱褶" ${"箱褶".equals(clothes.specs)?"checked=checked":""}>箱褶&nbsp;&nbsp;
				<input type=radio name="specs" value="前箱褶" ${"前箱褶".equals(clothes.specs)?"checked=checked":""}>前箱褶&nbsp;&nbsp;
				<input type=radio name="specs" value="无褶" ${"无褶".equals(clothes.specs)?"checked=checked":""}>无褶&nbsp;&nbsp;
				<input type=radio name="specs" value="其它" ${"其它".equals(clothes.specs)?"checked=checked":""}>其它
			</div>
			<div class="form-group">
				<label>腰围</label><br/>
				<input type="text" class="form-control" name="waist" value="${clothes.waist}">
			</div>
			<div class="form-group">
				<label>裙长</label><br/>
				<input type="text" class="form-control" name="len" value="${clothes.len}">
			</div>
		</c:if>


		<c:if test="${'格裙'.equals(clothes.type)}">
			<div class="form-group">
				<label>颜色</label><br/>
				<input type=radio name="colora" value="红格" ${"红格".equals(clothes.color.split(" ")[0])?"checked=checked":""}>红格&nbsp;&nbsp;
				<input type=radio name="colora" value="粉格" ${"粉格".equals(clothes.color.split(" ")[0])?"checked=checked":""}>粉格&nbsp;&nbsp;
				<input type=radio name="colora" value="橙格" ${"橙格".equals(clothes.color.split(" ")[0])?"checked=checked":""}>橙格&nbsp;&nbsp;
				<input type=radio name="colora" value="黄格" ${"黄格".equals(clothes.color.split(" ")[0])?"checked=checked":""}>黄格&nbsp;&nbsp;
				<input type=radio name="colora" value="绿格" ${"绿格".equals(clothes.color.split(" ")[0])?"checked=checked":""}>绿格&nbsp;&nbsp;
				<input type=radio name="colora" value="蓝格" ${"蓝格".equals(clothes.color.split(" ")[0])?"checked=checked":""}>蓝格&nbsp;&nbsp;
				<input type=radio name="colora" value="紫格" ${"紫格".equals(clothes.color.split(" ")[0])?"checked=checked":""}>紫格&nbsp;&nbsp;
				<input type=radio name="colora" value="棕格" ${"棕格".equals(clothes.color.split(" ")[0])?"checked=checked":""}>棕格&nbsp;&nbsp;
				<input type=radio name="colora" value="黑灰格" ${"黑灰格".equals(clothes.color.split(" ")[0])?"checked=checked":""}>黑灰格&nbsp;&nbsp;
			</div>
			<div class="form-group">
				<label>深浅</label><br/>
				<input type=radio name="colorb" value="深格" ${"深格".equals(clothes.color.split(" ")[1])?"checked=checked":""}>深格&nbsp;&nbsp;
				<input type=radio name="colorb" value="浅格" ${"浅格".equals(clothes.color.split(" ")[1])?"checked=checked":""}>浅格&nbsp;&nbsp;
			</div>
		</c:if>
		<c:if test="${'水手服'.equals(clothes.type)}">
			<div class="form-group">
				<label>衣服本体颜色</label><br/>
				<input type="text" class="form-control" name="colora" value="${clothes.color.split(" ")[0]}">
			</div>
			<div class="form-group">
				<label>襟线颜色</label><br/>
				<input type="text" class="form-control" name="colorb" value="${clothes.color.split(" ")[1]}">
			</div>
		</c:if>
		<c:if test="${(!'格裙'.equals(clothes.type))&&(!'水手服'.equals(clothes.type))}">
			<div class="form-group">
				<label>颜色</label><br/>
				<input type="text" class="form-control" name="color" value="${clothes.color}">
			</div>
		</c:if>


		<div class="form-group">
			<label>描述</label>
			<input type="text" class="form-control" name="describe" value="${clothes.describe1}">
		</div>
		<div class="form-group">
			<label>图片</label><br/>
			<input type="hidden" name="pic" value="${clothes.pic}">
			<input type="file" name="filePic" id="filePic" value="${clothes.pic}">
		</div>
		<td rowspan="9" valign="top" >
			<fieldset style="width: 210px; height: 370px;">
				<legend>图片预览</legend>
				<img id="imgPic" src="${clothes.pic}" width="200px" height="300px"/>
			</fieldset>
		</td>
		<div>
			<input type="submit" value="卖出" class="button_for_text" onclick="return confirm('确认卖出');">
		</div>

	</form>
</div>
<br>
	</div></div>

</body>
</html>
