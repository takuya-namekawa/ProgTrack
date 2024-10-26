package com.app.progTrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.progTrack.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	// ログイン時のパスワード取得用
	public User findByEmail(String email);
}
