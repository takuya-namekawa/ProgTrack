package com.app.progTrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.progTrack.form.SignupForm;
import com.app.progTrack.service.UserService;

@Controller
public class AuthenticationController {
	private final UserService userService;
	
	public AuthenticationController (UserService userService) {
		this.userService = userService;
	}
	
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
	
	// アカウント登録の処理
	@PostMapping("/signup")
	public String signup(@ModelAttribute @Validated SignupForm signupForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
		
		// メールアドレスが重複している場合
		if (userService.isEmailChecked(signupForm.getEmail())) {
			// 新規アカウント登録画面で発生したフォームのエラーをオブジェクト化し、bindingResultに格納している
			FieldError  fieldError = new FieldError(bindingResult.getObjectName(), "email", "そのメールアドレスは登録済みされています");
			bindingResult.addError(fieldError);
		}
		
		// パスワードとパスワード(確認用)の一致しない場合
		if (! userService.isPasswordValid(signupForm.getPassword(), signupForm.getRePassword())) {
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "入力されたパスワードが一致しません");
			bindingResult.addError(fieldError);
		}
		
		// フィールドに設定したバリデーションルールを確認する
		if (bindingResult.hasErrors()) {
			// エラーの場合は、もう一度アカウント登録画面を表示しないといけないので、modelにaddし画面に返す
			model.addAttribute("signupForm", signupForm);
			return "authentication/signup";
		}
		
		// 登録の処理
		userService.createUser(signupForm);
		redirectAttributes.addFlashAttribute("successMessage", "新規アカウントの登録が完了しました");
		
		return "redirect:/login";
	}
}
