package com.app.progTrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.progTrack.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	// Roleエンティティのフィールド「roleName」を検索する
	public Role findByRoleName(String roleName);
}
