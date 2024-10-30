package com.app.progTrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.progTrack.form.SignupForm;

@Controller
public class AuthenticationController {
	// ログインページ 初期表示
	@GetMapping("/login")
	public String login() {
		return "authentication/login";
	}
	
	// アカウント登録ページ 初期表示
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("signupForm", new SignupForm());
		return "authentication/signup";
	}
}
