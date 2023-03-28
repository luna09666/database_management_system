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
  <div class="title">修改密码</div>
  <form id="fmLogin" method="post" action="user?type=modifypwd&userid=${userid}" >
    <div class="block">
      <input type="text" name="name" value="${user.name}" readonly/>
      <input type="password" name="pwd" value="${user.password}" readonly/>
      <input type="password" name="newpwd" placeholder="请输入新密码" />
      <input type="password" name="newpwda" placeholder="请确认新密码" />
    </div>
    <div class="block1">
      <button class="button" type="submit">确认修改</button>
    </div>
  </form>
</div>
<div class="login3">
  已有账号？
</div>
<div class="login2">
  <a href="login.html">登录</a>
</div>

</body>
</html>
