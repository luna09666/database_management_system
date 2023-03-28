<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@page import="java.sql.*" %>  <%--导入java.sql包--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>个人信息填写</title>
  <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/global.css">
  <script src="js/jquery-3.5.1.min.js"></script>
  <script src="js/bootstrap.min.js"></script>
  <script src="js/echarts.min.js"></script>
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
    <li><a class="for_a_global active" href="user?type=sta&type_sta=all&userid=${userid}">统计</a></li>
    <li><a class="for_a_global" href="wear?type=query&pageIndex=1&userid=${userid}">穿搭</a></li>
  </div>
</ul>


<div class="container">
  <div style="margin-top:20px;margin-bottom:40px;border-style:dashed;border-width:5px 5px;border-color: lightpink;border-radius: 0.5em;">
  <div style="border-style:solid;border-width:20px 10px;border-color: #ffffff;">

    <script type="text/javascript">
      var colorkey=[];
      var colorvalue=[];
      var skirtcolorkey=[];
      var skirtcolorvalue=[];
      var typekey=[];
      var typevalue=[];
      var lenkey=[];
      var lenvalue=[];
      var waistkey=[];
      var waistvalue=[];
      var sizekey=[];
      var sizevalue=[];
      var monthmoneykey=[];
      var monthmoneyvalue=[];
      var monthsellkey=[];
      var monthsellvalue=[];
    </script>
    <div style="margin:50px 0 50px 0;">
      <ul class="for_wardrobe_ul">
        <li><a ${"all".equals(type_sta)?"class='active'":""} href="user?type=sta&type_sta=all&userid=${userid}">全部</a></li>
        <li><a ${"skirt".equals(type_sta)?"class='active'":""} href="user?type=sta&type_sta=skirt&userid=${userid}">格裙</a></li>
        <li><a ${"shirt".equals(type_sta)?"class='active'":""} href="user?type=sta&type_sta=shirt&userid=${userid}">衬衫</a></li>
        <li><a ${"little".equals(type_sta)?"class='active'":""} href="user?type=sta&type_sta=little&userid=${userid}">小物</a></li>
        <li><a ${"sweater".equals(type_sta)?"class='active'":""} href="user?type=sta&type_sta=sweater&userid=${userid}">毛衣</a></li>
        <li><a ${"vest".equals(type_sta)?"class='active'":""} href="user?type=sta&type_sta=vest&userid=${userid}">马甲</a></li>
        <li><a ${"suit".equals(type_sta)?"class='active'":""} href="user?type=sta&type_sta=suit&userid=${userid}">西服</a></li>
        <li><a ${"shoes".equals(type_sta)?"class='active'":""} href="user?type=sta&type_sta=shoes&userid=${userid}">鞋子</a></li>
        <li><a ${"seawoman".equals(type_sta)?"class='active'":""} href="user?type=sta&type_sta=seawoman&userid=${userid}">水手服</a></li>
        <li><a ${"pureskirt".equals(type_sta)?"class='active'":""} href="user?type=sta&type_sta=pureskirt&userid=${userid}">纯色裙</a></li>
        <li><a ${"dress".equals(type_sta)?"class='active'":""} href="user?type=sta&type_sta=dress&userid=${userid}">连衣裙</a></li>
        <li><a ${"others".equals(type_sta)?"class='active'":""} href="user?type=sta&type_sta=others&userid=${userid}">其它</a></li>
      </ul>
    </div>

    <div style="font-size:30px;text-align: center;margin-top:1%;color:#fd3cd8;">
      收支统计
    </div>

      <c:forEach items="${nmmoneymonth}" var="nentry" varStatus="nv">
        <script>
          monthmoneykey.push(${nentry.key.split("-")[0]}+"-"+${nentry.key.split("-")[1]});
          monthmoneyvalue.push(${nentry.value});
        </script>
      </c:forEach>
      <c:forEach items="${nmsellmonth}" var="nentry" varStatus="nv">
        <script>
          monthsellkey.push(${nentry.key.split("-")[0]}+"-"+${nentry.key.split("-")[1]});
          monthsellvalue.push(${nentry.value});
        </script>
      </c:forEach>


    <div style="margin:50px;">
      <div id="moneymonth" style="width: 100%;height: 200px;"></div>

      <div style="text-align: left;color:grey">
        年花费:
        <c:forEach items="${nmmoneyyear}" var="nentry" varStatus="nv">
          ${nentry.key}:
          ${nentry.value}元;
        </c:forEach></div>
    </div>
  <div style="margin:auto;padding: 0;width: 90%;height: 5px;border-top: solid #fda8c9 1.8px;"></div>
    <div style="margin:50px;">
      <div id="sellmonth" style="width: 100%;height: 200px;"></div>
      <div style="text-align: left;color:grey"><c:forEach items="${nmsellyear}" var="nentry" varStatus="nv">
        年收入:
        ${nentry.key}:
        ${nentry.value}元;
      </c:forEach></div>
    </div>

    <div style="margin:auto;padding: 0;width: 90%;height: 5px;border-top: solid #fda8c9 1.8px;"></div>
    <div style="margin:50px;">
      <div style="text-align: left;margin-left: 1%;font-size: 19px;color:#505050;"><strong>支出类型</strong></div>
      <div id="type" style="margin:auto;width: 800px;height:300px;"></div>
    </div>

    <div style="font-size:30px;text-align: center;margin-top:1%;color:#fd3cd8;">
      喜好统计
    </div>
    <div style="margin:50px;">
      <div style="text-align: left;margin-left: 1%;font-size: 19px;color:#505050;"><strong>颜 色</strong></div>
      <div id="color" style="margin:auto;width: 60%;height:300px;left:0;"></div>
      <div style="left:70%;">
        <div style="text-align: center;color:grey">
          <c:forEach items="${nmskirtcolors}" var="nentry" varStatus="nv">
            ${nentry.key}:
            ${nentry.value};
          </c:forEach>
        </div>
      </div>
    </div>
    </div>

    <div style="margin:auto;padding: 0;width: 90%;height: 5px;border-top: solid #fda8c9 1.8px;"></div>
    <div style="margin:50px;">
      <div style="text-align: left;margin-left: 1%;font-size: 19px;color:#505050;"><strong>裙 长</strong></div>
      <div id="len" style="margin:auto;width: 300px;height:300px;"></div>
    </div>
    <div style="margin:auto;padding: 0;width: 90%;height: 5px;border-top: solid #fda8c9 1.8px;"></div>
    <div style="margin:50px;">
      <div style="text-align: left;margin-left: 1%;font-size: 19px;color:#505050;"><strong>腰 围</strong></div>
      <div id="waist" style="margin:auto;width: 300px;height:300px;"></div>
    </div>
    <div style="margin:auto;padding: 0;width: 90%;height: 5px;border-top: solid #fda8c9 1.8px;"></div>
    <div style="margin:50px;">
      <div style="text-align: left;margin-left: 1%;font-size: 19px;color:#505050;"><strong>尺 码</strong></div>
      <div id="size" style="margin:auto;width: 400px;height:300px;"></div>
    </div>


      <script>
        //初始化ehcharts实例
        var myChart=echarts.init(document.getElementById("sellmonth"));
        //指定图表的配置项和数据
        var option={
          //标题
          title:{
            text:'月收入'
          },
          //工具箱
          //保存图片
          toolbox:{
            show:true,
            feature:{
              saveAsImage:{
                show:true
              }
            }
          },
          //图例-每一条数据的名字叫销量
          legend:{
            data:['收入']
          },
          //x轴
          xAxis:{
            data:monthsellkey,
            axisLabel:{
              interval:4,
            }
          },
          //y轴没有显式设置，根据值自动生成y轴
          yAxis:{},
          //数据-data是最终要显示的数据
          series:[{
            name:'收入',
            type:'line',
            data:monthsellvalue,
            label: {
              show: true,
              formatter: function (data) {
                return data.value;
              }
            },
            itemStyle: {
              normal: {
                color: "#ff76b9",//折线点的颜色
                lineStyle: {
                  color: "#ff76b9"//折线的颜色
                }
              }
            }
          }],
          dataZoom: [{
            type: 'slider',
            show: true, //flase直接隐藏图形
            xAxisIndex: [0],
            left: '9%', //滚动条靠左侧的百分比
            bottom: -5,
            start: 0,//滚动条的起始位置
            end: 50 //滚动条的截止位置（按比例分割你的柱状图x轴长度）
            }]
        };
        //使用刚刚指定的配置项和数据项显示图表
        myChart.setOption(option);
      </script>

      <script>
        //初始化ehcharts实例
        var myChart=echarts.init(document.getElementById("moneymonth"));
        //指定图表的配置项和数据
        var option={
          //标题
          title:{
            text:'月花费'
          },
          //工具箱
          //保存图片
          toolbox:{
            show:true,
            feature:{
              saveAsImage:{
                show:true
              }
            }
          },
          //图例-每一条数据的名字叫销量
          legend:{
            data:['花费']
          },
          //x轴
          xAxis:{
            data:monthmoneykey
          },
          //y轴没有显式设置，根据值自动生成y轴
          yAxis:{},
          //数据-data是最终要显示的数据
          series:[{
            name:'花费',
            type:'line',
            data:monthmoneyvalue,
            label: {
              show: true,
              formatter: function (data) {
                return data.value;
              }
            },
            itemStyle: {
              normal: {
                color: "#ff76b9",//折线点的颜色
                lineStyle: {
                  color: "#ff76b9"//折线的颜色
                }
              }
            }
          }],
          dataZoom: [{
            type: 'slider',
            show: true, //flase直接隐藏图形
            xAxisIndex: [0],
            left: '9%', //滚动条靠左侧的百分比
            bottom: -5,
            start: 0,//滚动条的起始位置
            end: 50 //滚动条的截止位置（按比例分割你的柱状图x轴长度）
          }]
        };
        //使用刚刚指定的配置项和数据项显示图表
        myChart.setOption(option);
      </script>





      <script>
        //初始化ehcharts实例
        var myChart=echarts.init(document.getElementById("sellyear"));
        //指定图表的配置项和数据
        var option={
          //标题
          title:{
            text:'年收入'
          },
          //工具箱
          //保存图片
          toolbox:{
            show:true,
            feature:{
              saveAsImage:{
                show:true
              }
            }
          },
          //图例-每一条数据的名字叫销量
          legend:{
            data:['收入']
          },
          //x轴
          xAxis:{
            data:yearsellkey
          },
          //y轴没有显式设置，根据值自动生成y轴
          yAxis:{},
          //数据-data是最终要显示的数据
          series:[{
            name:'收入',
            type:'line',
            data:yearsellvalue,
            label: {
              show: true,
              formatter: function (data) {
                return data.value;
              }
            }
          }]
        };
        //使用刚刚指定的配置项和数据项显示图表
        myChart.setOption(option);
      </script>


    <c:forEach items="${nmtypes}" var="nentry" varStatus="nv">
      <script type="text/javascript">
        var myChart = echarts.init(document.getElementById('type'));
        typekey.push("${nentry.key}");
        typevalue.push(${nentry.value});
      </script>
    </c:forEach>
    <script>
      var data=[];
      for(var i=0;i<typekey.length;i++){
        data.push({value:typevalue[i], name:typekey[i]});
      }
      myChart.setOption({
        series : [
          {
            color: ['#fc7ac8','#f8c9e2','#ff40b3','#fc7ac8','#fda8d5','#fc1da2','#fd87ce','#facae7','#fda8db'],
            name: '类型',
            type: 'pie',    // 设置图表类型为饼图
            radius: '55%',  // 饼图的半径，外半径为可视区尺寸（容器高宽中较小一项）的 55% 长度。
            data:data,
            itemStyle:{
              normal:{
                label:{
                  show: true,
                  formatter: '{b} : {c} ({d}%)'
                },
                labelLine :{show:true}
              }
            }
          }
        ]
      })
    </script>




        <c:forEach items="${nmcolors}" var="nentry" varStatus="nv">
        <script type="text/javascript">
          var myChart = echarts.init(document.getElementById('color'));
          colorkey.push("${nentry.key}");
          colorvalue.push(${nentry.value});
        </script>
        </c:forEach>
        <script>
          var data=[];
          for(var i=0;i<colorkey.length;i++){
            data.push({value:colorvalue[i], name:colorkey[i]});
          }
          myChart.setOption({
            series : [
              {
                color: ['#7aedfc','#ace9f8','#5bb7f3','#65a0f8','#00b2ff','#46cbfc','#00b3ff','#4183fc'],

                name: '颜色',
                type: 'pie',    // 设置图表类型为饼图
                radius: '55%',  // 饼图的半径，外半径为可视区尺寸（容器高宽中较小一项）的 55% 长度。
                data:data,
                itemStyle:{
                  normal:{
                    label:{
                      show: true,
                      formatter: '{b} : {c} ({d}%)'
                    },
                    labelLine :{show:true}
                  }
                }
              }
            ]
          })
        </script>









    <c:forEach items="${nmlen}" var="nentry" varStatus="nv">
      <script type="text/javascript">
        var myChart = echarts.init(document.getElementById('len'));
        lenkey.push("${nentry.key}");
        lenvalue.push(${nentry.value});
      </script>
    </c:forEach>
    <script>
      var data=[];
      for(var i=0;i<lenkey.length;i++){
        data.push({value:lenvalue[i], name:lenkey[i]});
      }
      myChart.setOption({
        series : [
          {
            color: ['#e38ffa','#d1acf8','#aa5bf3','#e8b2fa','#c334f3','#9f2ffa','#c48aff'],
            name: '裙长',
            type: 'pie',    // 设置图表类型为饼图
            radius: '55%',  // 饼图的半径，外半径为可视区尺寸（容器高宽中较小一项）的 55% 长度。
            data:data,
            itemStyle:{
              normal:{
                label:{
                  show: true,
                  formatter: '{b} : {c} ({d}%)'
                },
                labelLine :{show:true}
              }
            }
          }
        ]
      })
    </script>



    <c:forEach items="${nmwaist}" var="nentry" varStatus="nv">
      <script type="text/javascript">
        var myChart = echarts.init(document.getElementById('waist'));
        waistkey.push("${nentry.key}");
        waistvalue.push(${nentry.value});
      </script>
    </c:forEach>
    <script>
      var data=[];
      for(var i=0;i<waistkey.length;i++){
        data.push({value:waistvalue[i], name:waistkey[i]});
      }
      myChart.setOption({
        series : [
          {
            color: ['#f39f5b','#fc987a','#f8c9ac','#fab2b2','#f36734','#f6a375','#f5b81c'],

            name: '腰围',
            type: 'pie',    // 设置图表类型为饼图
            radius: '55%',  // 饼图的半径，外半径为可视区尺寸（容器高宽中较小一项）的 55% 长度。
            data:data,
            itemStyle:{
              normal:{
                label:{
                  show: true,
                  formatter: '{b} : {c} ({d}%)'
                },
                labelLine :{show:true}
              }
            }
          }
        ]
      })
    </script>


    <c:forEach items="${nmsize}" var="nentry" varStatus="nv">
      <script type="text/javascript">
        var myChart = echarts.init(document.getElementById('size'));
        sizekey.push("${nentry.key}");
        sizevalue.push(${nentry.value});
      </script>
    </c:forEach>
    <script>
      var data=[];
      for(var i=0;i<sizekey.length;i++){
        data.push({value:sizevalue[i], name:sizekey[i]});
      }
      myChart.setOption({
        series : [
          {
            color: ['#f3df5b','#fcbb7a','#f8e6ac','#fadab2','#f3d034','#fa9e2f','#fcf0aa'],
            name: '尺码',
            type: 'pie',    // 设置图表类型为饼图
            radius: '55%',  // 饼图的半径，外半径为可视区尺寸（容器高宽中较小一项）的 55% 长度。
            data:data,
            itemStyle:{
              normal:{
                label:{
                  show: true,
                  formatter: '{b} : {c} ({d}%)'
                },
                labelLine :{show:true}
              }
            }
          }
        ]
      })
    </script>

      </div>
  </div>
  </div>
</div>
</body>
</html>