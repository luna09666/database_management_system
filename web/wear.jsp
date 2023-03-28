<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>穿搭主页</title>
  <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/global.css">
  <link rel="stylesheet" type="text/css" href="css/wear.css">
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
    <li><a class="for_a_global" href="wardrobe?type=query&pageIndex=1&type_q=all&userid=${userid}">衣柜</a></li>
    <li><a class="for_a_global" href="grass?type=query&pageIndex=1&type_q_g=all&type_q=all&userid=${userid}">草单</a></li>
    <li><a class="for_a_global" href="sell?type=query&pageIndex=1&type_q=all&userid=${userid}">卖出</a></li>
    <li><a class="for_a_global" href="user?type=sta&type_sta=all&userid=${userid}">统计</a></li>
    <li><a class="for_a_global active" href="wear?type=query&pageIndex=1&userid=${userid}">穿搭</a></li>
  </div>
</ul>

<div style="margin-top:40px;text-align: right;margin-right:4%;">
<a href="wear?type=addpre&userid=${userid}"><button class='btn btn-warning add'>写穿搭</button></a>
</div>
<div style="width:82%;margin-top:0;margin-left:16%;padding:1px 16px;">
      <ul id='timeline' style="margin-left:10%;border-left: 8px solid #fcd3ee;">
        <c:forEach items="${wears}" var="w">
          <li class='work'>
            <div class='content'>
              <div  style="margin:10px 5px 30px 5px;border-style:dashed;border-width:5px 5px;border-color: lightpink;border-radius: 0.5em;">
                <div style="margin:20px 30px;">
                  <div style="margin-right:1%;text-align: right;">
                    <a onclick="return confirm('确认删除');" href="wear?type=remove&id=${pageScope.w.id}&userid=${userid}">
                      <button class="button_for_delete">删除</button>
                    </a>
                  </div>

                  <div style="margin: auto;color:#ffa8ef;font-size:20px;"><label>${w.title}</label></div>
                  <div style="margin-top: -10px;top: 50%;left: -155px;font-size: 1.5em;line-height: 20px;position: absolute;">
                    <strong>${w.createtime.split(" ")[0]}</strong></div>
                  <div style="margin-top: 7px;top: 50%;left: -105px;font-size: 0.95em;line-height: 20px;position: absolute;">
                      ${w.createtime.split(" ")[1]}</div>
                  <div class='circle' style="border: 5px solid #fda8d5;"></div>
                  <p></p>
                  <div style="text-indent:30px;font-size: 15px;margin-bottom:5%;">
                      ${w.content}
                  </div>
                  <c:if test="${w.pic!=null&&!w.pic.isEmpty()}">
                    <a href="${w.pic}"><img src="${w.pic}" style="width: 100px;height:125px;"/></a>
                  </c:if>
                </div>
              </div>
            </div>
          </li>
        </c:forEach>
        <tr><td colspan="8" align="center">
          <div class="pager">
            <ul class="clearfix">
              <li><a href="wear?type=query&pageIndex=${param.pageIndex-1}&userid=${userid}">上一页</a></li>
              <c:forEach var="i" begin="1" end="${pageCount}" step="1">
                <c:if test="${i==param.pageIndex}">
                  <li class="current"><a href="wear?type=query&pageIndex=${i}&userid=${userid}">${i}</a></li>
                </c:if>
                <c:if test="${i!=param.pageIndex}">
                  <li><a href="wear?type=query&pageIndex=${i}&userid=${userid}">${i}</a></li>
                </c:if>
              </c:forEach>
              <li><a href="wear?type=query&pageIndex=${param.pageIndex+1}&userid=${userid}">下一页</a></li>
            </ul>
          </div>

        </td></tr>

      </ul>
    </div>
</body>
</html>