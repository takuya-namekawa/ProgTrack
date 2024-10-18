package com.app.progTrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.progTrack.entity.StudyTime;

public interface StudyTimeRepository extends JpaRepository<StudyTime, Long>{
	 
	 // 学習時間を計算する
	 @Query("SELECT SUM(TIMESTAMPDIFF(MINUTE, s.startTime, s.endTime)) FROM StudyTime s WHERE MONTH(s.startTime) = :month AND YEAR(s.startTime) = :year AND s.endTime IS NOT NULL")
	 Long findTotalStudyTimeByMonth(@Param("month") int month, @Param("year") int year);
	 
	 // 当月を絞り込む
	 @Query("SELECT s FROM StudyTime s WHERE MONTH(s.startTime) = :month AND YEAR(s.startTime) = :year")
	 List<StudyTime> findAllByMonthAndYear(@Param("month") int month, @Param("year") int year);
}
