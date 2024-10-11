package com.app.progTrack.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.app.progTrack.entity.StudyTime;
import com.app.progTrack.repository.StudyTimeRepository;

@Service
public class StudyTimeService {
	private final StudyTimeRepository studyTimeRepository;
	
	public StudyTimeService(StudyTimeRepository studyTimeRepository) {
		this.studyTimeRepository = studyTimeRepository;
	}
	
	// 学習時間の打刻セットロジック
	public void saveStudyTime(StudyTime studyTime) {

		if (studyTime.getStartTime() == null) {
			studyTime.setStartTime(LocalDateTime.now());
		}
		
		if (studyTime.getEndTime() == null) {
			studyTime.setEndTime(LocalDateTime.now());
		}
		
		studyTimeRepository.save(studyTime);
	}
	// 一覧ページ表示用
	public List<StudyTime> getAllStudyTimes() {
		return studyTimeRepository.findAll();
	}
	// 編集用
	public StudyTime getStudyTime(Long id) {
		return studyTimeRepository.findById(id).orElse(null);
	}
	// 削除用
	public void deleteStudyTime(Long id) {
		studyTimeRepository.deleteById(id);
	}
}
