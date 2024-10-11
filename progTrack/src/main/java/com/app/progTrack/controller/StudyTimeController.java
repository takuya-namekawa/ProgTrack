package com.app.progTrack.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
		model.addAttribute("studyTimeObject", new StudyTime());
		return "studytime/studyTimeForm";
	}
	
	@PostMapping
	public String createStudyTime(@ModelAttribute StudyTime studyTime) {
		studyTimeService.saveStudyTime(studyTime);
		return "redirect:/studytime";
	}
	
	@GetMapping("/{id}/edit")
	public String editStudyTimeForm(@PathVariable Long id, Model model) {
		StudyTime studyTime = studyTimeService.getStudyTime(id);
		model.addAttribute("studyTimeObject", studyTime);
		return "studytime/studyTimeForm";
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
