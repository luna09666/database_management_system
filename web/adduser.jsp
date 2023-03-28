<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <link rel="stylesheet" type="text/css" href="css/login.css">
    <link rel="stylesheet" type="text/css" href="css/reset.css">
</head>
<body>
    <div class="container">
        <div class="title">注册</div>
        <form id="fmLogin" method="post" action="user?type=adduser" >
            <div class="block">
                <input type="text" name="phonenumber" placeholder="请输入手机号码" />
                <input type="password" name="pwd" placeholder="请输入密码" />
                <input type="password" name="pwda" placeholder="请确认密码" />
            </div>
            <div class="block1">
                <button class="button" type="submit">添加</button>
            </div>
        </form>
    </div>
</body>
</html>
