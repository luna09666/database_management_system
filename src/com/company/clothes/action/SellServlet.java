package com.company.clothes.action;

import com.company.clothes.bean.*;
import com.company.clothes.biz.UserBiz;
import com.company.clothes.biz.*;
import com.company.clothes.dao.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@WebServlet(name = "sell", urlPatterns="/sell")
public class SellServlet extends HttpServlet {
    sellBiz  sellBiz = new sellBiz();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        doPost(req, resp);
        out.flush();
        out.close();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter  out  = resp.getWriter();

        HttpSession session = req.getSession();
        if(session.getAttribute("user")==null){
            out.println("<script>alert('请登录');parent.window.location.href='login.html';</script>");
            return;
        }


        String type = req.getParameter("type");
        int userid = Integer.parseInt(req.getParameter("userid"));
        UserBiz userBiz = new UserBiz();
        User useri= null;
        try {
            useri = userBiz.getUserById(userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        req.setAttribute("useri",useri);
        switch (type) {
            case "addtemp":
                req.setAttribute("userid", userid);
                req.getRequestDispatcher("beforeAddSell.jsp?userid="+userid).forward(req, resp);
                break;
            case "addpre":
                String name_a = req.getParameter("name_a");
                if (name_a==null) {
                    out.println("<script>alert('名称不可为空'); location.href='sell?type=addtemp&userid="+userid+"';</script>");
                }
                String type_a = req.getParameter("type_a");
                String sellprice = req.getParameter("sellprice");
                String buyprice = req.getParameter("buyprice");
                String selltime = req.getParameter("selltime");
                req.setAttribute("name_a", name_a);
                req.setAttribute("type_a", type_a);
                req.setAttribute("userid", userid);
                req.setAttribute("sellprice", sellprice);
                req.setAttribute("buyprice", buyprice);
                req.setAttribute("selltime", selltime);
                if("格裙".equals(type_a)||"纯色裙".equals(type_a)){
                    req.getRequestDispatcher("BASskirt.jsp?userid="+userid).forward(req, resp);
                }
                else{
                    req.getRequestDispatcher("BASothers.jsp?userid="+userid).forward(req, resp);
                }
                break;
            case "add":
                try {
                    add(req, resp, out, userid);
                } catch (FileUploadException e) {
                    e.printStackTrace();
                    resp.sendError(500, "error");
                } catch (Exception e) {
                    e.printStackTrace();
                    resp.sendError(500, e.getMessage());
                }
                break;
            case "modifypre":
                //sell?type=modifypre&id=xx
                int clothesId = Integer.parseInt(req.getParameter("id"));
                Sell sell = null;
                try {
                    sell = sellBiz.getSellById(clothesId,userid);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                req.setAttribute("sell", sell);
                req.setAttribute("userid", userid);
                req.getRequestDispatcher("modifySell.jsp?userid="+userid).forward(req, resp);
                break;
            case "modify":
                try {
                    modify(req, resp, out,userid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "query":
                try {
                    query(req, resp, out,userid);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;
            case "remove":
                int removeId = Integer.parseInt(req.getParameter("id"));

                try {
                    int count = sellBiz.removeSell(removeId,userid);

                    if (count > 0) {
                        out.println("<script>alert('删除成功'); location.href='sell?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
                    } else {
                        out.println("<script>alert('删除失败'); location.href='sell?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("<script>alert('" + e.getMessage() + "'); location.href='sell?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
                }
                break;
            case "searchpre":
                req.setAttribute("userid",userid);
                req.getRequestDispatcher("searchSell.jsp?userid="+userid).forward(req,resp);
                break;
            case "search":
                try {
                    List<Sell> sells;
                    String key = req.getParameter("key");
                    String type_s = req.getParameter("type_s");
                    String type_q = req.getParameter("type_q");
                    int pageSize = 10;
                    int pageCount = sellBiz.getKeyPageCount(pageSize,key,type_s,userid);
                    int pageIndex=1;
                    if(req.getParameter("pageIndex")!=null){
                        pageIndex = Integer.parseInt(req.getParameter("pageIndex"));
                    }
                    if(pageIndex<1){
                        pageIndex = 1;
                    }
                    else if(pageIndex>pageCount){
                        pageIndex = pageCount;
                    }
                    sells = sellBiz.getSellByKeyAndPage(pageIndex,pageSize,key,userid,type_s);


                    req.setAttribute("userid",userid);//?????jsp???
                    req.setAttribute("pageCount",pageCount);
                    req.setAttribute("key",key);
                    req.setAttribute("pageIndex",pageIndex);
                    req.setAttribute("sells",sells);
                    req.setAttribute("type_q",type_q);
                    req.setAttribute("type_s",type_s);
                    req.getRequestDispatcher("searchSell.jsp?pageIndex="+pageIndex+"&userid="+userid+"&type_q="+type_q+"&type_s="+type_s).forward(req,resp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                resp.sendError(404);
        }
    }
    /**
     * ??????
     * sell?type=query&pageIndex=1
     * ???: biz
     * ??????:pageIndex = 1
     * ??:request,???
     * @param req
     * @param resp
     * @throws Exception
     */
    private void query(HttpServletRequest req, HttpServletResponse resp, PrintWriter out,int userid) throws Exception {
        UserDao userdao = new UserDao();
        int pageSize = 10;
        int pageCount = sellBiz.getSellPageCount(pageSize,userid);
        int pageIndex = Integer.parseInt(req.getParameter("pageIndex"));
        String type_q = req.getParameter("type_q");

        if(pageIndex < 1){
            pageIndex = 1;
        }
        if(pageIndex > pageCount){
            pageIndex = pageCount;
        }

        List<Sell> sells = sellBiz.getSellByPage(pageIndex,pageSize,type_q,userid);
        User useri = userdao.getUserById(userid);

        req.setAttribute("pageCount",pageCount);
        req.setAttribute("sells",sells);
        req.setAttribute("type_q",type_q);
        req.setAttribute("userid",userid);
        req.setAttribute("useri",useri);

        req.getRequestDispatcher("sell.jsp?type_q="+type_q+"pageIndex="+pageIndex+"&userid="+userid).forward(req,resp);
    }


    /**
     * ???
     * @param req
     * @param resp
     * @param out
     */
    private void modify(HttpServletRequest req, HttpServletResponse resp, PrintWriter out,int userid) throws Exception {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024*9);
        File file = new File("d:\\This Term\\Database\\temp");
        if(!file.exists()){
            file.mkdir();
        }
        factory.setRepository(file);

        ServletFileUpload  fileUpload = new ServletFileUpload(factory);

        List<FileItem> fileItems = fileUpload.parseRequest(req);

        Sell sell  =new Sell();

        SimpleDateFormat sdf = new SimpleDateFormat();// ????????
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a?am/pm????
        Date date = new Date();// ?????????
        String time = sdf.format(date);
        sell.setCreatetime(time);
        sell.setUpdatetime(time);
        String type = null;
        String colora = null;
        String colorb = null;
        String specsa = null;
        String specsb = null;
        String specsc = null;


        String name_m=null;

        for(FileItem  item: fileItems){
            if(item.isFormField()){
                String name = item.getFieldName();
                String value = item.getString("utf-8");//???????
                switch(name){
                    case "id":
                        sell.setId(Integer.parseInt(value));
                        break;
                    case "type_m":
                        type = value;
                        sell.setType(value);
                        break;
                    case "name_m":
                        name_m=value;
                        sell.setName(value);
                        break;
                    case "sellprice":
                        sell.setSellprice(Float.parseFloat(value));
                        break;
                    case "buyprice":
                        sell.setBuyprice(Float.parseFloat(value));
                        break;
                    case "selltime":
                        sell.setSelltime(value);
                        break;
                    case "pic":
                        sell.setPic(value);
                        break;
                    case "len":
                        sell.setLen(Float.parseFloat(value));
                        break;
                    case "waist":
                        sell.setWaist(Float.parseFloat(value));
                        break;
                    case "size":
                        sell.setSize(value);
                        break;
                    case "color":
                        sell.setColor(value);
                        break;
                    case "describe":
                        sell.setDescribe1(value);
                        break;
                    case "specs":
                        sell.setSpecs(value);
                        break;
                    case "colora":
                        colora = value;
                        break;
                    case "colorb":
                        colorb = value;
                        break;
                    case "specsa":
                        specsa = value;
                        break;
                    case "specsb":
                        specsb = value;
                        break;
                    case "specsc":
                        specsc = value;
                        break;
                }


                if("格裙".equals(type)){
                    String color = colora+" "+colorb;
                    sell.setColor(color);
                }
                else if("水手服".equals(type)){
                    String specs = specsa+"本 "+specsb;
                    sell.setSpecs(specs);
                }
                else if("毛衣".equals(type)){
                    String specs = specsa+" "+specsb+" "+specsc;

                    sell.setSpecs(specs);
                }
                else if("西服".equals(type)){
                    String specs = specsa+" "+specsb;
                    sell.setSpecs(specs);
                }

            }else {
                String fileName = item.getName();
                if(fileName.trim().length()>0) {
                    String filterName = fileName.substring(fileName.lastIndexOf("."));
                    SimpleDateFormat sdf0= new SimpleDateFormat("yyyyMMddHHmmssS");  //??????
                    fileName = sdf0.format(new Date())+filterName;

                    String path = req.getServletContext().getRealPath("/Image");
                    String filePath = path+"/"+fileName;
                    String dbPath  = "Image/"+fileName;
                    sell.setPic(dbPath);

                    item.write(new File(filePath));
                }
            }
        }
        int count = sellBiz.modifySell(sell,userid);
        if (name_m==null) {
            out.println("<script>alert('名称不可为空'); location.href='sell?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
        }
        if(count>0){
            out.println("<script>alert('修改成功');location.href='sell?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
        }else{
            out.println("<script>alert('修改失败');location.href='sell?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
        }
    }



    private void add(HttpServletRequest req, HttpServletResponse resp,  PrintWriter  out,int userid) throws Exception {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024*9);
        File file = new File("d:\\This Term\\Database\\temp");
        if(!file.exists()){
            file.mkdir();
        }
        factory.setRepository(file);

        ServletFileUpload  fileUpload = new ServletFileUpload(factory);

        List<FileItem> fileItems = fileUpload.parseRequest(req);

        Sell sell  =new Sell();

        SimpleDateFormat sdf = new SimpleDateFormat();// ????????
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a?am/pm????
        Date date = new Date();// ?????????
        String time = sdf.format(date);
        sell.setCreatetime(time);
        sell.setUpdatetime(time);
        String type = null;
        String colora = null;
        String colorb = null;
        String specsa = null;
        String specsb = null;
        String specsc = null;
        String id = null;
        String wardrobe = null;

        for(FileItem  item: fileItems){
            if(item.isFormField()){
                String name = item.getFieldName();
                String value = item.getString("utf-8");
                switch(name){
                    case "id":
                        id = value;
                        break;
                    case "type_a":
                        type = value;
                        sell.setType(value);
                        break;
                    case "name_a":
                        sell.setName(value);
                        break;
                    case "len":
                        sell.setLen(Float.parseFloat(value));
                        break;
                    case "waist":
                        sell.setWaist(Float.parseFloat(value));
                        break;
                    case "size":
                        sell.setSize(value);
                        break;
                    case "color":
                        sell.setColor(value);
                        break;
                    case "describe":
                        sell.setDescribe1(value);
                        break;
                    case "specs":
                        sell.setSpecs(value);
                        break;
                    case "colora":
                        colora = value;
                        break;
                    case "colorb":
                        colorb = value;
                        break;
                    case "specsa":
                        specsa = value;
                        break;
                    case "specsb":
                        specsb = value;
                        break;
                    case "specsc":
                        specsc = value;
                        break;
                    case "pic":
                        sell.setPic(value);
                        break;
                    case "selltime":
                        if(value!=null&&!"".equals(value)){
                            sell.setSelltime(value);
                        }
                        else{
                            sell.setSelltime(time);
                        }
                        break;
                    case "wardrobe":
                        wardrobe = value;
                        break;
                }


                if("格裙".equals(type)){
                    String color = colora+" "+colorb;
                    sell.setColor(color);
                }
                else if("水手服".equals(type)){
                    String specs = specsa+"本 "+specsb;

                    sell.setSpecs(specs);
                }
                else if("毛衣".equals(type)){
                    String specs = specsa+" "+specsb+" "+specsc;

                    sell.setSpecs(specs);
                }
                else if("西服".equals(type)){
                    String specs = specsa+" "+specsb;

                    sell.setSpecs(specs);
                }

            }else {
                String fileName = item.getName();
                if(fileName.trim().length()>0){
                    String filterName = fileName.substring(fileName.lastIndexOf("."));
                    SimpleDateFormat sdf0= new SimpleDateFormat("yyyyMMddHHmmssS");
                    fileName = sdf0.format(new Date())+filterName;

                    String path = req.getServletContext().getRealPath("/Image");
                    String filePath = path+"/"+fileName;
                    String dbPath  = "Image/"+fileName;
                    sell.setPic(dbPath);

                    //4.3 ???????
                    item.write(new File(filePath));
                }
            }

        }
        int count = sellBiz.addSell(sell,userid);
        clothesBiz clothesBiz=new clothesBiz();
        if(count>0){
            if("wardrobe".equals(wardrobe)){
                assert id != null;
                int counta = clothesBiz.removeWardrobe(Integer.parseInt(id),userid);
                if(counta>0){
                    out.println("<script>alert('添加成功');location.href='sell?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
                }
                else{
                    out.println("<script>alert('添加失败');location.href='sell?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
                }
            }
            out.println("<script>alert('添加成功');location.href='sell?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
        }else{
            out.println("<script>alert('添加失败');location.href='sell?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
        }
    }
}
