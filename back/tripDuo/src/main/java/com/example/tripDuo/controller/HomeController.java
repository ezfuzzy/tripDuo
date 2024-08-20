package com.example.tripDuo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	@ResponseBody
	@GetMapping("/")
	public String index() {
		return "welcome index";
	}
	
	@ResponseBody
	@GetMapping("/home")
	public String home() {
		return "welcome home";
	}
	
	@ResponseBody
	@GetMapping("/main")
	public String main() {
		return "welcome main";
	}
}
