package com.app.progTrack.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.progTrack.entity.Role;
import com.app.progTrack.entity.User;
import com.app.progTrack.form.SignupForm;
import com.app.progTrack.repository.RoleRepository;
import com.app.progTrack.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService (UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	// フォームから送信された情報を基にユーザーをDBに登録する
	// 以下「@Transactional」により、途中で中断された場合、登録情報を保存しない
	@Transactional
	public User createUser(SignupForm signupForm) {
		// アカウント登録するため、登録情報を格納する変数を用意
		User user = new User();
		// ロール名を取得する この場合「MEMBER」という権限名を取得する
		Role role = roleRepository.findByRoleName("MEMBER");
		// signupFormから入力された情報を取得し、その情報を「user」へセットしている
		user.setUserName(signupForm.getName());
		user.setEmail(signupForm.getEmail());
		// 取得したパスワードはハッシュ化してからセットする
		user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
	    // この場合、権限名は「MEMBER」をセット
		user.setRole(role);
		// ユーザーは有効として「true」をセットする
		user.setEnabled(true);
		// 取得しセットした情報をＤＢへ保存する
		return userRepository.save(user);
	}
	
	// メールアドレスが登録済みかをチェックする
	public boolean isEmailChecked(String email) {
		// 引数で受け取ったメールアドレスに一致する「user」を検索する
		User user = userRepository.findByEmail(email);
		// 検索結果：結果がnullでない場合(メールアドレスが登録済み)  == 検索結果が一致する  すなわちtrueを返す
		// 検索結果: 結果が一致しない　つまりDBに同じメールアドレスが存在しないのでfalseを返す
		return user != null;
	}
	
	// パスワードとパスワード(確認用)の入力値を確認する
	public boolean isPasswordValid(String password, String rePassword) {
		// 引数で取得したパスワードとパスワード(確認用)を比較し、合致していればtrueを返す　違うならfalseを返す
		return password.equals(rePassword);
	}
}
