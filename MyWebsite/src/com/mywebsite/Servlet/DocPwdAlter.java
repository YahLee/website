package com.mywebsite.Servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mywebsite.Dao.LoginDao;
import com.mywebsite.ValidationCode.Encrypter;
import com.mywebsite.bean.DocInfo;

@WebServlet("/DocPwdAlter")
public class DocPwdAlter extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public DocPwdAlter() {
		super();
	}
	protected void service(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		HttpSession session=request.getSession();
		String oldpwd=request.getParameter("oldpwd");
		String newpwd=request.getParameter("newpwd");
		String againpwd=request.getParameter("againpwd");
		LoginDao userLoginDao=new LoginDao();
		String dusername=(String)session.getAttribute("dusername");
		DocInfo docinfo=userLoginDao.Querydocinfo(dusername);
		try {
		  if(againpwd.equals(newpwd)){
			String passwordMD5 = Encrypter.md5Encrypt(oldpwd);
			if(!docinfo.getDpassword().equals(passwordMD5))
				request.setAttribute("info", "原密码错误！");
			else {
				String newpwdMD5 = Encrypter.md5Encrypt(newpwd);//新密码进行MD5加密
				LoginDao loginDao=new LoginDao();
				loginDao.ChangeDocPwd(newpwdMD5,dusername);
				request.setAttribute("info", "密码修改成功!");//  定义result.jsp中使用的消息
			}
		  }
		  else
				request.setAttribute("info", "新密码不一致！");
			
		} catch (Exception e){ 
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
			finally		
			{		
				request.setAttribute("page","UserPwdAlter.jsp");
				 RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/front/result.jsp");
				rd.forward(request, response);
			}
		}

	

}
