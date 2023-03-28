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
                    out.println("<script>alert('�û��������벻����');location.href = 'login.html';</script>");
                }else {
                    int id = user.getId();
                    session.setAttribute("user",user);
                    if(user.getId()!=2){
                        out.println("<script>alert('��¼�ɹ�');location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+id+"';</script>");
                    }
                    else if(user.getId()==2){
                        out.println("<script>alert('��¼�ɹ�');location.href='user?type=query&pageIndex=1&userid="+id+"';</script>");
                    }
                    else{
                        out.println("<script>alert('��¼ʧ��');location.href='login.html';</script>");
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
                    //��֤�û��Ƿ��¼
                    if(session.getAttribute("user")==null){
                        out.println("<script>alert('���¼');parent.window.location.href='login.html';</script>");
                        return;
                    }
                    //1.���session
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
                    //��֤�û��Ƿ��¼
                    if(session.getAttribute("user")==null){
                        out.println("<script>alert('���¼');parent.window.location.href='login.html';</script>");
                        return;
                    }

                    //�޸�����
                    //1.��ȡ�û�������µ�����
                    String newpwd = req.getParameter("newpwd");
                    String newpwda = req.getParameter("newpwda");
                    if(!newpwd.equals(newpwda)){
                        out.println("<script>alert('����������벻һ�£�'); location.href='user?type=modifypwdpre&userid="+userid+"';</script>");
                    }
                    else{
                        //2.��ȡ�û��ı��-session
                        //3.����biz�㷽��
                        int count = 0;
                        try {
                            count = userBiz.modifyPwd(userid,newpwd);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        //4.��Ӧ-�ο�exit
                        if(count>0){
                            out.println("<script>alert('�����޸ĳɹ�');parent.window.location.href='user?type=show&userid="+userid+"';</script>");
                        }else{
                            out.println("<script>alert('�����޸�ʧ��');parent.window.location.href='user?type=show&userid="+userid+"';</script>");
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
                            out.println("<script>alert('���벻��Ϊ�գ�'); location.href='regInfo.jsp';</script>");
                        }
                        if(!pwda_ra.equals(pwd_ra)){
                            out.println("<script>alert('���벻һ�£�'); location.href='regInfo.jsp';</script>");
                        }else{
                            int count_r = userBiz.addUser(phonenumbera,pwd_ra);
                            if (count_r > 0) {
                                out.println("<script>alert('ע��ɹ�'); location.href='user?type=query&pageIndex=1';</script>");
                            } else {
                                out.println("<script>alert('ע��ʧ��'); location.href='user?type=query&pageIndex=1';</script>");
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
                            out.println("<script>alert('���벻��Ϊ�գ�'); location.href='regInfo.jsp';</script>");
                        }
                        if(!pwda_r.equals(pwd_r)){
                            out.println("<script>alert('���벻һ�£�'); location.href='regInfo.jsp';</script>");
                        }else{
                            int count_r = userBiz.addUser(phonenumber,pwd_r);
                            if (count_r > 0) {
                                out.println("<script>alert('ע��ɹ�'); location.href='login.html';</script>");


                            } else {
                                out.println("<script>alert('ע��ʧ��'); location.href='login.html';</script>");

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

                        //ת����jspҳ��
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


                        req.setAttribute("userid",userids);//ת����jspҳ��
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


                        //��
                        req.setAttribute("pageCount",pageCount);
                        req.setAttribute("users",users);

                        //ת����jspҳ��
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
                            out.println("<script>alert('ɾ���ɹ�'); location.href='user?type=query&pageIndex=1';</script>");
                        } else {
                            out.println("<script>alert('ɾ��ʧ��'); location.href='user?type=query&pageIndex=1';</script>");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.println("<script>alert('" + e.getMessage() + "'); location.href='user?type=query&pageIndex=1';</script>");
                    }
                    break;
                case "search":
                    try {
                        int key = Integer.parseInt(req.getParameter("key"));
                        User user = userBiz.getUserById(key);  //���ҽ��

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
        //����һ�����̹���
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //���ô�С
        factory.setSizeThreshold(1024*9);
        //��ʱ�ֿ�
        File file = new File("d:\\This Term\\Database\\temp");
        if(!file.exists()){
            file.mkdir();//�����ļ���
        }
        factory.setRepository(file);

        //�ļ��ϴ�+������
        ServletFileUpload  fileUpload = new ServletFileUpload(factory);

        //�����������һ����FileItem(�ļ�+��Ԫ��)
        List<FileItem> fileItems = fileUpload.parseRequest(req);

        //����FileItem
        User user  =new User();


        int userid=0;
        for(FileItem  item: fileItems){
            if(item.isFormField()){
                //Ԫ�����ƺ��û���д��ֵ  name: trytry
                String name = item.getFieldName();
                String value = item.getString("utf-8");//��ֹ����
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
                //�ļ�: ͼƬ���ļ���  trytry.png
                String fileName = item.getName();
                //�����ļ��滻����ǰ��ϵͳʱ��.png
                //��ȡ��׺�� .png
                if(fileName.trim().length()>0) {
                    String filterName = fileName.substring(fileName.lastIndexOf("."));
                    //�޸��ļ���  20211117112512234.png
                    SimpleDateFormat sdf0= new SimpleDateFormat("yyyyMMddHHmmssS");  //��ǰʱ��
                    fileName = sdf0.format(new Date())+filterName;

                    //���浽����
                    //����·��: Image/1-1.png
                    //�ļ��Ķ�д:ʵ��·�� D://xx  --> ����·��: Image��Ӧ��ʵ��·��
                    String path = req.getServletContext().getRealPath("/Image");
                    //d:/xxx/xx/ 20211117112512234.png
                    String filePath = path+"/"+fileName;
                    //���ݿ���е�·�� ��Image/101-1.png�������Ŀ�ĸ�Ŀ¼��λ��
                    String dbPath  = "Image/"+fileName;
                    user.setPic(dbPath);

                    //4.3 �����ļ�
                    item.write(new File(filePath));
                }
                else{
                    user.setPic("Image/login.jpg");
                }
            }

        }
        //5.����Ϣ���浽���ݿ�
        int count = userBiz.modifyUser(user);
        req.setAttribute("user",user);
        if(count>0){
            out.println("<script>alert('�޸ĳɹ�');location.href='user?type=query&pageIndex=1&userid=2';</script>");
        }else{
            out.println("<script>alert('�޸�ʧ��');location.href='user?type=query&pageIndex=1&userid=2';</script>");
        }
    }



    private void modifyUser(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) throws Exception {
        //����һ�����̹���
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //���ô�С
        factory.setSizeThreshold(1024*9);
        //��ʱ�ֿ�
        File file = new File("d:\\This Term\\Database\\temp");
        if(!file.exists()){
            file.mkdir();//�����ļ���
        }
        factory.setRepository(file);

        //�ļ��ϴ�+������
        ServletFileUpload  fileUpload = new ServletFileUpload(factory);

        //�����������һ����FileItem(�ļ�+��Ԫ��)
        List<FileItem> fileItems = fileUpload.parseRequest(req);

        //����FileItem
        User user  =new User();


        int userid=0;
        for(FileItem  item: fileItems){
            if(item.isFormField()){
                //Ԫ�����ƺ��û���д��ֵ  name: trytry
                String name = item.getFieldName();
                String value = item.getString("utf-8");//��ֹ����
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
                //�ļ�: ͼƬ���ļ���  trytry.png
                String fileName = item.getName();
                //�����ļ��滻����ǰ��ϵͳʱ��.png
                //��ȡ��׺�� .png
                if(fileName.trim().length()>0) {
                    String filterName = fileName.substring(fileName.lastIndexOf("."));
                    //�޸��ļ���  20211117112512234.png
                    SimpleDateFormat sdf0= new SimpleDateFormat("yyyyMMddHHmmssS");  //��ǰʱ��
                    fileName = sdf0.format(new Date())+filterName;

                    //���浽����
                    //����·��: Image/1-1.png
                    //�ļ��Ķ�д:ʵ��·�� D://xx  --> ����·��: Image��Ӧ��ʵ��·��
                    String path = req.getServletContext().getRealPath("/Image");
                    //d:/xxx/xx/ 20211117112512234.png
                    String filePath = path+"/"+fileName;
                    //���ݿ���е�·�� ��Image/101-1.png�������Ŀ�ĸ�Ŀ¼��λ��
                    String dbPath  = "Image/"+fileName;
                    user.setPic(dbPath);

                    //4.3 �����ļ�
                    item.write(new File(filePath));
                }
                else{
                    user.setPic("Image/login.jpg");
                }
            }

        }
        //5.����Ϣ���浽���ݿ�
        int count = userBiz.modifyUser(user);
        req.setAttribute("user",user);
        if(count>0){
            out.println("<script>alert('�޸ĳɹ�');location.href='user?type=show&userid="+userid+"';</script>");
        }else{
            out.println("<script>alert('�޸�ʧ��');location.href='user?type=show&userid="+userid+"';</script>");
        }
    }
}
