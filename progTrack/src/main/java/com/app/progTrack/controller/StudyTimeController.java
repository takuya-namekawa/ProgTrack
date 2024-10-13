package com.app.progTrack.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.progTrack.entity.StudyTime;
import com.app.progTrack.service.StudyTimeService;

@Controller
@RequestMapping("/studytime")
public class StudyTimeController {
	private final StudyTimeService studyTimeService;
	
	public StudyTimeController(StudyTimeService studyTimeService) {
		this.studyTimeService = studyTimeService;
	}
	
	@GetMapping
	public String listStudyTimes(Model model) {
		List<StudyTime> studyTimes = studyTimeService.getAllStudyTimes();
		model.addAttribute("studyTimes", studyTimes);
		return "studytime/studyTimeList";
	}
	
	@GetMapping("/new")
	public String newStudyTimeForm(Model model) {
		
		// ベースとなる日付を取得する　この場合はJST取得の日付 現在の時刻取得用
		LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
		// どういう型で用意するかを定義
		DateTimeFormatter dateFormateer = DateTimeFormatter.ofPattern("MM/dd"); // 日付で形成する
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		// 現在の時刻にあてはめる
		String date = currentDateTime.format(dateFormateer);
		String time = currentDateTime.format(timeFormatter);

		model.addAttribute("studyTimeObject", new StudyTime());
		model.addAttribute("date", date);
		model.addAttribute("time", time); //ページが読み込まれた時の初期表示の時刻表示用
		// 曜日を取得する
		model.addAttribute("currentDayOfWeek", currentDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.JAPAN));
		return "studytime/studyTimeForm";
	}
	
	// 時刻を返すエンドポイント
	@GetMapping("/current-time")
	@ResponseBody
	public String getCurrentTime() {
		LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		return currentDateTime.format(formatter);
	}
	
	@PostMapping
	public String createStudyTime(@ModelAttribute StudyTime studyTime, @RequestParam String action) {
		
		if ("start".equals(action)) {
			studyTime.setStartTime(studyTimeService.convertToJST(LocalDateTime.now(ZoneId.of("UTC"))));
			studyTime.setEndTime(null);
			studyTimeService.saveStudyTime(studyTime);
		} else if ("end".equals(action)) {
			// 直近の学習セッションを取得する
			StudyTime lastStudyTime = studyTimeService.getLastStudyTime();
			
			if (lastStudyTime != null) {
				lastStudyTime.setEndTime(studyTimeService.convertToJST(LocalDateTime.now(ZoneId.of("UTC"))));
				studyTimeService.saveStudyTime(lastStudyTime);
			}
		}
		
		return "redirect:/studytime";
	}
	
	@GetMapping("/{id}/edit")
	public String editStudyTimeForm(@PathVariable Long id, Model model) {
		StudyTime studyTime = studyTimeService.getStudyTime(id);
		model.addAttribute("studyTimeObject", studyTime);
		return "studytime/edit";
	}
	
	@PostMapping("/{id}")
	public String updateStudyTime(@PathVariable Long id, @ModelAttribute StudyTime studyTime) {
		studyTime.setStudyTimesId(id);
		studyTimeService.saveStudyTime(studyTime);
		return "redirect:/studytime";
	}
	
	@PostMapping("/{id}/delete")
	public String deleteStudyTime(@PathVariable Long id) {
		studyTimeService.deleteStudyTime(id);
		return "redirect:/studytime";
	}
}
