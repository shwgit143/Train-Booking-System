package com.booking.app.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.booking.app.config.Constants;
import com.booking.app.dto.User;
import com.booking.app.service.UserService;
import com.booking.app.util.AuthenticationRequest;

@Controller
public class AuthenticationController {

	@Autowired
	private UserService userService;

	@RequestMapping("/displaySignInPage")
	String signInPageMethod() {

		return "signInPage";
	}

	@RequestMapping(value = "/signIn", method = RequestMethod.POST)
	String signIn(AuthenticationRequest request, HttpServletResponse resp, HttpSession httpSession) {
		// System.out.println("login info " + request);

		User userFromDB = userService.findUserByEmailOrMobileNoOrUserName(request.getLoginCredentials());

		System.out.println("userFromDB -> " + userFromDB);
		// logic for Authentication
		if (userFromDB != null && !request.getPassword().equals(userFromDB.getPassword())) {
			return "signInPage";
		}

		Cookie userNameCookie = new Cookie("user", request.getLoginCredentials());
		userNameCookie.setMaxAge(24 * 60 * 60);
		resp.addCookie(userNameCookie);

		// before adding password into cookie
		Cookie passwordCookie = new Cookie("password", request.getPassword());
		passwordCookie.setMaxAge(24 * 60 * 60);
		resp.addCookie(passwordCookie);

		httpSession.setAttribute("userData", userFromDB);

		// logic for Authorization
		if (userFromDB != null && userFromDB.getRole().equals(Constants.ROLE_ADMIN)) {
			return "adminDashboardPage";
		} else if (userFromDB != null && userFromDB.getRole().equals(Constants.ROLE_USER)) {
			return "userDashboardPage";
		}
		return "signInPage";
	}

	@RequestMapping("/logoutPage")
	public String logoutPage()
	{
		return "logoutPage";
	}
	
	@RequestMapping("/logout/{value}")
	public String logout(@PathVariable String value,SessionStatus sessionStatus,HttpServletResponse resp,HttpSession httpSession){
		
		if("yes".equalsIgnoreCase(value))
		{
			System.out.println("....");
			httpSession .removeAttribute("userData");
			Cookie userNameCookie=new Cookie("user","");
			userNameCookie.setMaxAge(0);
			userNameCookie.setPath("/TrainBooking");
			resp.addCookie(userNameCookie);
			Cookie passwordCookie=new Cookie("password","");
			passwordCookie.setMaxAge(0);
			passwordCookie.setPath("/TrainBooking");
			resp.addCookie(passwordCookie);
			//sessionStatus.setComplete();
			//httpSession object should
			
			//delete cookies
			/*Cookie userNameCookie = new Cookie("user","");
			userNameCookie.setMaxAge(0);
			resp.addCookie(userNameCookie);

			// before adding password into cookie
			Cookie passwordCookie = new Cookie("password","");
			passwordCookie.setMaxAge(1);
			resp.addCookie(passwordCookie);
		}*/
		}
		else
		{
	return "redirect: /TrainBooking";
		}
		return "redirect: /TrainBooking";	
}
}
