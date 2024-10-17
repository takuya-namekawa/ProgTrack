package com.app.progTrack.controller;

import java.time.LocalDate;
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
		
		// 現在の月と前月の累計時間を取得する
		int currentMonth = LocalDate.now().getMonthValue();
		int currentYear = LocalDate.now().getYear();
		// 当月の学習時間を算出
		double _currentMonthTotal = studyTimeService.getTotalStudyTimeForMonth(currentMonth, currentYear) / 60.0;
		
		// 現在の月 - 前月 = 0 なら　12を返す　0でないなら-1を引いた数字を返す
		int lastMonth = currentMonth - 1 == 0 ? 12 : currentMonth -1;
		int lastYear = currentMonth - 1 == 0 ? currentYear - 1 : currentYear;
		// 前月の学習時間を算出
		double _lastMonthTotal = studyTimeService.getTotalStudyTimeForMonth(lastMonth,  lastYear) / 60.0;
		
		// 小数点第二位を切り上げてから小数点第一位まで表示する
		String currentMonthTotal = String.format("%.1f", Math.ceil(_currentMonthTotal * 10) / 10);
		String lastMonthTotal = String.format("%.1f", Math.ceil(_lastMonthTotal * 10) / 10);
		
		model.addAttribute("currentMonthTotal", currentMonthTotal);
		model.addAttribute("lastMonthTotal", lastMonthTotal);
		
		return "studytime/studyTimeList";
	}
	
	@GetMapping("/new")
	public String newStudyTimeForm(Model model) {
		
		// ベースとなる日付を取得する　この場合はJST取得の日付 現在の時刻取得用
		LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
		// どういう型で用意するかを定義
		DateTimeFormatter dateFormateer = DateTimeFormatter.ofPattern("MM/dd"); // 日付で形成する
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // 時間で形成する
		// 現在の時刻にあてはめる
		String date = currentDateTime.format(dateFormateer);
		String time = currentDateTime.format(timeFormatter);

		model.addAttribute("studyTimeObject", new StudyTime());
		model.addAttribute("date", date);
		model.addAttribute("time", time); //ページが読み込まれた時の初期表示の時刻表示用
		// 曜日を取得する
		model.addAttribute("currentDayOfWeek", currentDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.JAPAN));
		// 学習中フラグの状態を追加する
		model.addAttribute("isLearningActive", studyTimeService.isLiarningActive());
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
			studyTime.setIsLearning(true);
			studyTimeService.saveStudyTime(studyTime);
		} else if ("end".equals(action)) {
			// 直近の学習セッションを取得する
			StudyTime lastStudyTime = studyTimeService.getLastStudyTime();
			
			if (lastStudyTime != null) {
				lastStudyTime.setEndTime(studyTimeService.convertToJST(LocalDateTime.now(ZoneId.of("UTC"))));
				lastStudyTime.setIsLearning(false);
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
