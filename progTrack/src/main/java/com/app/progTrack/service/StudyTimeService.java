package com.app.progTrack.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
	
	// 学習時間の打刻セット
	public void saveStudyTime(StudyTime studyTime) {

		studyTimeRepository.save(studyTime);
	}
	
	// 最後のレコードを取得する
	public StudyTime getLastStudyTime() {
		List<StudyTime> studyTimes = studyTimeRepository.findAll();
		return studyTimes.isEmpty() ? null : studyTimes.get(studyTimes.size() - 1);
	}
	
	// UTCからJSTへ変換する処理
	public LocalDateTime convertToJST(LocalDateTime utcDateTime) {
		// LocalDateTimeをUTCのZoneTimeに変換
		ZonedDateTime utcZoned = utcDateTime.atZone(ZoneId.of("UTC"));
		// UTCからJSTへ同じ瞬間の時間を取得
		ZonedDateTime jstZonde = utcZoned.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
		// LocalDateTimeに変換する
		return jstZonde.toLocalDateTime();
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
