package com.company.clothes.action;

import com.company.clothes.bean.*;
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


@WebServlet(name = "wardrobe", urlPatterns="/wardrobe")
public class ClothesServlet extends HttpServlet {
    clothesBiz  clothesBiz = new clothesBiz();
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
     * /wardrobe?type=addpre ���ǰ׼��
     * /wardorbe?type=add ��ӣ��ڶ�����
     * /wardrobe?type=modifypre&id=xx �޸�ǰ׼��
     * /wardrobe?type=modify        �޸�
     * /wardrobe?type=sell&id=xx        ����ǰ׼��
     * /wardrobe?type=sell        ����
     * /wardrobe?type=remove&id=xx    ɾ��
     * /wardrobe?type=query&pageIndex=1 :��ҳ��ѯ(request:ת��)
     * /wardrobe?type=query&type_q=xx&pageIndex=1 :��ҳ�����ѯ(request:ת��)
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
            case "addtemp":
                req.setAttribute("userid", userid);
                req.getRequestDispatcher("beforeAddClothes.jsp").forward(req, resp);
                break;
            case "addpre":
                String name_a = req.getParameter("name_a");
                if (name_a==null||"".equals(name_a)) {
                    out.println("<script>alert('���Ʋ���Ϊ�գ�'); location.href='wardrobe?type=addtemp&userid="+userid+"';</script>");
                }

                float price = 100;
                String type_a = req.getParameter("type_a");
                if (req.getParameter("price")==null||"".equals(req.getParameter("price"))) {
                    out.println("<script>alert('�۸���Ϊ�գ�'); location.href='wardrobe?type=addtemp&userid="+userid+"';</script>");
                }
                else{
                    price = Float.parseFloat(req.getParameter("price"));
                    req.setAttribute("price", price);
                }
                req.setAttribute("userid", userid);
                req.setAttribute("name_a", name_a);
                req.setAttribute("type_a", type_a);
                if("��ȹ".equals(type_a)||"��ɫȹ".equals(type_a)){
                    req.getRequestDispatcher("BACskirt.jsp?useid="+userid).forward(req, resp);
                }
                else{
                    req.getRequestDispatcher("BACothers.jsp?useid="+userid).forward(req, resp);
                }
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
            case "modifypre":
                //wardrobe?type=modifypre&id=xx
                int clothesId = Integer.parseInt(req.getParameter("id"));
                Clothes clothes = null;
                try {
                    clothes = clothesBiz.getWardrobeById(clothesId,userid);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                req.setAttribute("clothes", clothes);
                req.setAttribute("userid", userid);
                req.getRequestDispatcher("modify.jsp?userid="+userid).forward(req, resp);
                break;
            case "modify":
                try {
                    modify(req, resp, out, userid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "query":
                try {
                    int pageSize = 10;
                    int pageCount = clothesBiz.getWardrobePageCount(pageSize,userid);
                    int pageIndex = Integer.parseInt(req.getParameter("pageIndex"));
                    String type_q = req.getParameter("type_q");

                    if(pageIndex < 1){
                        pageIndex = 1;
                    }
                    if(pageIndex > pageCount){
                        pageIndex = pageCount;
                    }

                    List<Clothes> cclothes = clothesBiz.getWardrobeByPage(pageIndex,pageSize,type_q,userid);


                    //��
                    req.setAttribute("pageCount",pageCount);
                    req.setAttribute("cclothes",cclothes);
                    req.setAttribute("type_q",type_q);
                    req.setAttribute("userid",userid);
                    req.setAttribute("useri",useri);

                    //ת����jspҳ��
                    req.getRequestDispatcher("wardrobe.jsp?type_q="+type_q+"&pageIndex="+pageIndex+"&userid="+userid).forward(req,resp);
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
                    int count = clothesBiz.removeWardrobe(removeId,userid);
                    if (count > 0) {
                        out.println("<script>alert('ɾ���ɹ�'); location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
                    } else {
                        out.println("<script>alert('ɾ��ʧ��'); location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("<script>alert('" + e.getMessage() + "'); location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
                }
                //3. ��ʾ+��ת=>out (��ѯ��servlet)
                break;
            case "searchpre":
                req.setAttribute("userid",userid);//ת����jspҳ��
                req.getRequestDispatcher("searchClothes.jsp?userid="+userid).forward(req,resp);
                break;
            case "search":
                try {
                    String key = req.getParameter("key");
                    String type_s = req.getParameter("type_s");
                    String type_q = req.getParameter("type_q");
                    int pageSize = 10;
                    int pageCount = clothesBiz.getKeyPageCount(pageSize,key,type_s,userid);
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
                    List<Clothes> cclothes;
                    cclothes = clothesBiz.getWardrobeByKeyAndPage(pageIndex,pageSize,key,userid,type_s);  //���ҽ��

                    req.setAttribute("userid",userid);//ת����jspҳ��
                    req.setAttribute("pageCount",pageCount);
                    req.setAttribute("key",key);
                    req.setAttribute("pageIndex",pageIndex);
                    req.setAttribute("cclothes",cclothes);
                    req.setAttribute("type_q",type_q);
                    req.setAttribute("type_s",type_s);
                    req.getRequestDispatcher("searchClothes.jsp?pageIndex="+pageIndex+"&userid="+userid+"&type_q="+type_q+"&type_s="+type_s).forward(req,resp);
                    } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "sell":
                int id_s = Integer.parseInt(req.getParameter("id"));
                Clothes clothes2 = null;
                try {
                    clothes2 = clothesBiz.getWardrobeById(id_s,userid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                req.setAttribute("clothes",clothes2);
                req.setAttribute("userid",userid);//ת����jspҳ��
                req.getRequestDispatcher("beforeSell.jsp?userid="+userid).forward(req,resp);
                break;
            default:
                resp.sendError(404);
        }
    }


    /**
     * �޸�
     * @param req
     * @param resp
     * @param out
     */
    private void modify(HttpServletRequest req, HttpServletResponse resp, PrintWriter out,int userid) throws Exception {
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
        Clothes clothes  =new Clothes();

        SimpleDateFormat sdf = new SimpleDateFormat();// ��ʽ��ʱ��
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// aΪam/pm�ı��
        Date date = new Date();// ��ȡ��ǰʱ��
        String time = sdf.format(date);
        clothes.setCreatetime(time);
        clothes.setUpdatetime(time);
        String type = null;
        String colora = null;
        String colorb = null;
        String specsa = null;
        String specsb = null;
        String specsc = null;
        String name_m=null;

        for(FileItem  item: fileItems){
            if(item.isFormField()){
                //Ԫ�����ƺ��û���д��ֵ  name: trytry
                String name = item.getFieldName();
                String value = item.getString("utf-8");//��ֹ����
                switch(name){
                    case "id":
                        clothes.setId(Integer.parseInt(value));
                        break;
                    case "type_m":
                        type = value;
                        clothes.setType(value);
                        break;
                    case "name_m":
                        name_m = value;
                        clothes.setName(value);
                        break;
                    case "price":
                        clothes.setPrice(Float.parseFloat(value));
                        break;
                    case "pic":
                        clothes.setPic(value);
                        break;
                    case "len":
                        clothes.setLen(Float.parseFloat(value));
                        break;
                    case "waist":
                        clothes.setWaist(Float.parseFloat(value));
                        break;
                    case "size":
                        clothes.setSize(value);
                        break;
                    case "color":
                        clothes.setColor(value);
                        break;
                    case "describe":
                        clothes.setDescribe1(value);
                        break;
                    case "specs":
                        clothes.setSpecs(value);
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
                if("��ȹ".equals(type)){
                    String color = colora+" "+colorb;
                    clothes.setColor(color);
                }
                else if("ˮ�ַ�".equals(type)){
                    String specs = specsa+"�� "+specsb;

                    clothes.setSpecs(specs);
                }
                else if("ë��".equals(type)){
                    String specs = specsa+" "+specsb+" "+specsc;

                    clothes.setSpecs(specs);
                }
                else if("����".equals(type)){
                    String specs = specsa+" "+specsb;

                    clothes.setSpecs(specs);
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
                    clothes.setPic(dbPath);

                    //4.3 �����ļ�
                    item.write(new File(filePath));
                }

            }

        }
        //5.����Ϣ���浽���ݿ�
        int count = clothesBiz.modifyWardrobe(clothes,userid);
        if (name_m==null) {
            out.println("<script>alert('���Ʋ���Ϊ�գ�'); location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
        }
        if(count>0){
            out.println("<script>alert('�޸ĳɹ�');location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
        }else{
            out.println("<script>alert('�޸�ʧ��');location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
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
        Clothes clothes  =new Clothes();

        SimpleDateFormat sdf = new SimpleDateFormat();// ��ʽ��ʱ��
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// aΪam/pm�ı��
        Date date = new Date();// ��ȡ��ǰʱ��
        String time = sdf.format(date);
        clothes.setCreatetime(time);
        clothes.setUpdatetime(time);
        String type = null;
        String colora = null;
        String colorb = null;
        String specsa = null;
        String specsb = null;
        String specsc = null;
        String id = null;
        String grass = null;

        for(FileItem  item: fileItems){
            if(item.isFormField()){
                //Ԫ�����ƺ��û���д��ֵ  name: trytry
                String name = item.getFieldName();
                String value = item.getString("utf-8");//��ֹ����
                switch(name){
                    case "id":
                        id = value;
                        break;
                    case "type_a":
                        type = value;
                        clothes.setType(value);
                        break;
                    case "name_a":
                        clothes.setName(value);
                        break;
                    case "price":
                        clothes.setPrice(Float.parseFloat(value));
                        break;
                    case "len":
                        clothes.setLen(Float.parseFloat(value));
                        break;
                    case "waist":
                        clothes.setWaist(Float.parseFloat(value));
                        break;
                    case "size":
                        clothes.setSize(value);
                        break;
                    case "color":
                        clothes.setColor(value);
                        break;
                    case "describe":
                        clothes.setDescribe1(value);
                        break;
                    case "specs":
                        clothes.setSpecs(value);
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
                        clothes.setPic(value);
                        break;
                    case "grass":
                        grass = value;
                        break;
                }
                if("��ȹ".equals(type)){
                    String color = colora+" "+colorb;
                    clothes.setColor(color);
                }
                else if("ˮ�ַ�".equals(type)){
                    String specs = specsa+"�� "+specsb;

                    clothes.setSpecs(specs);
                }
                else if("ë��".equals(type)){
                    String specs = specsa+" "+specsb+" "+specsc;

                    clothes.setSpecs(specs);
                }
                else if("����".equals(type)){
                    String specs = specsa+" "+specsb;

                    clothes.setSpecs(specs);
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
                    clothes.setPic(dbPath);

                    //4.3 �����ļ�
                    item.write(new File(filePath));
                }
            }

        }
        int count = clothesBiz.addWardrobe(clothes, userid);
        if(count>0){
            if("grass".equals(grass)){
                assert id != null;
                grassBiz grassBiz = new grassBiz();
                int counta = grassBiz.removeGrass(Integer.parseInt(id), userid);
                if(counta>0){
                    out.println("<script>alert('��ӳɹ�');location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
                }
                else{
                    out.println("<script>alert('���ʧ��');location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
                }
            }
            else{
                out.println("<script>alert('��ӳɹ�');location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
            }

        }else{
            out.println("<script>alert('���ʧ��');location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
        }
    }




    public static void main(String[] args) {
        try {
            clothesBiz clothesBiz = new clothesBiz();
            List<Clothes> cclothes = clothesBiz.getWardrobeByPage(1,3,"ȫ��",1);
            System.out.println(cclothes.size());
            int pageCount = clothesBiz.getWardrobePageCount(3,1);
            for(Clothes clothes : cclothes){
                System.out.println(clothes);
                System.out.println(clothes.getCreatetime());
            }
            System.out.println(pageCount);
            @SuppressWarnings("unused")
            clothesDao clothesDao = new clothesDao();

            //System.out.println(count);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

    }
}
