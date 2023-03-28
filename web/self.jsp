<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@page import="java.sql.*" %>  <%--导入java.sql包--%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>个人信息填写</title>
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
  <li><img src="${user.pic}" style="width: 60px;height:60px;border-radius: 10em;border-style:solid;border-width:1.5px;border-color:white;"/></li>
  <a class="for_a_global" href="user?type=show&userid=${userid}"><div style="color: #545353;">${user.name}</div></a></li>
  <div style="margin: 20px 0 0 0;">
    <li><a class="for_a_global" href="wardrobe?type=query&pageIndex=1&type_q=all&userid=${userid}">衣柜</a></li>
    <li><a class="for_a_global" href="grass?type=query&pageIndex=1&type_q_g=all&type_q=all&userid=${userid}">草单</a></li>
    <li><a class="for_a_global" href="sell?type=query&pageIndex=1&type_q=all&userid=${userid}">卖出</a></li>
    <li><a class="for_a_global" href="user?type=sta&type_sta=all&userid=${userid}">统计</a></li>
    <li><a class="for_a_global" href="wear?type=query&pageIndex=1&userid=${userid}">穿搭</a></li>
  </div>
</ul>



<div class="container">
  <div style="margin-top:20px;margin-bottom:40px;border-style:dashed;border-width:5px 5px;border-color: lightpink;border-radius: 0.5em;">
  <div style="border-style:solid;border-width:20px 10px;border-color: #ffffff;">
  <form action="user?type=modifyuser&userid=${userid}" role="form" method="post" enctype="multipart/form-data">

      <div style="text-align: center">
        <img id="imgPic" src="${user.pic}" style="width: 100px;height:100px;border-radius: 10em;border-style:solid;border-width:3px;border-color:#ffbad7;"/>
      </div>
      <input type="hidden" name="pic" value="${user.pic}">
      <input type="file" name="filePic" id="filePic" value="${user.pic}" style="margin:2% 45% 8%;">



    <div class="form-group">
      <div style="width:90%;margin:auto;">
        <label>id</label>
        <input type="text" class="form-control" name="userid" value="${user.id}" readonly>
      </div>
    </div>

    <div class="form-group">
      <div style="width:90%;margin:auto;">
        <label>电话号码</label>
        <input type="text" class="form-control" name="phonenumber" value="${user.phonenumber}" readonly>
      </div>
    </div>

    <div class="form-group">
      <div style="width:90%;margin:auto;">
        <label>用户名</label>
        <input type="text" class="form-control" name="name" value="${user.name}">
      </div>
    </div>

    <div class="form-group">
      <div style="width:90%;margin:auto;">
      <label>性别</label><br/>
      <select name="sex">
        <option ${"女".equals(user.sex)?"selected":""}>女</option>
        <option ${"男".equals(user.sex)?"selected":""}>男</option>
      </select>
      </div>
    </div>

    <div class="form-group">
      <div style="width:90%;margin:auto;">
      <label>地址</label>
      <input type="text" class="form-control" name="position" value="${user.position}" placeholder="请输入地区">
      </div>
    </div>

    <div class="form-group">
      <div style="width:90%;margin:auto;">
      <label>个性签名</label>
      <input type="text" class="form-control" name="describe1" value="${user.describe1}" placeholder="请输入个性签名">
      </div>
    </div>

    <input type="hidden" name="password" value="${user.password}">

    <div style="margin: 8% 45% 2%">
      <button onclick="return confirm('确认修改？');" class="button_for_text" type="submit">修改资料</button>
    </div>
  </form>
    <div style="margin: 2% 45% 2%">
      <a onclick="return confirm('确认退出？');" href="user?type=exit&userid=${userid}"><button class="button_for_pwd">安全退出</button></a>
    </div>

  <div style="margin: 2% 45% 8%">
    <a href="user?type=modifypwdpre&userid=${userid}"><button class="button_for_pwd">修改密码</button></a>
  </div>
  </div>
  </div>
</div>
</body>
</html>