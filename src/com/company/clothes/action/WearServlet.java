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
            case "addpre":
                req.setAttribute("userid", userid);
                req.getRequestDispatcher("beforeAddWear.jsp?userid="+userid).forward(req, resp);
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

                    //存
                    req.setAttribute("pageCount",pageCount);
                    req.setAttribute("wears",wears);
                    req.setAttribute("userid",userid);
                    req.setAttribute("useri",useri);

                    //转发到jsp页面
                    req.getRequestDispatcher("wear.jsp?&pageIndex="+pageIndex+"&userid="+userid).forward(req,resp);
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
                    int count = wearBiz.removeWear(removeId,userid);
                    if (count > 0) {
                        out.println("<script>alert('删除成功'); location.href='wear?type=query&pageIndex=1&userid="+userid+"';</script>");
                    } else {
                        out.println("<script>alert('删除失败'); location.href='wear?type=query&pageIndex=1&userid="+userid+"';</script>");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("<script>alert('" + e.getMessage() + "'); location.href='wear?type=query&pageIndex=1&userid="+userid+"';</script>");
                }
                //3. 提示+跳转=>out (查询的servlet)
                break;
            default:
                resp.sendError(404);
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
        Wear wear  =new Wear();

        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        String time = sdf.format(date);
        wear.setCreatetime(time);
        wear.setUpdatetime(time);
        String title=null;

        for(FileItem  item: fileItems){
            if(item.isFormField()){
                //元素名称和用户填写的值  name: trytry
                String name = item.getFieldName();
                String value = item.getString("utf-8");//防止乱码
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
                    wear.setPic(dbPath);

                    //4.3 保存文件
                    item.write(new File(filePath));
                }
            }
        }
        int count = wearBiz.addWear(wear, userid);
        if(title==null){
            out.println("<script>alert('标题不能为空！');location.href='wear?type=addtemp&userid="+userid+"';</script>");
        }
        if(count>0){
            out.println("<script>alert('添加成功');location.href='wear?type=query&pageIndex=1&userid="+userid+"';</script>");
        }else{
            out.println("<script>alert('添加失败');location.href='wear?type=addtemp&userid="+userid+"';</script>");
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
