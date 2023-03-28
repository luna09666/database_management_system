package com.company.clothes.action;

import com.company.clothes.bean.User;
import com.company.clothes.bean.Wear;
import com.company.clothes.biz.*;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@WebServlet(name = "wear", urlPatterns="/wear")
public class WearServlet extends HttpServlet {
    wearBiz  wearBiz = new wearBiz();
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

    /**
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter  out  = resp.getWriter();

        //��֤�û��Ƿ��¼
        HttpSession user = req.getSession();
        if(user.getAttribute("user")==null){
           out.println("<script>alert('���¼');parent.window.location.href='login.html';</script>");
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
            case "addpre":
                req.setAttribute("userid", userid);
                req.getRequestDispatcher("beforeAddWear.jsp?userid="+userid).forward(req, resp);
                break;
            case "add":
                try {
                    add(req, resp, out, userid);
                } catch (FileUploadException e) {
                    e.printStackTrace();
                    resp.sendError(500, "�ļ��ϴ�ʧ��");
                } catch (Exception e) {
                    e.printStackTrace();
                    resp.sendError(500, e.getMessage());
                }
                break;
            case "query":
                try {
                    int pageSize = 10;
                    int pageCount = wearBiz.getWearPageCount(pageSize,userid);
                    int pageIndex = Integer.parseInt(req.getParameter("pageIndex"));
                    String type_q = req.getParameter("type_q");

                    if(pageIndex < 1){
                        pageIndex = 1;
                    }
                    if(pageIndex > pageCount){
                        pageIndex = pageCount;
                    }

                    List<Wear> wearsa = wearBiz.getWearByPage(pageIndex,pageSize,userid);
                    List<Wear> wears = new ArrayList<Wear>();
                    for (int i=wearsa.size()-1;i>=0;i--){
                        wears.add(wearsa.get(i));
                    }

                    //��
                    req.setAttribute("pageCount",pageCount);
                    req.setAttribute("wears",wears);
                    req.setAttribute("userid",userid);
                    req.setAttribute("useri",useri);

                    //ת����jspҳ��
                    req.getRequestDispatcher("wear.jsp?&pageIndex="+pageIndex+"&userid="+userid).forward(req,resp);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;
            case "remove":
                //1.��ȡɾ����clothesid
                int removeId = Integer.parseInt(req.getParameter("id"));

                //2. ����bizɾ������
                try {
                    int count = wearBiz.removeWear(removeId,userid);
                    if (count > 0) {
                        out.println("<script>alert('ɾ���ɹ�'); location.href='wear?type=query&pageIndex=1&userid="+userid+"';</script>");
                    } else {
                        out.println("<script>alert('ɾ��ʧ��'); location.href='wear?type=query&pageIndex=1&userid="+userid+"';</script>");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("<script>alert('" + e.getMessage() + "'); location.href='wear?type=query&pageIndex=1&userid="+userid+"';</script>");
                }
                //3. ��ʾ+��ת=>out (��ѯ��servlet)
                break;
            default:
                resp.sendError(404);
        }
    }




    /**
     * ����鼮
     *  1.enctype="multipart/form-data":����ǰ��ͬ
     *  ��ȡ��Ԫ��  req.getParameter("name") : error
     *  2.�ļ��ϴ� ��ͼƬ�ļ���������˱��浽�������� (������ FileUpload+io)
     *  3.·��
     *    ͼƬ:
     *    D:\This Term\Database\pic\trytry.png  :ʵ��·��
     *    http://localhost:8080/IdeaProject_war_exploded/Image/trytry.png:����·��(������)
     *
     *  @param req
     * @param resp
     * @param userid
     */
    private void add(HttpServletRequest req, HttpServletResponse resp, PrintWriter  out, int userid) throws Exception {
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
        Wear wear  =new Wear();

        SimpleDateFormat sdf = new SimpleDateFormat();// ��ʽ��ʱ��
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// aΪam/pm�ı��
        Date date = new Date();// ��ȡ��ǰʱ��
        String time = sdf.format(date);
        wear.setCreatetime(time);
        wear.setUpdatetime(time);
        String title=null;

        for(FileItem  item: fileItems){
            if(item.isFormField()){
                //Ԫ�����ƺ��û���д��ֵ  name: trytry
                String name = item.getFieldName();
                String value = item.getString("utf-8");//��ֹ����
                switch(name){
                    case "title":
                        title=value;
                        wear.setTitle(value);
                        break;
                    case "content":
                        wear.setContent(value);
                        break;
                    case "pic":
                        wear.setPic(value);
                        break;
                }

            }else {
                //�ļ�: ͼƬ���ļ���  trytry.png
                String fileName = item.getName();
                //�����ļ��滻����ǰ��ϵͳʱ��.png
                if(fileName.trim().length()>0){
                    //��ȡ��׺�� .png
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
                    wear.setPic(dbPath);

                    //4.3 �����ļ�
                    item.write(new File(filePath));
                }
            }
        }
        int count = wearBiz.addWear(wear, userid);
        if(title==null){
            out.println("<script>alert('���ⲻ��Ϊ�գ�');location.href='wear?type=addtemp&userid="+userid+"';</script>");
        }
        if(count>0){
            out.println("<script>alert('��ӳɹ�');location.href='wear?type=query&pageIndex=1&userid="+userid+"';</script>");
        }else{
            out.println("<script>alert('���ʧ��');location.href='wear?type=addtemp&userid="+userid+"';</script>");
        }
    }




    public static void main(String[] args) {
        try {
            wearBiz wearBiz = new wearBiz();
            List<Wear> wears = wearBiz.getWearByPage(1,3,1);
            System.out.println(wears.size());
            int pageCount = wearBiz.getWearPageCount(3,1);
            for(Wear clothes : wears){
                System.out.println(clothes);
                System.out.println(clothes.getCreatetime());
            }
            System.out.println(pageCount);

            //System.out.println(count);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

    }
}
