package com.app.progTrack.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "user_name")
	private String userName;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "enabled")
	private Boolean enabled;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	
	@OneToMany
	@JoinColumn(name = "study_times_id")
	private StudyTime studyTime;
	
	@Column(name = "created_at", insertable = false, updatable = false)
	private Timestamp createdAt;
	
	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updatedAt;

}
