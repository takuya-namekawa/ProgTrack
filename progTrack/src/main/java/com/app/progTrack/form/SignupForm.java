package com.app.progTrack.form;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class SignupForm {
	
	@NotBlank(message = "ユーザー名を入力してください")
	private String name;
	
	@NotBlank(message = "メールアドレスを入力してください")
	@Email(message = "メールアドレスは正しい形式で入力してください")
	private String email;
	
	@NotBlank(message = "パスワードを入力してください")
	@Length(min = 6, message = "パスワードは6文字以上で入力してください")
	private String password;
	
	@NotBlank(message = "パスワード(確認用)を入力してください")
	private String rePassword;
}
