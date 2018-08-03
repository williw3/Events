package com.leewatterson.events.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.leewatterson.events.models.Event;
import com.leewatterson.events.models.User;
import com.leewatterson.events.services.EventService;
import com.leewatterson.events.services.UserService;
import com.leewatterson.events.validator.UserValidator;

@Controller
public class UserController {
	private final UserService userService;
    private final UserValidator userValidator;
    private final EventService eventService;
    public UserController(UserService userService, UserValidator userValidator, EventService eventService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.eventService = eventService;
    }
    
    @RequestMapping("/")
	public String registerForm(@ModelAttribute("user") User user, Model model) {
    	List<String> locations = Arrays.asList("CA", "WA", "TX", "DC", "OK", "IL");
    	model.addAttribute("locations", locations);
		return "/users/logReg.jsp";
	}
	
	@RequestMapping(value="/registration", method=RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model, HttpSession session) {
		userValidator.validate(user, result);
		if(result.hasErrors()) {
			System.out.println("Errors Found");
			List<String> locations = Arrays.asList("CA", "WA", "TX", "DC", "OK", "IL");
	    	model.addAttribute("locations", locations);
			return "/users/logReg.jsp";
		}
		else {
			User u = userService.registerUser(user);
			System.out.println("New USER Created: " + user.getEmail());
			session.setAttribute("userId", u.getId());
			return "redirect:/home";
		}
    }
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
    public String loginUser(@ModelAttribute("user") User user, BindingResult errors, @RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session) {
		boolean verify = userService.authenticateUser(email, password);
		if(!verify) {
			model.addAttribute("error", "Invalid Credentials. Try again");
			List<String> locations = Arrays.asList("CA", "WA", "TX", "DC", "OK", "IL");
	    	model.addAttribute("locations", locations);
			return "/users/logReg.jsp";
		}
		else {
			User getUser = userService.findByEmail(email);
			session.setAttribute("userId", getUser.getId());
			return "redirect:/home";
		}
    }
	
	@RequestMapping("/home")
    public String home(@ModelAttribute("event") Event event, HttpSession session, Model model) {
		Long id = (Long) session.getAttribute("userId");
		if(id == null) {
			return "redirect:/";
		}
		else {
			User user = userService.findById(id);
			model.addAttribute("user", user);
			List<String> locations = Arrays.asList("CA", "WA", "TX", "DC", "OK", "IL");
	    	model.addAttribute("locations", locations);
	    	String state = user.getState();
	    	List<Event> local = eventService.findAllByState(state);
	    	model.addAttribute("local", local);
	    	List<Event> events = eventService.findAllByOther(state);
	    	model.addAttribute("events", events);
	    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	Calendar today = Calendar.getInstance();
	    	today.setTime(new Date());
	    	today.add(Calendar.HOUR_OF_DAY, 24);
			String dateStr = df.format(today.getTime());
	    	model.addAttribute("dateStr", dateStr);
			return "users/homePage.jsp";
		}
    }
	
	@RequestMapping("/logout")
    public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
    }
}
