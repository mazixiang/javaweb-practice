package me.mazixiang.servlet.formdb;

import cn.hutool.core.io.file.FileReader;
import me.mazixiang.dao.student.StudentDao;
import me.mazixiang.dao.student.StudentDaoImpl;
import me.mazixiang.vo.Student;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StudentServlet extends HttpServlet {

    private String dbConfigString;

    @Override
    public void init() throws ServletException {
        ServletContext application = this.getServletContext();
        String configFilePath = this.getServletContext().getRealPath(application.getInitParameter("ConfigFile"));
        FileReader fileReader = new FileReader(configFilePath);
        dbConfigString = fileReader.readString();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Student student = new Student();

        student.setStuId(req.getParameter("stuid"));
        student.setStuName(req.getParameter("stuname"));
        student.setStuPass(req.getParameter("stupass"));
        student.setStuGender(req.getParameter("stugender"));

        student.setStuAge(Integer.parseInt(req.getParameter("stuage")));

        String[] hobbies = req.getParameterValues("stuhobbies");
        StringBuilder hobbiesString = new StringBuilder();
        for (int i = 0; i < hobbies.length; i++) {
            if (i != 0) {
                hobbiesString.append("，");
            }
            hobbiesString.append(hobbies[i]);
        }
        student.setStuHobbies(hobbiesString.toString());
        student.setStuSchool(req.getParameter("stuschool"));


        StudentDao studentDao = new StudentDaoImpl(dbConfigString);

        resp.setHeader("content-type", "text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        try {
            if (student.getStuId() == null ||
                    student.getStuId().length() == 0 ||
                    student.getStuId().trim().length() == 0) {
                studentDao.insert(student);
                resp.getWriter().println("添加成功");
            } else {
                studentDao.update(student);
                resp.getWriter().println("更新成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
