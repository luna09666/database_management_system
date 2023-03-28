package com.company.clothes.action;

import com.company.clothes.bean.Clothes;
import com.company.clothes.biz.clothesBiz;
import com.company.clothes.dao.ExcelFileGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "excel", urlPatterns="/excel")
public class ExcelServlet extends HttpServlet {
    clothesBiz  clothesBiz = new clothesBiz();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setContentType("text/html");
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            req.setCharacterEncoding("utf-8");
            resp.setContentType("text/html;charset=utf-8");


            String type = req.getParameter("type");
            int userid = Integer.parseInt(req.getParameter("userid"));

            String title[]={"id",new String("名称".getBytes("gbk"), StandardCharsets.UTF_8),new String("类型".getBytes("gbk"), StandardCharsets.UTF_8)
                    ,new String("价格".getBytes("gbk"), StandardCharsets.UTF_8),new String("写入时间".getBytes("gbk"), StandardCharsets.UTF_8)
                    ,new String("修改时间".getBytes("gbk"), StandardCharsets.UTF_8)};
            List<Clothes> cclothes = clothesBiz.getAllWardrobe(userid);
            int l = cclothes.size();
            String[][] content = new String[l][6];
            int i=0;
            for(Clothes clothes:cclothes) {
                String[] temp = {String.valueOf(clothes.getId()), new String(clothes.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8), new String(clothes.getType().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8),
                        String.valueOf(clothes.getPrice()), clothes.getCreatetime(), clothes.getUpdatetime()};
                content[i] = temp;
                i++;
            }

            //初始化fieldName，fieldDate
            ArrayList fieldName=getFieldName(title);    //excel标题数据集
            ArrayList fieldData=getFieldData(content);    //excel数据内容
            String myexcel="myexcel";
            //回去输出流
            OutputStream out=resp.getOutputStream();
            //重置输出流
            resp.reset();
            //设置导出Excel报表的导出形式
            resp.setContentType("application/vnd.ms-excel;charset=UTF-8");
            resp.setHeader("Content-Disposition","attachment;filename="+myexcel+".xls");
            ExcelFileGenerator efg=new ExcelFileGenerator(fieldName, fieldData);

            efg.expordExcel(out);
            //设置输出形式
            System.setOut(new PrintStream(out));
            //刷新输出流
            out.flush();
            //关闭输出流
            if(out!=null){
                out.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }


    //模拟提供excel中的标题数据集
    public ArrayList getFieldName(String str[]) throws UnsupportedEncodingException {
        //String str[]={new String("姓名".getBytes("gbk"), StandardCharsets.UTF_8),"学号","性别"};
        ArrayList list=new ArrayList();
        for(int i=0;i<str.length;i++){
            list.add(str[i]);
        }
        return list;
    }
    //模拟提供excel中的标题数据内容
    public ArrayList getFieldData(String str[][]){

        ArrayList list1=new ArrayList();
        //String str[][]={{"wang","01","男"},{"hai","02","男"},{"ping","03","女"}};


        for(int i=0;i<str.length;i++)
        {
            ArrayList list=new ArrayList();
            for(int j=0;j<str[0].length;j++)
            {
                list.add(str[i][j]);
            }
            list1.add(list);
        }
        return list1;
    }

}


