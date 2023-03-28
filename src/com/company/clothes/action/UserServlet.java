package com.company.clothes.action;

import com.company.clothes.bean.*;
import com.company.clothes.dao.*;
import com.company.clothes.biz.UserBiz;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet(name = "user",urlPatterns="/user")
public class UserServlet extends HttpServlet {
    UserBiz userBiz = new UserBiz();
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
        PrintWriter out = resp.getWriter();

        HttpSession session = req.getSession();
        String method = req.getParameter("type");
        StaDao staDao = new StaDao();
        if("login".equals(method)){
            String phonenumber = req.getParameter("phonenumber");
            String pwd = req.getParameter("pwd");
            try {
                User user = userBiz.getUser(phonenumber, pwd);

                if(user==null){
                    out.println("<script>alert('用户名或密码不存在');location.href = 'login.html';</script>");
                }else {
                    int id = user.getId();
                    session.setAttribute("user",user);
                    if(user.getId()!=2){
                        out.println("<script>alert('登录成功');location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+id+"';</script>");
                    }
                    else if(user.getId()==2){
                        out.println("<script>alert('登录成功');location.href='user?type=query&pageIndex=1&userid="+id+"';</script>");
                    }
                    else{
                        out.println("<script>alert('登录失败');location.href='login.html';</script>");
                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else{
            switch (method){
                case "exit":
                    //验证用户是否登录
                    if(session.getAttribute("user")==null){
                        out.println("<script>alert('请登录');parent.window.location.href='login.html';</script>");
                        return;
                    }
                    //1.清除session
                    session.invalidate();
                    out.println("<script>parent.window.location.href='login.html';</script>");
                    break;
                case "modifypwdpre":
                    try {
                        int userid = Integer.parseInt(req.getParameter("userid"));
                        User user= userBiz.getUserById(userid);
                        req.setAttribute("user",user);
                        req.setAttribute("userid", userid);
                        req.getRequestDispatcher("modifypwd.jsp?userid="+userid).forward(req, resp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "modifypwd":
                    int userid = Integer.parseInt(req.getParameter("userid"));
                    //验证用户是否登录
                    if(session.getAttribute("user")==null){
                        out.println("<script>alert('请登录');parent.window.location.href='login.html';</script>");
                        return;
                    }

                    //修改密码
                    //1.获取用户输入的新的密码
                    String newpwd = req.getParameter("newpwd");
                    String newpwda = req.getParameter("newpwda");
                    if(!newpwd.equals(newpwda)){
                        out.println("<script>alert('输入的新密码不一致！'); location.href='user?type=modifypwdpre&userid="+userid+"';</script>");
                    }
                    else{
                        //2.获取用户的编号-session
                        //3.调用biz层方法
                        int count = 0;
                        try {
                            count = userBiz.modifyPwd(userid,newpwd);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        //4.响应-参考exit
                        if(count>0){
                            out.println("<script>alert('密码修改成功');parent.window.location.href='user?type=show&userid="+userid+"';</script>");
                        }else{
                            out.println("<script>alert('密码修改失败');parent.window.location.href='user?type=show&userid="+userid+"';</script>");
                        }
                    }

                    break;
                case "modifypre":
                    //wardrobe?type=modifypre&id=xx
                    int userId = Integer.parseInt(req.getParameter("id"));
                    User userm = null;
                    try {
                        userm = userBiz.getUserById(userId);
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    req.setAttribute("userm", userm);
                    req.getRequestDispatcher("modifyUser.jsp").forward(req, resp);
                    break;
                case "modifyuser":
                    try {
                        modifyUser(req, resp, out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "adduser":
                    String phonenumbera = req.getParameter("phonenumber");
                    String pwd_ra = req.getParameter("pwd");
                    String pwda_ra = req.getParameter("pwda");
                    try {
                        if(pwd_ra==null){
                            out.println("<script>alert('密码不能为空！'); location.href='regInfo.jsp';</script>");
                        }
                        if(!pwda_ra.equals(pwd_ra)){
                            out.println("<script>alert('密码不一致！'); location.href='regInfo.jsp';</script>");
                        }else{
                            int count_r = userBiz.addUser(phonenumbera,pwd_ra);
                            if (count_r > 0) {
                                out.println("<script>alert('注册成功'); location.href='user?type=query&pageIndex=1';</script>");
                            } else {
                                out.println("<script>alert('注册失败'); location.href='user?type=query&pageIndex=1';</script>");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "reginfo":
                    String phonenumber = req.getParameter("phonenumber");
                    String pwd_r = req.getParameter("pwd");
                    String pwda_r = req.getParameter("pwda");
                    try {
                        if(pwd_r==null){
                            out.println("<script>alert('密码不能为空！'); location.href='regInfo.jsp';</script>");
                        }
                        if(!pwda_r.equals(pwd_r)){
                            out.println("<script>alert('密码不一致！'); location.href='regInfo.jsp';</script>");
                        }else{
                            int count_r = userBiz.addUser(phonenumber,pwd_r);
                            if (count_r > 0) {
                                out.println("<script>alert('注册成功'); location.href='login.html';</script>");


                            } else {
                                out.println("<script>alert('注册失败'); location.href='login.html';</script>");

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "show":
                    try {
                        int useridsh = Integer.parseInt(req.getParameter("userid"));
                        User user=userBiz.getUserById(useridsh);
                        req.setAttribute("user",user);
                        req.setAttribute("userid",useridsh);

                        //转发到jsp页面
                        req.getRequestDispatcher("self.jsp?userid="+useridsh).forward(req,resp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "sta":
                    try {
                        int userids = Integer.parseInt(req.getParameter("userid"));
                        String type_sta = req.getParameter("type_sta");
                        Map<String,Integer> nmcolors= staDao.getMostColor(type_sta,userids);
                        Map<String,Integer> nmskirtcolors = staDao.getMostSkirtColor(type_sta,userids);
                        Map<String,Float> nmtypes= staDao.getMostType(userids);
                        Map<Float,Integer> nmlen= staDao.getMostLen(userids);
                        Map<Float,Integer> nmwaist= staDao.getMostWaist(userids);
                        Map<String,Float> nmmoneyyear= staDao.getMoneyPerYear(type_sta,userids);
                        Map<String,Float> nmmoneymonth= staDao.getMoneyPerMonth(type_sta,userids);
                        Map<String,Float> nmsellyear= staDao.getSellPerYear(type_sta,userids);
                        Map<String,Float> nmsellmonth= staDao.getSellPerMonth(type_sta,userids);
                        Map<String,Integer> nmsize= staDao.getMostSize(userids);


                        req.setAttribute("userid",userids);//转发到jsp页面
                        User user=userBiz.getUserById(userids);
                        req.setAttribute("user",user);
                        req.setAttribute("type_sta",type_sta);
                        req.setAttribute("nmcolors",nmcolors);
                        req.setAttribute("nmskirtcolors",nmskirtcolors);
                        req.setAttribute("nmtypes",nmtypes);
                        req.setAttribute("nmlen",nmlen);
                        req.setAttribute("nmwaist",nmwaist);
                        req.setAttribute("nmsize",nmsize);
                        req.setAttribute("nmmoneyyear",nmmoneyyear);
                        req.setAttribute("nmmoneymonth",nmmoneymonth);
                        req.setAttribute("nmsellyear",nmsellyear);
                        req.setAttribute("nmsellmonth",nmsellmonth);

                        req.getRequestDispatcher("sta.jsp?&type_sta="+type_sta+"userid="+userids).forward(req,resp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "query":
                    try {
                        int pageSize = 10;
                        int pageCount = userBiz.getUserPageCount(pageSize);
                        int pageIndex = Integer.parseInt(req.getParameter("pageIndex"));

                        if(pageIndex < 1){
                            pageIndex = 1;
                        }
                        if(pageIndex > pageCount){
                            pageIndex = pageCount;
                        }

                        List<User> users = userBiz.getUserByPage(pageIndex,pageSize);


                        //存
                        req.setAttribute("pageCount",pageCount);
                        req.setAttribute("users",users);

                        //转发到jsp页面
                        req.getRequestDispatcher("users.jsp?pageIndex="+pageIndex).forward(req,resp);
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    break;
                case "remove":
                    int removeId = Integer.parseInt(req.getParameter("id"));

                    try {
                        int count = userBiz.removeUser(removeId);
                        if (count > 0) {
                            out.println("<script>alert('删除成功'); location.href='user?type=query&pageIndex=1';</script>");
                        } else {
                            out.println("<script>alert('删除失败'); location.href='user?type=query&pageIndex=1';</script>");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.println("<script>alert('" + e.getMessage() + "'); location.href='user?type=query&pageIndex=1';</script>");
                    }
                    break;
                case "search":
                    try {
                        int key = Integer.parseInt(req.getParameter("key"));
                        User user = userBiz.getUserById(key);  //查找结果

                        req.setAttribute("user",user);
                        req.setAttribute("key",key);
                        req.getRequestDispatcher("searchUsers.jsp").forward(req,resp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    resp.sendError(404);
            }
        }

    }

    private void modifyUserG(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) throws Exception {
        //构建一个磁盘工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //设置大小
        factory.setSizeThreshold(1024*9);
        //临时仓库
        File file = new File("d:\\This Term\\Database\\temp");
        if(!file.exists()){
            file.mkdir();//创建文件夹
        }
        factory.setRepository(file);

        //文件上传+表单数据
        ServletFileUpload  fileUpload = new ServletFileUpload(factory);

        //将请求解析成一个个FileItem(文件+表单元素)
        List<FileItem> fileItems = fileUpload.parseRequest(req);

        //遍历FileItem
        User user  =new User();


        int userid=0;
        for(FileItem  item: fileItems){
            if(item.isFormField()){
                //元素名称和用户填写的值  name: trytry
                String name = item.getFieldName();
                String value = item.getString("utf-8");//防止乱码
                switch(name){
                    case "userid":
                        userid = Integer.parseInt(value);
                        user.setId(userid);
                        break;
                    case "phonenumber":
                        break;
                    case "name":
                        user.setName(value);
                        break;
                    case "password":
                        user.setPassword(value);
                        break;
                    case "position":
                        user.setPosition(value);
                        break;
                    case "sex":
                        user.setSex(value);
                        break;
                    case "pic":
                        user.setPic(value);
                        break;
                    case "describe1":
                        user.setDescribe1(value);
                        break;
                }

            }else {
                //文件: 图片的文件名  trytry.png
                String fileName = item.getName();
                //避免文件替换：当前的系统时间.png
                //获取后缀名 .png
                if(fileName.trim().length()>0) {
                    String filterName = fileName.substring(fileName.lastIndexOf("."));
                    //修改文件名  20211117112512234.png
                    SimpleDateFormat sdf0= new SimpleDateFormat("yyyyMMddHHmmssS");  //当前时间
                    fileName = sdf0.format(new Date())+filterName;

                    //保存到哪里
                    //虚拟路径: Image/1-1.png
                    //文件的读写:实际路径 D://xx  --> 虚拟路径: Image对应的实际路径
                    String path = req.getServletContext().getRealPath("/Image");
                    //d:/xxx/xx/ 20211117112512234.png
                    String filePath = path+"/"+fileName;
                    //数据库表中的路径 ：Image/101-1.png：相对项目的根目录的位置
                    String dbPath  = "Image/"+fileName;
                    user.setPic(dbPath);

                    //4.3 保存文件
                    item.write(new File(filePath));
                }
                else{
                    user.setPic("Image/login.jpg");
                }
            }

        }
        //5.将信息保存到数据库
        int count = userBiz.modifyUser(user);
        req.setAttribute("user",user);
        if(count>0){
            out.println("<script>alert('修改成功');location.href='user?type=query&pageIndex=1&userid=2';</script>");
        }else{
            out.println("<script>alert('修改失败');location.href='user?type=query&pageIndex=1&userid=2';</script>");
        }
    }



    private void modifyUser(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) throws Exception {
        //构建一个磁盘工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //设置大小
        factory.setSizeThreshold(1024*9);
        //临时仓库
        File file = new File("d:\\This Term\\Database\\temp");
        if(!file.exists()){
            file.mkdir();//创建文件夹
        }
        factory.setRepository(file);

        //文件上传+表单数据
        ServletFileUpload  fileUpload = new ServletFileUpload(factory);

        //将请求解析成一个个FileItem(文件+表单元素)
        List<FileItem> fileItems = fileUpload.parseRequest(req);

        //遍历FileItem
        User user  =new User();


        int userid=0;
        for(FileItem  item: fileItems){
            if(item.isFormField()){
                //元素名称和用户填写的值  name: trytry
                String name = item.getFieldName();
                String value = item.getString("utf-8");//防止乱码
                switch(name){
                    case "userid":
                        userid = Integer.parseInt(value);
                        user.setId(userid);
                        break;
                    case "phonenumber":
                        break;
                    case "name":
                        user.setName(value);
                        break;
                    case "password":
                        user.setPassword(value);
                        break;
                    case "position":
                        user.setPosition(value);
                        break;
                    case "sex":
                        user.setSex(value);
                        break;
                    case "pic":
                        user.setPic(value);
                        break;
                    case "describe1":
                        user.setDescribe1(value);
                        break;
                }

            }else {
                //文件: 图片的文件名  trytry.png
                String fileName = item.getName();
                //避免文件替换：当前的系统时间.png
                //获取后缀名 .png
                if(fileName.trim().length()>0) {
                    String filterName = fileName.substring(fileName.lastIndexOf("."));
                    //修改文件名  20211117112512234.png
                    SimpleDateFormat sdf0= new SimpleDateFormat("yyyyMMddHHmmssS");  //当前时间
                    fileName = sdf0.format(new Date())+filterName;

                    //保存到哪里
                    //虚拟路径: Image/1-1.png
                    //文件的读写:实际路径 D://xx  --> 虚拟路径: Image对应的实际路径
                    String path = req.getServletContext().getRealPath("/Image");
                    //d:/xxx/xx/ 20211117112512234.png
                    String filePath = path+"/"+fileName;
                    //数据库表中的路径 ：Image/101-1.png：相对项目的根目录的位置
                    String dbPath  = "Image/"+fileName;
                    user.setPic(dbPath);

                    //4.3 保存文件
                    item.write(new File(filePath));
                }
                else{
                    user.setPic("Image/login.jpg");
                }
            }

        }
        //5.将信息保存到数据库
        int count = userBiz.modifyUser(user);
        req.setAttribute("user",user);
        if(count>0){
            out.println("<script>alert('修改成功');location.href='user?type=show&userid="+userid+"';</script>");
        }else{
            out.println("<script>alert('修改失败');location.href='user?type=show&userid="+userid+"';</script>");
        }
    }
}
