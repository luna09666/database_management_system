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
     * /grass?type=addpre 添加前准备
     * /wardorbe?type=add 添加
     * /grass?type=modifypre&id=xx 修改前准备
     * /grass?type=modify        修改
     * /grass?type=buy        买入
     * /grass?type=buypre        买入前准备
     * /grass?type=payfront        付定金
     * /grass?type=remove&id=xx    删除
     * /grass?type=query&type_q=xx&pageIndex=1 :分页分类查询(request:转发)
     * /grass?type=doajax&name=xx  :使用ajax查询图书名对应的图书信息
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
                req.getRequestDispatcher("beforeAddGrass.jsp").forward(req, resp);
                break;
            case "addpre":
                String name_a = req.getParameter("name_a");
                if (name_a==null) {
                    out.println("<script>alert('名称不能为空！'); location.href='grass?type=addtemp&userid="+userid+"';</script>");
                }
                String type_a = req.getParameter("type_a");
                String howtobuy =req.getParameter("howtobuy");
                req.setAttribute("userid", userid);
                req.setAttribute("name_a", name_a);
                req.setAttribute("type_a", type_a);
                req.setAttribute("howtobuy", howtobuy);
                if("格裙".equals(type_a)||"纯色裙".equals(type_a)){
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
                    resp.sendError(500, "文件上传失败");
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
                if(!("现货".equals(grass.getFlag())||"已付定金".equals(grass.getFlag().split(" ")[1]))){
                    out.println("<script>alert('预售商品未付定金，不可购买！'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
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
                        out.println("<script>alert('付定金成功！'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
                    } else {
                        String flag = grass.getFlag();
                        if("预售".equals(flag.split(" ")[0])){
                            out.println("<script>alert('已付定金！'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
                        }
                        else{
                            out.println("<script>alert('是现货，不可付定金！'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
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
                //1.获取删除的clothesid
                int removeId = Integer.parseInt(req.getParameter("id"));

                //2. 调用biz删除方法
                try {
                    int count = grassBiz.removeGrass(removeId,userid);
                    if (count > 0) {
                        out.println("<script>alert('删除成功'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
                    } else {
                        out.println("<script>alert('删除失败'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("<script>alert('" + e.getMessage() + "'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
                }
                //3. 提示+跳转=>out (查询的servlet)
                break;
            case "searchpre":
                req.setAttribute("userid",userid);//转发到jsp页面
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
                    grasses = grassBiz.getGrassByKeyAndPage(pageIndex,pageSize,key,userid,type_s);  //查找结果

                    req.setAttribute("userid",userid);//转发到jsp页面
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
     * 按页查询
     * grass?type=query&pageIndex=1
     * 页数: biz
     * 当前页码:pageIndex = 1
     * 存:request,转发
     * @param req
     * @param resp
     * @throws Exception 
     */
    private void queryGrass(HttpServletRequest req, HttpServletResponse resp, PrintWriter out,int userid) throws Exception {
        //获取信息(页数，页码,信息)
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

        //存
        req.setAttribute("pageCount",pageCount);
        req.setAttribute("grasses",grasses);
        req.setAttribute("type_q_g",type_q_g);
        req.setAttribute("type_q",type_q);
        req.setAttribute("userid",userid);
        req.setAttribute("useri",useri);

        //转发到jsp页面
        req.getRequestDispatcher("grass.jsp?pageIndex="+pageIndex+"&type_q="+type_q+"&type_q_g="+type_q_g+"&userid="+userid).forward(req,resp);
    }


    /**
     * 修改
     * @param req
     * @param resp
     * @param out
     */
    private void modifyGrass(HttpServletRequest req, HttpServletResponse resp, PrintWriter out,int userid) throws Exception {
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
        Grass grass  =new Grass();

        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
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
                //元素名称和用户填写的值  name: trytry
                String name = item.getFieldName();
                String value = item.getString("utf-8");//防止乱码
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
                if("现货".equals(howtobuy)){
                    grass.setFlag(howtobuy);
                    grass.setFrontprice((float) 0);
                    grass.setTailprice((float) 0);
                    grass.setBuytime(null);
                }
                else{
                    grass.setFlag(howtobuy+" "+flag);
                    grass.setPrice(Float.parseFloat(frontprice)+Float.parseFloat(tailprice));
                }
                if("格裙".equals(type)){
                    String color = colora+" "+colorb;
                    grass.setColor(color);
                }
                else if("水手服".equals(type)){
                    String specs = specsa+"本 "+specsb;

                    grass.setSpecs(specs);
                }
                else if("毛衣".equals(type)){
                    String specs = specsa+" "+specsb+" "+specsc;

                    grass.setSpecs(specs);
                }
                else if("西服".equals(type)){
                    String specs = specsa+" "+specsb;

                    grass.setSpecs(specs);
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
                    grass.setPic(dbPath);

                    //4.3 保存文件
                    item.write(new File(filePath));
                }

            }

        }
        //5.将信息保存到数据库
        if (name_m==null) {
            out.println("<script>alert('名称不能为空！'); location.href='grass?type=query&pageIndex=1&type_q=all&userid="+userid+"';</script>");
        }
        int count = grassBiz.modifyGrass(grass,userid);
        if(count>0){
            out.println("<script>alert('修改成功');location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
        }else{
            out.println("<script>alert('修改失败');location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
        }
    }



    /**
     * 添加
     *  1.enctype="multipart/form-data":和以前不同
     *  获取表单元素  req.getParameter("name") : error
     *  2.文件上传 ：图片文件从浏览器端保存到服务器端 (第三方 FileUpload+io)
     *  3.路径
     *    图片:
     *    D:\This Term\Database\pic\trytry.png  :实际路径
     *    http://localhost:8080/IdeaProject_war_exploded/Image/trytry.png:虚拟路径(服务器)
     *
     *
     * @param req
     * @param resp
     */
    private void addGrass(HttpServletRequest req, HttpServletResponse resp,  PrintWriter  out,int userid) throws Exception {
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
        ServletFileUpload fileUpload = new ServletFileUpload(factory);

        //将请求解析成一个个FileItem(文件+表单元素)
        List<FileItem> fileItems = fileUpload.parseRequest(req);

        //遍历FileItem
        Grass grass  =new Grass();

        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
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

        String flag = "现货";

        for(FileItem  item: fileItems){
            if(item.isFormField()){
                //元素名称和用户填写的值  name: trytry
                String name = item.getFieldName();
                String value = item.getString("utf-8");//防止乱码
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
                if("现货".equals(howtobuy)){
                    grass.setFlag(howtobuy);
                    grass.setFrontprice((float) 0);
                    grass.setTailprice((float) 0);
                    grass.setBuytime(null);
                }
                else{
                    grass.setFlag(howtobuy+" "+flag);
                    grass.setPrice(Float.parseFloat(frontprice)+Float.parseFloat(tailprice));
                }
                if("格裙".equals(type)){
                    String color = colora+" "+colorb;
                    grass.setColor(color);
                }
                else if("水手服".equals(type)){
                    String specs = specsa+"本 "+specsb;

                    grass.setSpecs(specs);
                }
                else if("毛衣".equals(type)){
                    String specs = specsa+" "+specsb+" "+specsc;

                    grass.setSpecs(specs);
                }
                else if("西服".equals(type)){
                    String specs = specsa+" "+specsb;

                    grass.setSpecs(specs);
                }

            }else {
                //文件: 图片的文件名  trytry.png
                String fileName = item.getName();
                //避免文件替换：当前的系统时间.png
                //获取后缀名 .png
                if(fileName.trim().length()>0){
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
                    grass.setPic(dbPath);

                    //4.3 保存文件
                    item.write(new File(filePath));
                }
            }

        }
        int count = grassBiz.addGrass(grass,userid);
        if(count>0){
            out.println("<script>alert('添加成功');location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
        }else{
            out.println("<script>alert('添加失败');location.href='beforeAddGrass.jsp?userid="+userid+"';</script>");
        }
    }

    private void buyGrass(HttpServletRequest req, HttpServletResponse resp,  PrintWriter  out,int userid) throws Exception {
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
        ServletFileUpload fileUpload = new ServletFileUpload(factory);

        //将请求解析成一个个FileItem(文件+表单元素)
        List<FileItem> fileItems = fileUpload.parseRequest(req);

        //遍历FileItem
        Grass grass  =new Grass();

        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
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
                //元素名称和用户填写的值  name: trytry
                String name = item.getFieldName();
                String value = item.getString("utf-8");//防止乱码
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
                if("格裙".equals(type)){
                    String color = colora+" "+colorb;
                    grass.setColor(color);
                }
                else if("水手服".equals(type)){
                    String specs = specsa+"本 "+specsb;

                    grass.setSpecs(specs);
                }
                else if("毛衣".equals(type)){
                    String specs = specsa+" "+specsb+" "+specsc;

                    grass.setSpecs(specs);
                }
                else if("西服".equals(type)){
                    String specs = specsa+" "+specsb;

                    grass.setSpecs(specs);
                }

            }else {
                //文件: 图片的文件名  trytry.png
                String fileName = item.getName();
                //避免文件替换：当前的系统时间.png
                //获取后缀名 .png
                if(fileName.trim().length()>0){
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
                    grass.setPic(dbPath);

                    //4.3 保存文件
                    item.write(new File(filePath));
                }
            }

        }
        int count = grassBiz.buyGrass(grass,userid);
        if (count > 0) {
            out.println("<script>alert('购买成功！'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
            grassBiz.removeGrass(Integer.parseInt(id),userid);
        } else {
            out.println("<script>alert('购买失败'); location.href='grass?type=query&pageIndex=1&type_q=all&type_q_g=all&userid="+userid+"';</script>");
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
