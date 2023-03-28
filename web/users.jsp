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
    <li class="textlocation">欢迎您!管理员</li>
    <a href="user?type=exit&userid=${userid}"><button class="button_for_pwd">安全退出</button></a>
</ul>

<div style="position:relative;font-size:40px;text-align:left;margin-left:20%;margin-top:1%;color:#ffa8ef; ">
    用户列表
</div>

<div style="margin-top:40px;text-align: right;margin-right:4%;">
    <a href="adduser.jsp"><button class='btn btn-warning add'>添加</button></a>
    <a href="searchUsers.jsp"><button class='btn btn-warning add'>查询</button></a>
</div>


    <div class="container" style="box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19);">
        <div style="margin-top:10px;border-style:dashed;border-width:5px 5px;border-color: lightpink;border-radius: 0.5em;">
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
            <c:forEach items="${requestScope.users}" var="u">
                <c:if test="${u.id!=2}">
                <tr align="center" class="d">
                    <td>${pageScope.u.id}</td>
                    <td>${pageScope.u.phonenumber}</td>
                    <td>${pageScope.u.name}</td>
                    <td>${pageScope.u.password}</td>
                    <td>${pageScope.u.createtime}</td>
                    <td><a href="user?type=modifypreg&id=${pageScope.u.id}">
                        <button class="button_for_detail">详情</button></a>&nbsp;&nbsp;
                        <a onclick="return confirm('确认删除');" href="user?type=remove&id=${pageScope.u.id}">
                            <button class="button_for_delete">删除</button></a>
                    </td>
                </tr>
                </c:if>
            </c:forEach>
        </table>
            <tr><td colspan="8" align="center">
                <div class="pager">
                    <ul class="clearfix">
                        <li><a href="user?type=query&pageIndex=${param.pageIndex-1}&userid=${userid}">上一页</a></li>
                        <c:forEach var="i" begin="1" end="${pageCount}" step="1">
                            <c:if test="${i==param.pageIndex}">
                                <li class="current"><a href="user?type=query&pageIndex=${i}&userid=${userid}">${i}</a></li>
                            </c:if>
                            <c:if test="${i!=param.pageIndex}">
                                <li><a href="user?type=query&pageIndex=${i}&userid=${userid}">${i}</a></li>
                            </c:if>
                        </c:forEach>
                        <li><a href="user?type=query&pageIndex=${param.pageIndex+1}&userid=${userid}">下一页</a></li>
                    </ul>
                </div>
            </td></tr>
        </div>
    </div>
    </div>

</body>
</html>