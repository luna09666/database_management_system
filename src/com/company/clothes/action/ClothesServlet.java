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
     * /wardrobe?type=addpre 添加前准备
     * /wardorbe?type=add 添加（第二步）
     * /wardrobe?type=modifypre&id=xx 修改前准备
     * /wardrobe?type=modify        修改
     * /wardrobe?type=sell&id=xx        卖出前准备
     * /wardrobe?type=sell        卖出
     * /wardrobe?type=remove&id=xx    删除
     * /wardrobe?type=query&pageIndex=1 :分页查询(request:转发)
     * /wardrobe?type=query&type_q=xx&pageIndex=1 :分页分类查询(request:转发)
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

        //验证用户是否登录
        HttpSession user = req.getSession();
        if(user.getAttribute("user")==null){
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
                req.getRequestDispatcher("beforeAddClothes.jsp").forward(req, resp);
                break;
            case "addpre":
                String name_a = req.getParameter("name_a");
                if (name_a==null||"".equals(name_a)) {
                    out.println("<script>alert('名称不能为空！'); location.href='wardrobe?type=addtemp&userid="+userid+"';</script>");
                }

                float price = 100;
                String type_a = req.getParameter("type_a");
                if (req.getParameter("price")==null||"".equals(req.getParameter("price"))) {
                    out.println("<script>alert('价格不能为空！'); location.href='wardrobe?type=addtemp&userid="+userid+"';</script>");
                }
                else{
                    price = Float.parseFloat(req.getParameter("price"));
                    req.setAttribute("price", price);
                }
                req.setAttribute("userid", userid);
                req.setAttribute("name_a", name_a);
                req.setAttribute("type_a", type_a);
                if("格裙".equals(type_a)||"纯色裙".equals(type_a)){
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
                    resp.sendError(500, "文件上传失败");
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


                    //存
                    req.setAttribute("pageCount",pageCount);
                    req.setAttribute("cclothes",cclothes);
                    req.setAttribute("type_q",type_q);
                    req.setAttribute("userid",userid);
                    req.setAttribute("useri",useri);

                    //转发到jsp页面
                    req.getRequestDispatcher("wardrobe.jsp?type_q="+type_q+"&pageIndex="+pageIndex+"&userid="+userid).forward(req,resp);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;
            case "remove":
                //1.获取删除的clothesid
                int removeId = Integer.parseInt(req.getParameter("id"));

                //2. 调用biz删除方法
                try {
                    int count = clothesBiz.removeWardrobe(removeId,userid);
                    if (count > 0) {
                        out.println("<script>alert('删除成功'); location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
                    } else {
                        out.println("<script>alert('删除失败'); location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("<script>alert('" + e.getMessage() + "'); location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
                }
                //3. 提示+跳转=>out (查询的servlet)
                break;
            case "searchpre":
                req.setAttribute("userid",userid);//转发到jsp页面
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
                    cclothes = clothesBiz.getWardrobeByKeyAndPage(pageIndex,pageSize,key,userid,type_s);  //查找结果

                    req.setAttribute("userid",userid);//转发到jsp页面
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
                req.setAttribute("userid",userid);//转发到jsp页面
                req.getRequestDispatcher("beforeSell.jsp?userid="+userid).forward(req,resp);
                break;
            default:
                resp.sendError(404);
        }
    }


    /**
     * 修改
     * @param req
     * @param resp
     * @param out
     */
    private void modify(HttpServletRequest req, HttpServletResponse resp, PrintWriter out,int userid) throws Exception {
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
        Clothes clothes  =new Clothes();

        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
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
                //元素名称和用户填写的值  name: trytry
                String name = item.getFieldName();
                String value = item.getString("utf-8");//防止乱码
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
                if("格裙".equals(type)){
                    String color = colora+" "+colorb;
                    clothes.setColor(color);
                }
                else if("水手服".equals(type)){
                    String specs = specsa+"本 "+specsb;

                    clothes.setSpecs(specs);
                }
                else if("毛衣".equals(type)){
                    String specs = specsa+" "+specsb+" "+specsc;

                    clothes.setSpecs(specs);
                }
                else if("西服".equals(type)){
                    String specs = specsa+" "+specsb;

                    clothes.setSpecs(specs);
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
                    clothes.setPic(dbPath);

                    //4.3 保存文件
                    item.write(new File(filePath));
                }

            }

        }
        //5.将信息保存到数据库
        int count = clothesBiz.modifyWardrobe(clothes,userid);
        if (name_m==null) {
            out.println("<script>alert('名称不能为空！'); location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
        }
        if(count>0){
            out.println("<script>alert('修改成功');location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
        }else{
            out.println("<script>alert('修改失败');location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
        }
    }



    /**
     * 添加书籍
     *  1.enctype="multipart/form-data":和以前不同
     *  获取表单元素  req.getParameter("name") : error
     *  2.文件上传 ：图片文件从浏览器端保存到服务器端 (第三方 FileUpload+io)
     *  3.路径
     *    图片:
     *    D:\This Term\Database\pic\trytry.png  :实际路径
     *    http://localhost:8080/IdeaProject_war_exploded/Image/trytry.png:虚拟路径(服务器)
     *
     *  @param req
     * @param resp
     * @param userid
     */
    private void add(HttpServletRequest req, HttpServletResponse resp, PrintWriter  out, int userid) throws Exception {
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
        Clothes clothes  =new Clothes();

        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
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
                //元素名称和用户填写的值  name: trytry
                String name = item.getFieldName();
                String value = item.getString("utf-8");//防止乱码
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
                if("格裙".equals(type)){
                    String color = colora+" "+colorb;
                    clothes.setColor(color);
                }
                else if("水手服".equals(type)){
                    String specs = specsa+"本 "+specsb;

                    clothes.setSpecs(specs);
                }
                else if("毛衣".equals(type)){
                    String specs = specsa+" "+specsb+" "+specsc;

                    clothes.setSpecs(specs);
                }
                else if("西服".equals(type)){
                    String specs = specsa+" "+specsb;

                    clothes.setSpecs(specs);
                }

            }else {
                //文件: 图片的文件名  trytry.png
                String fileName = item.getName();
                //避免文件替换：当前的系统时间.png
                if(fileName.trim().length()>0){
                    //获取后缀名 .png
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
                    clothes.setPic(dbPath);

                    //4.3 保存文件
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
                    out.println("<script>alert('添加成功');location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
                }
                else{
                    out.println("<script>alert('添加失败');location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
                }
            }
            else{
                out.println("<script>alert('添加成功');location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
            }

        }else{
            out.println("<script>alert('添加失败');location.href='wardrobe?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
        }
    }




    public static void main(String[] args) {
        try {
            clothesBiz clothesBiz = new clothesBiz();
            List<Clothes> cclothes = clothesBiz.getWardrobeByPage(1,3,"全部",1);
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
