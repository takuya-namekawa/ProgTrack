package com.app.progTrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.progTrack.entity.StudyTime;

public interface StudyTimeRepository extends JpaRepository<StudyTime, Long>{
	 
	 @Query("SELECT SUM(TIMESTAMPDIFF(MINUTE, s.startTime, s.endTime)) FROM StudyTime s WHERE MONTH(s.startTime) = :month AND YEAR(s.startTime) = :year AND s.endTime IS NOT NULL")
	 Long findTotalStudyTimeByMonth(@Param("month") int month, @Param("year") int year);
	 
}
