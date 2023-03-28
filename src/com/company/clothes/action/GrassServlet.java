package com.company.clothes.action;

import com.alibaba.fastjson.JSON;
import com.company.clothes.bean.Clothes;
import com.company.clothes.bean.Grass;
import com.company.clothes.bean.User;
import com.company.clothes.biz.UserBiz;
import com.company.clothes.biz.grassBiz;
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
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@WebServlet(name = "grass", urlPatterns="/grass")
public class GrassServlet extends HttpServlet {
    grassBiz  grassBiz = new grassBiz();
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
     * /grass?type=addpre ���ǰ׼��
     * /wardorbe?type=add ���
     * /grass?type=modifypre&id=xx �޸�ǰ׼��
     * /grass?type=modify        �޸�
     * /grass?type=buy        ����
     * /grass?type=buypre        ����ǰ׼��
     * /grass?type=payfront        ������
     * /grass?type=remove&id=xx    ɾ��
     * /grass?type=query&type_q=xx&pageIndex=1 :��ҳ�����ѯ(request:ת��)
     * /grass?type=doajax&name=xx  :ʹ��ajax��ѯͼ������Ӧ��ͼ����Ϣ
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
        HttpSession session = req.getSession();
        if(session.getAttribute("user")==null){
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
                req.getRequestDispatcher("beforeAddGrass.jsp").forward(req, resp);
                break;
            case "addpre":
                String name_a = req.getParameter("name_a");
                if (name_a==null) {
                    out.println("<script>alert('���Ʋ���Ϊ�գ�'); location.href='grass?type=addtemp&userid="+userid+"';</script>");
                }
                String type_a = req.getParameter("type_a");
                String howtobuy =req.getParameter("howtobuy");
                req.setAttribute("userid", userid);
                req.setAttribute("name_a", name_a);
                req.setAttribute("type_a", type_a);
                req.setAttribute("howtobuy", howtobuy);
                if("��ȹ".equals(type_a)||"��ɫȹ".equals(type_a)){
                    req.getRequestDispatcher("BAGskirt.jsp").forward(req, resp);
                }
                else{
                    req.getRequestDispatcher("BAGothers.jsp").forward(req, resp);
                }
                break;
            case "add":
                try {
                    addGrass(req, resp, out, userid);
                } catch (FileUploadException e) {
                    e.printStackTrace();
                    resp.sendError(500, "�ļ��ϴ�ʧ��");
                } catch (Exception e) {
                    e.printStackTrace();
                    resp.sendError(500, e.getMessage());
                }
                break;
            case "modifypre":
                //grass?type=modifypre&id=xx
                int grassId = Integer.parseInt(req.getParameter("id"));
                Grass grass = null;
                try {
                    grass = grassBiz.getGrassById(grassId,userid);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                req.setAttribute("grass", grass);
                req.setAttribute("userid", userid);
                req.getRequestDispatcher("modifyGrass.jsp?useid="+userid).forward(req, resp);
                break;
            case "modify":
                try {
                    modifyGrass(req, resp, out,userid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "buypre":
                int id = Integer.parseInt(req.getParameter("id"));
                grass = null;
                try {
                    grass = grassBiz.getGrassById(id,userid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(!("�ֻ�".equals(grass.getFlag())||"�Ѹ�����".equals(grass.getFlag().split(" ")[1]))){
                    out.println("<script>alert('Ԥ����Ʒδ�����𣬲��ɹ���'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
                }
                else{
                    req.setAttribute("grass", grass);
                    req.setAttribute("userid", userid);
                    req.getRequestDispatcher("beforeBuy.jsp?userid="+userid).forward(req, resp);
                }
                break;
            case "buy":
                try{
                    buyGrass(req,resp,out,userid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "payfront":
                id = Integer.parseInt(req.getParameter("id"));
                try{
                    grass = grassBiz.getGrassById(id,userid);
                    int count = grassBiz.payFrontGrass(grass,userid);
                    if (count > 0) {
                        out.println("<script>alert('������ɹ���'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
                    } else {
                        String flag = grass.getFlag();
                        if("Ԥ��".equals(flag.split(" ")[0])){
                            out.println("<script>alert('�Ѹ�����'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
                        }
                        else{
                            out.println("<script>alert('���ֻ������ɸ�����'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "query":
                try {
                    queryGrass(req, resp, out,userid);
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
                    int count = grassBiz.removeGrass(removeId,userid);
                    if (count > 0) {
                        out.println("<script>alert('ɾ���ɹ�'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
                    } else {
                        out.println("<script>alert('ɾ��ʧ��'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("<script>alert('" + e.getMessage() + "'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
                }
                //3. ��ʾ+��ת=>out (��ѯ��servlet)
                break;
            case "searchpre":
                req.setAttribute("userid",userid);//ת����jspҳ��
                req.getRequestDispatcher("searchGrass.jsp?userid="+userid).forward(req,resp);
                break;
            case "search":
                try {
                    String key = req.getParameter("key");
                    String type_s = req.getParameter("type_s");
                    String type_q = req.getParameter("type_q");
                    int pageSize = 10;
                    int pageCount = grassBiz.getKeyPageCount(pageSize,key,type_s,userid);
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
                    List<Grass> grasses;
                    grasses = grassBiz.getGrassByKeyAndPage(pageIndex,pageSize,key,userid,type_s);  //���ҽ��

                    req.setAttribute("userid",userid);//ת����jspҳ��
                    req.setAttribute("pageCount",pageCount);
                    req.setAttribute("key",key);
                    req.setAttribute("pageIndex",pageIndex);
                    req.setAttribute("grasses",grasses);
                    req.setAttribute("type_q",type_q);
                    req.setAttribute("type_s",type_s);
                    req.getRequestDispatcher("searchGrass.jsp?pageIndex="+pageIndex+"&userid="+userid+"&type_q="+type_q+"&type_s="+type_s).forward(req,resp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                resp.sendError(404);
        }
    }
    /**
     * ��ҳ��ѯ
     * grass?type=query&pageIndex=1
     * ҳ��: biz
     * ��ǰҳ��:pageIndex = 1
     * ��:request,ת��
     * @param req
     * @param resp
     * @throws Exception 
     */
    private void queryGrass(HttpServletRequest req, HttpServletResponse resp, PrintWriter out,int userid) throws Exception {
        //��ȡ��Ϣ(ҳ����ҳ��,��Ϣ)
        UserDao userdao = new UserDao();
        int pageSize = 10;
        int pageCount = grassBiz.getGrassPageCount(pageSize,userid);
        int pageIndex = Integer.parseInt(req.getParameter("pageIndex"));
        String type_q = req.getParameter("type_q");
        String type_q_g = req.getParameter("type_q_g");

        if(pageIndex < 1){
            pageIndex = 1;
        }
        if(pageIndex > pageCount){
            pageIndex = pageCount;
        }

        List<Grass> grasses = grassBiz.getGrassByPage(pageIndex,pageSize,type_q,type_q_g,userid);

        User useri = userdao.getUserById(userid);

        //��
        req.setAttribute("pageCount",pageCount);
        req.setAttribute("grasses",grasses);
        req.setAttribute("type_q_g",type_q_g);
        req.setAttribute("type_q",type_q);
        req.setAttribute("userid",userid);
        req.setAttribute("useri",useri);

        //ת����jspҳ��
        req.getRequestDispatcher("grass.jsp?pageIndex="+pageIndex+"&type_q="+type_q+"&type_q_g="+type_q_g+"&userid="+userid).forward(req,resp);
    }


    /**
     * �޸�
     * @param req
     * @param resp
     * @param out
     */
    private void modifyGrass(HttpServletRequest req, HttpServletResponse resp, PrintWriter out,int userid) throws Exception {
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
        Grass grass  =new Grass();

        SimpleDateFormat sdf = new SimpleDateFormat();// ��ʽ��ʱ��
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// aΪam/pm�ı��
        Date date = new Date();// ��ȡ��ǰʱ��
        String time = sdf.format(date);
        grass.setCreatetime(time);
        grass.setUpdatetime(time);
        String howtobuy = null;
        String type = null;
        String colora = null;
        String colorb = null;
        String specsa = null;
        String specsb = null;
        String specsc = null;

        String frontprice = "0";
        String tailprice = "0";

        String name_m=null;

        String flag = null;

        for(FileItem  item: fileItems){
            if(item.isFormField()){
                //Ԫ�����ƺ��û���д��ֵ  name: trytry
                String name = item.getFieldName();
                String value = item.getString("utf-8");//��ֹ����
                switch(name){
                    case "id":
                        grass.setId(Integer.parseInt(value));
                        break;
                    case "howtobuy":
                        howtobuy = value;
                        break;
                    case "type_m":
                        type = value;
                        grass.setType(value);
                        break;
                    case "name_m":
                        name_m = value;
                        grass.setName(value);
                        break;
                    case "price":
                        grass.setPrice(Float.parseFloat(value));
                        break;
                    case "flag":
                        flag = value;
                        break;
                    case "frontprice":
                        frontprice = value;
                        grass.setFrontprice(Float.parseFloat(frontprice));
                        break;
                    case "tailprice":
                        tailprice = value;
                        grass.setTailprice(Float.parseFloat(tailprice));
                        break;
                    case "buytime":
                        grass.setBuytime(value);
                        break;
                    case "len":
                        grass.setLen(Float.parseFloat(value));
                        break;
                    case "pic":
                        grass.setPic(value);
                        break;
                    case "waist":
                        grass.setWaist(Float.parseFloat(value));
                        break;
                    case "size":
                        grass.setSize(value);
                        break;
                    case "color":
                        grass.setColor(value);
                        break;
                    case "describe":
                        grass.setDescribe1(value);
                        break;
                    case "specs":
                        grass.setSpecs(value);
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
                if("�ֻ�".equals(howtobuy)){
                    grass.setFlag(howtobuy);
                    grass.setFrontprice((float) 0);
                    grass.setTailprice((float) 0);
                    grass.setBuytime(null);
                }
                else{
                    grass.setFlag(howtobuy+" "+flag);
                    grass.setPrice(Float.parseFloat(frontprice)+Float.parseFloat(tailprice));
                }
                if("��ȹ".equals(type)){
                    String color = colora+" "+colorb;
                    grass.setColor(color);
                }
                else if("ˮ�ַ�".equals(type)){
                    String specs = specsa+"�� "+specsb;

                    grass.setSpecs(specs);
                }
                else if("ë��".equals(type)){
                    String specs = specsa+" "+specsb+" "+specsc;

                    grass.setSpecs(specs);
                }
                else if("����".equals(type)){
                    String specs = specsa+" "+specsb;

                    grass.setSpecs(specs);
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
                    grass.setPic(dbPath);

                    //4.3 �����ļ�
                    item.write(new File(filePath));
                }

            }

        }
        //5.����Ϣ���浽���ݿ�
        if (name_m==null) {
            out.println("<script>alert('���Ʋ���Ϊ�գ�'); location.href='grass?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
        }
        int count = grassBiz.modifyGrass(grass,userid);
        if(count>0){
            out.println("<script>alert('�޸ĳɹ�');location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
        }else{
            out.println("<script>alert('�޸�ʧ��');location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
        }
    }



    /**
     * ���
     *  1.enctype="multipart/form-data":����ǰ��ͬ
     *  ��ȡ��Ԫ��  req.getParameter("name") : error
     *  2.�ļ��ϴ� ��ͼƬ�ļ���������˱��浽�������� (������ FileUpload+io)
     *  3.·��
     *    ͼƬ:
     *    D:\This Term\Database\pic\trytry.png  :ʵ��·��
     *    http://localhost:8080/IdeaProject_war_exploded/Image/trytry.png:����·��(������)
     *
     *
     * @param req
     * @param resp
     */
    private void addGrass(HttpServletRequest req, HttpServletResponse resp,  PrintWriter  out,int userid) throws Exception {
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
        ServletFileUpload fileUpload = new ServletFileUpload(factory);

        //�����������һ����FileItem(�ļ�+��Ԫ��)
        List<FileItem> fileItems = fileUpload.parseRequest(req);

        //����FileItem
        Grass grass  =new Grass();

        SimpleDateFormat sdf = new SimpleDateFormat();// ��ʽ��ʱ��
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// aΪam/pm�ı��
        Date date = new Date();// ��ȡ��ǰʱ��
        String time = sdf.format(date);
        grass.setCreatetime(time);
        grass.setUpdatetime(time);
        String howtobuy = null;
        String type = null;
        String colora = null;
        String colorb = null;
        String specsa = null;
        String specsb = null;
        String specsc = null;

        String frontprice = "0";
        String tailprice = "0";

        String flag = "�ֻ�";

        for(FileItem  item: fileItems){
            if(item.isFormField()){
                //Ԫ�����ƺ��û���д��ֵ  name: trytry
                String name = item.getFieldName();
                String value = item.getString("utf-8");//��ֹ����
                switch(name){
                    case "howtobuy":
                        howtobuy = value;
                        break;
                    case "type_a":
                        type = value;
                        grass.setType(value);
                        break;
                    case "name_a":
                        grass.setName(value);
                        break;
                    case "price":
                        grass.setPrice(Float.parseFloat(value));
                        break;
                    case "flag":
                        flag = value;
                        break;
                    case "frontprice":
                        frontprice = value;
                        grass.setFrontprice(Float.parseFloat(frontprice));
                        break;
                    case "tailprice":
                        tailprice = value;
                        grass.setTailprice(Float.parseFloat(tailprice));
                        break;
                    case "buytime":
                        grass.setBuytime(value);
                        break;
                    case "len":
                        grass.setLen(Float.parseFloat(value));
                        break;
                    case "waist":
                        grass.setWaist(Float.parseFloat(value));
                        break;
                    case "size":
                        grass.setSize(value);
                        break;
                    case "color":
                        grass.setColor(value);
                        break;
                    case "describe":
                        grass.setDescribe1(value);
                        break;
                    case "specs":
                        grass.setSpecs(value);
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
                if("�ֻ�".equals(howtobuy)){
                    grass.setFlag(howtobuy);
                    grass.setFrontprice((float) 0);
                    grass.setTailprice((float) 0);
                    grass.setBuytime(null);
                }
                else{
                    grass.setFlag(howtobuy+" "+flag);
                    grass.setPrice(Float.parseFloat(frontprice)+Float.parseFloat(tailprice));
                }
                if("��ȹ".equals(type)){
                    String color = colora+" "+colorb;
                    grass.setColor(color);
                }
                else if("ˮ�ַ�".equals(type)){
                    String specs = specsa+"�� "+specsb;

                    grass.setSpecs(specs);
                }
                else if("ë��".equals(type)){
                    String specs = specsa+" "+specsb+" "+specsc;

                    grass.setSpecs(specs);
                }
                else if("����".equals(type)){
                    String specs = specsa+" "+specsb;

                    grass.setSpecs(specs);
                }

            }else {
                //�ļ�: ͼƬ���ļ���  trytry.png
                String fileName = item.getName();
                //�����ļ��滻����ǰ��ϵͳʱ��.png
                //��ȡ��׺�� .png
                if(fileName.trim().length()>0){
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
                    grass.setPic(dbPath);

                    //4.3 �����ļ�
                    item.write(new File(filePath));
                }
            }

        }
        int count = grassBiz.addGrass(grass,userid);
        if(count>0){
            out.println("<script>alert('��ӳɹ�');location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
        }else{
            out.println("<script>alert('���ʧ��');location.href='beforeAddGrass.jsp?userid="+userid+"';</script>");
        }
    }

    private void buyGrass(HttpServletRequest req, HttpServletResponse resp,  PrintWriter  out,int userid) throws Exception {
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
        ServletFileUpload fileUpload = new ServletFileUpload(factory);

        //�����������һ����FileItem(�ļ�+��Ԫ��)
        List<FileItem> fileItems = fileUpload.parseRequest(req);

        //����FileItem
        Grass grass  =new Grass();

        SimpleDateFormat sdf = new SimpleDateFormat();// ��ʽ��ʱ��
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// aΪam/pm�ı��
        Date date = new Date();// ��ȡ��ǰʱ��
        String time = sdf.format(date);
        grass.setCreatetime(time);
        grass.setUpdatetime(time);
        String type = null;
        String colora = null;
        String colorb = null;
        String specsa = null;
        String specsb = null;
        String specsc = null;
        String id = "0";

        for(FileItem  item: fileItems){
            if(item.isFormField()){
                //Ԫ�����ƺ��û���д��ֵ  name: trytry
                String name = item.getFieldName();
                String value = item.getString("utf-8");//��ֹ����
                switch(name){
                    case "id":
                        id = value;
                        break;
                    case "type_m":
                        type = value;
                        grass.setType(value);
                        break;
                    case "pic":
                        grass.setPic(value);
                        break;
                    case "name_m":
                        grass.setName(value);
                        break;
                    case "price":
                        grass.setPrice(Float.parseFloat(value));
                        break;
                    case "len":
                        grass.setLen(Float.parseFloat(value));
                        break;
                    case "waist":
                        grass.setWaist(Float.parseFloat(value));
                        break;
                    case "size":
                        grass.setSize(value);
                        break;
                    case "color":
                        grass.setColor(value);
                        break;
                    case "describe":
                        grass.setDescribe1(value);
                        break;
                    case "specs":
                        grass.setSpecs(value);
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
                    grass.setColor(color);
                }
                else if("ˮ�ַ�".equals(type)){
                    String specs = specsa+"�� "+specsb;

                    grass.setSpecs(specs);
                }
                else if("ë��".equals(type)){
                    String specs = specsa+" "+specsb+" "+specsc;

                    grass.setSpecs(specs);
                }
                else if("����".equals(type)){
                    String specs = specsa+" "+specsb;

                    grass.setSpecs(specs);
                }

            }else {
                //�ļ�: ͼƬ���ļ���  trytry.png
                String fileName = item.getName();
                //�����ļ��滻����ǰ��ϵͳʱ��.png
                //��ȡ��׺�� .png
                if(fileName.trim().length()>0){
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
                    grass.setPic(dbPath);

                    //4.3 �����ļ�
                    item.write(new File(filePath));
                }
            }

        }
        int count = grassBiz.buyGrass(grass,userid);
        if (count > 0) {
            out.println("<script>alert('����ɹ���'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
            grassBiz.removeGrass(Integer.parseInt(id),userid);
        } else {
            out.println("<script>alert('����ʧ��'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
        }
    }



    public static void main(String[] args) {
        try {
            grassBiz grassBiz = new grassBiz();
            List<Grass> grasses = grassBiz.getGrassByPage(1,3,"all","all",1);
            System.out.println(grasses.size());
            int pageCount = grassBiz.getGrassPageCount(3,1);
            for(Grass grass : grasses){
                System.out.println(grass);
                System.out.println(grass.getCreatetime());
            }
            System.out.println(pageCount);
            @SuppressWarnings("unused")
            grassDao grassDao = new grassDao();

            //System.out.println(count);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

    }
}
