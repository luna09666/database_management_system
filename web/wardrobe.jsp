<%@ page contentType="text/html"%>
<%@page pageEncoding="UTF-8" isELIgnored="false"%>
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
        <li><a class="for_a_global active" href="wardrobe?type=query&pageIndex=1&type_q=all&userid=${userid}">衣柜</a></li>
        <li><a class="for_a_global" href="grass?type=query&pageIndex=1&type_q_g=all&type_q=all&userid=${userid}">草单</a></li>
        <li><a class="for_a_global" href="sell?type=query&pageIndex=1&type_q=all&userid=${userid}">卖出</a></li>
        <li><a class="for_a_global" href="user?type=sta&type_sta=all&userid=${userid}">统计</a></li>
        <li><a class="for_a_global" href="wear?type=query&pageIndex=1&userid=${userid}">穿搭</a></li>
    </div>
</ul>

<div style="position:relative;font-size:40px;text-align:left;margin-left:20%;margin-top:1%;color:#ffa8ef; ">
    我的衣柜
</div>

<div style="margin-top:40px;text-align: right;margin-right:4%;">
    <a href="wardrobe?type=addtemp&userid=${userid}"><button class='btn btn-warning add'>添加</button></a>
    <a href="wardrobe?type=searchpre&type_s=all&userid=${userid}"><button class='btn btn-warning add'>查询</button></a>
    <a href="excel?userid=${userid}"><button class='btn btn-warning add'>导出Excel</button></a>
</div>


    <div class="container" style="box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19);">
        <div style="margin-top:10px;border-style:dashed;border-width:5px 5px;border-color: lightpink;border-radius: 0.5em;">
        <div style="margin:20px 10px;">
            <ul class="for_wardrobe_ul">
                <li><a ${"all".equals(type_q)?"class='active'":""} href="wardrobe?type=query&pageIndex=1&type_q=all&userid=${userid}">全部</a></li>
                <li><a ${"skirt".equals(type_q)?"class='active'":""} href="wardrobe?type=query&pageIndex=1&type_q=skirt&userid=${userid}">格裙</a></li>
                <li><a ${"shirt".equals(type_q)?"class='active'":""} href="wardrobe?type=query&pageIndex=1&type_q=shirt&userid=${userid}">衬衫</a></li>
                <li><a ${"little".equals(type_q)?"class='active'":""} href="wardrobe?type=query&pageIndex=1&type_q=little&userid=${userid}">小物</a></li>
                <li><a ${"sweater".equals(type_q)?"class='active'":""} href="wardrobe?type=query&pageIndex=1&type_q=sweater&userid=${userid}">毛衣</a></li>
                <li><a ${"vest".equals(type_q)?"class='active'":""} href="wardrobe?type=query&pageIndex=1&type_q=vest&userid=${userid}">马甲</a></li>
                <li><a ${"suit".equals(type_q)?"class='active'":""} href="wardrobe?type=query&pageIndex=1&type_q=suit&userid=${userid}">西服</a></li>
                <li><a ${"shoes".equals(type_q)?"class='active'":""} href="wardrobe?type=query&pageIndex=1&type_q=shoes&userid=${userid}">鞋子</a></li>
                <li><a ${"seawoman".equals(type_q)?"class='active'":""} href="wardrobe?type=query&pageIndex=1&type_q=seawoman&userid=${userid}">水手服</a></li>
                <li><a ${"pureskirt".equals(type_q)?"class='active'":""} href="wardrobe?type=query&pageIndex=1&type_q=pureskirt&userid=${userid}">纯色裙</a></li>
                <li><a ${"dress".equals(type_q)?"class='active'":""} href="wardrobe?type=query&pageIndex=1&type_q=dress&userid=${userid}">连衣裙</a></li>
                <li><a ${"others".equals(type_q)?"class='active'":""} href="wardrobe?type=query&pageIndex=1&type_q=others&userid=${userid}">其它</a></li>
            </ul>

        <table class="table table-striped table table-hover">
            <c:if test="${'all'.equals(type_q)}">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>价格</th>
                    <th>类型</th>
                    <th>写入时间</th>
                    <th>更新时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <c:forEach items="${requestScope.cclothes}" var="c">
                    <tr align="center" class="d">
                        <td>${pageScope.c.name}</td>
                        <td>${pageScope.c.price}</td>
                        <c:choose>
                            <c:when test="${'others'.equals(type_q)}">
                                <td>${pageScope.c.specs}</td>
                            </c:when>
                            <c:otherwise>
                                <td>${pageScope.c.type}</td>
                            </c:otherwise>
                        </c:choose>
                        <td>${pageScope.c.createtime}</td>
                        <td>${pageScope.c.updatetime}</td>
                        <td><a href="wardrobe?type=modifypre&id=${pageScope.c.id}&userid=${userid}">
                                <button class="button_for_detail">详情</button></a>&nbsp;&nbsp;
                            <a href="wardrobe?type=sell&id=${pageScope.c.id}&userid=${userid}">
                                <button class="button_for_sell">卖出</button></a>
                            <a onclick="return confirm('确认删除');" href="wardrobe?type=remove&id=${pageScope.c.id}&userid=${userid}">
                                <button class="button_for_delete">删除</button></a>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>

            <c:if test="${'skirt'.equals(type_q)||'pureskirt'.equals(type_q)}">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>价格</th>
                    <th>类型</th>
                    <th>尺码</th>
                    <th>裙长</th>
                    <th>腰围</th>
                    <th>褶子类型</th>
                    <th>颜色</th>
                    <th>操作</th>
                </tr>
                </thead>
                <c:forEach items="${requestScope.cclothes}" var="c">
                    <tr align="center" class="d">
                        <td>${pageScope.c.name}</td>
                        <td>${pageScope.c.price}</td>
                        <td>${pageScope.c.type}</td>
                        <td>${pageScope.c.size}</td>
                        <td>${pageScope.c.len}</td>
                        <td>${pageScope.c.waist}</td>
                        <td>${pageScope.c.specs}</td>
                        <td>${pageScope.c.color}</td>
                        <td><a href="wardrobe?type=modifypre&id=${pageScope.c.id}&userid=${userid}">
                            <button class="button_for_detail">详情</button></a>&nbsp;&nbsp;
                            <a href="wardrobe?type=sell&id=${pageScope.c.id}&userid=${userid}">
                                <button class="button_for_sell">卖出</button></a>
                            <a onclick="return confirm('确认删除');" href="wardrobe?type=remove&id=${pageScope.c.id}&userid=${userid}">
                                <button class="button_for_delete">删除</button></a>
                        </td>
            </tr>
                </c:forEach>
            </c:if>

            <c:if test="${'shirt'.equals(type_q)}">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>价格</th>
                    <th>类型</th>
                    <th>尺码</th>
                    <th>规格</th>
                    <th>颜色</th>
                    <th>操作</th>
                </tr>
                </thead>
                <c:forEach items="${requestScope.cclothes}" var="c">
                    <tr align="center" class="d">
                        <td>${pageScope.c.name}</td>
                        <td>${pageScope.c.price}</td>
                        <td>${pageScope.c.type}</td>
                        <td>${pageScope.c.size}</td>
                        <td>${pageScope.c.specs}</td>
                        <td>${pageScope.c.color}</td>
                        <td><a href="wardrobe?type=modifypre&id=${pageScope.c.id}&userid=${userid}">
                            <button class="button_for_detail">详情</button></a>&nbsp;&nbsp;
                            <a href="wardrobe?type=sell&id=${pageScope.c.id}&userid=${userid}">
                                <button class="button_for_sell">卖出</button></a>
                            <a onclick="return confirm('确认删除');" href="wardrobe?type=remove&id=${pageScope.c.id}&userid=${userid}">
                                <button class="button_for_delete">删除</button></a>
                        </td>
            </tr>
                </c:forEach>
            </c:if>

            <c:if test="${'little'.equals(type_q)}">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>价格</th>
                    <th>类型</th>
                    <th>颜色</th>
                    <th>操作</th>
                </tr>
                </thead>
                <c:forEach items="${requestScope.cclothes}" var="c">
                    <tr align="center" class="d">
                        <td>${pageScope.c.name}</td>
                        <td>${pageScope.c.price}</td>
                        <td>${pageScope.c.type}</td>
                        <td>${pageScope.c.color}</td>
                        <td><a href="wardrobe?type=modifypre&id=${pageScope.c.id}&userid=${userid}">
                            <button class="button_for_detail">详情</button></a>&nbsp;&nbsp;
                            <a href="wardrobe?type=sell&id=${pageScope.c.id}&userid=${userid}">
                                <button class="button_for_sell">卖出</button></a>
                            <a onclick="return confirm('确认删除');" href="wardrobe?type=remove&id=${pageScope.c.id}&userid=${userid}">
                                <button class="button_for_delete">删除</button></a>
                        </td>
            </tr>
                </c:forEach>
            </c:if>

            <c:if test="${'sweater'.equals(type_q)||'suit'.equals(type_q)||'seawoman'.equals(type_q)||'dress'.equals((type_q))}">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>价格</th>
                    <th>类型</th>
                    <th>尺码</th>
                    <th>规格</th>
                    <th>颜色</th>
                    <th>操作</th>
                </tr>
                </thead>
                <c:forEach items="${requestScope.cclothes}" var="c">
                    <tr align="center" class="d">
                        <td>${pageScope.c.name}</td>
                        <td>${pageScope.c.price}</td>
                        <td>${pageScope.c.type}</td>
                        <td>${pageScope.c.size}</td>
                        <td>${pageScope.c.specs}</td>
                        <td>${pageScope.c.color}</td>
                        <td><a href="wardrobe?type=modifypre&id=${pageScope.c.id}&userid=${userid}">
                            <button class="button_for_detail">详情</button></a>&nbsp;&nbsp;
                            <a href="wardrobe?type=sell&id=${pageScope.c.id}&userid=${userid}">
                                <button class="button_for_sell">卖出</button></a>
                            <a onclick="return confirm('确认删除');" href="wardrobe?type=remove&id=${pageScope.c.id}&userid=${userid}">
                                <button class="button_for_delete">删除</button></a>
                        </td>
            </tr>
                </c:forEach>
            </c:if>

            <c:if test="${'vest'.equals(type_q)}">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>价格</th>
                    <th>类型</th>
                    <th>尺码</th>
                    <th>款式</th>
                    <th>颜色</th>
                    <th>操作</th>
                </tr>
                </thead>
                <c:forEach items="${requestScope.cclothes}" var="c">
                    <tr align="center" class="d">
                        <td>${pageScope.c.name}</td>
                        <td>${pageScope.c.price}</td>
                        <td>${pageScope.c.type}</td>
                        <td>${pageScope.c.size}</td>
                        <td>${pageScope.c.specs}</td>
                        <td>${pageScope.c.color}</td>
                        <td><a href="wardrobe?type=modifypre&id=${pageScope.c.id}&userid=${userid}">
                            <button class="button_for_detail">详情</button></a>&nbsp;&nbsp;
                            <a href="wardrobe?type=sell&id=${pageScope.c.id}&userid=${userid}">
                                <button class="button_for_sell">卖出</button></a>
                            <a onclick="return confirm('确认删除');" href="wardrobe?type=remove&id=${pageScope.c.id}&userid=${userid}">
                                <button class="button_for_delete">删除</button></a>
                        </td>
            </tr>
                </c:forEach>
            </c:if>

            <c:if test="${'shoes'.equals(type_q)}">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>价格</th>
                    <th>类型</th>
                    <th>尺码</th>
                    <th>颜色</th>
                    <th>操作</th>
                </tr>
                </thead>
                <c:forEach items="${requestScope.cclothes}" var="c">
                    <tr align="center" class="d">
                        <td>${pageScope.c.name}</td>
                        <td>${pageScope.c.price}</td>
                        <td>${pageScope.c.type}</td>
                        <td>${pageScope.c.size}</td>
                        <td>${pageScope.c.color}</td>
                        <td><a href="wardrobe?type=modifypre&id=${pageScope.c.id}&userid=${userid}">
                            <button class="button_for_detail">详情</button></a>&nbsp;&nbsp;
                            <a href="wardrobe?type=sell&id=${pageScope.c.id}&userid=${userid}">
                                <button class="button_for_sell">卖出</button></a>
                            <a onclick="return confirm('确认删除');" href="wardrobe?type=remove&id=${pageScope.c.id}&userid=${userid}">
                                <button class="button_for_delete">删除</button></a>
                        </td>
            </tr>
                </c:forEach>
            </c:if>

            <c:if test="${'others'.equals(type_q)}">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>价格</th>
                    <th>类型</th>
                    <th>尺码</th>
                    <th>颜色</th>
                    <th>操作</th>
                </tr>
                </thead>
                <c:forEach items="${requestScope.cclothes}" var="c">
                    <tr align="center" class="d">
                        <td>${pageScope.c.name}</td>
                        <td>${pageScope.c.price}</td>
                        <td>${pageScope.c.specs}</td>
                        <td>${pageScope.c.size}</td>
                        <td>${pageScope.c.color}</td>
                        <td><a href="wardrobe?type=modifypre&id=${pageScope.c.id}&userid=${userid}">
                            <button class="button_for_detail">详情</button></a>&nbsp;&nbsp;
                            <a href="wardrobe?type=sell&id=${pageScope.c.id}&userid=${userid}">
                                <button class="button_for_sell">卖出</button></a>
                            <a onclick="return confirm('确认删除');" href="wardrobe?type=remove&id=${pageScope.c.id}&userid=${userid}">
                                <button class="button_for_delete">删除</button></a>
                        </td>
            </tr>
                </c:forEach>
            </c:if>
        </table>
            <tr><td colspan="8" align="center">
                <div class="pager">
                    <ul class="clearfix">
                        <li><a href="wardrobe?type=query&pageIndex=${param.pageIndex-1}&type_q=${type_q}&userid=${userid}">上一页</a></li>
                        <c:forEach var="i" begin="1" end="${pageCount}" step="1">
                            <c:if test="${i==param.pageIndex}">
                                <li class="current"><a href="wardrobe?type=query&pageIndex=${i}&type_q=${type_q}&userid=${userid}">${i}</a></li>
                            </c:if>
                            <c:if test="${i!=param.pageIndex}">
                                <li><a href="wardrobe?type=query&pageIndex=${i}&type_q=${type_q}&userid=${userid}">${i}</a></li>
                            </c:if>
                        </c:forEach>
                        <li><a href="wardrobe?type=query&pageIndex=${param.pageIndex+1}&type_q=${type_q}&userid=${userid}">下一页</a></li>
                    </ul>
                </div>
            </td></tr>
        </div>
    </div>
    </div>

</body>
</html>