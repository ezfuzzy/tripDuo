package com.example.tripDuo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	
	/**
	 * 여기 test 용도 컨트롤러 입니다
	 *  !!! 절대 참고 금지 !!!  
	 */
	
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
	
	@GetMapping("/tripDuo")
	public String passTest() {
		return "";
	}
	
	String phone = "";
	String v_code = "";
	
	@ResponseBody
	@PostMapping("/api/v1/auth/sms")
	public String sms(@RequestBody String phoneNum) {
		System.out.println(phoneNum);
		phone = phoneNum.substring(0, phoneNum.length()-1);
		v_code = "1313";
		return phone + " is right? code : " + v_code;
	}
	
	@ResponseBody
	@PostMapping("/api/v1/auth/sms/verify")
	public String smsVerify(@RequestBody String str) {
		String phoneNum = str.substring(0, 11);
		String code = str.substring(11, str.length()-1);
		
		System.out.println("phoneNUm : " + phoneNum);
		System.out.println("code : " + code);
		
		System.out.println(phone + " ::: " + v_code);
		
		if(phone.equals(phoneNum) && v_code.equals(code)) {
			return "true";
		}
		return "false";
	}
}
