package com.booking.app.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.booking.app.config.Constants;
import com.booking.app.dto.User;
import com.booking.app.service.UserService;

@Controller
public class AppController {

	
	@Autowired 
	private UserService userService;
	@RequestMapping("/")
	String entryPoint(HttpServletRequest request,HttpServletResponse resp){
		
		System.out.println("Ëntry point of project");
		
		  String userCred="";
		  String password ="";
		
		HttpSession httpSession=request.getSession();
		User userFromSession=(User)httpSession.getAttribute("userData");
		System.out.println(userFromSession);
		if(userFromSession!=null)
		{
			if(userFromSession != null && userFromSession.getRole().equals(Constants.ROLE_ADMIN)) {
				return "adminDashboardPage";
			}
			else if(userFromSession != null &&userFromSession.getRole().equals(Constants.ROLE_USER)) {
				return "userDashboardPage";
			}
		}
		else
		{
		//ServletContext servletContext=request.getServletContext();
		Cookie[] cookies=request.getCookies();
		if(cookies.length > 0) {
			for (Cookie cookie : cookies) {
				
				if(cookie.getName().equals("user")) {
				userCred= cookie.getValue();
			}
			else if (cookie.getName().equals("password"))
			{
				password =cookie.getValue();
			}
			}

		}	   //get object from the data base and check for the authorization 
		
		System.out.println(userCred+".."+password);
	/*	String name=cookies[0].getName();
		System.out.println(name);
		if(name!=null)
			return "userDashboardPage";  */
		

		if(userCred.length()>0 && password.length()>0) {
			System.out.println("inside the of db");
			User userFromDB =  userService.findUserByEmailOrMobileNoOrUserName(userCred);
			httpSession.setAttribute("userData",userFromDB);
			
			if(userFromDB != null && userFromDB.getRole().equals(Constants.ROLE_ADMIN)) {
				return "adminDashboardPage";
			}
			else if(userFromDB != null &&userFromDB.getRole().equals(Constants.ROLE_USER)) {
				return "userDashboardPage";
			}
		}
		
	}	
		
		
		if(userFromSession!=null)
		{
			if(userFromSession != null && userFromSession.getRole().equals(Constants.ROLE_ADMIN)) {
				return "adminDashboardPage";
			}
			else if(userFromSession != null && userFromSession.getRole().equals(Constants.ROLE_USER)) {
				return "userDashboardPage";
			}
		}
			else 
			{
				return "index";
				
			}
	
		//System.out.println("Entry point of Project");
	return "index";
		
	}
	
	}

