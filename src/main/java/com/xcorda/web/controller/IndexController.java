package com.xcorda.web.controller;

import com.xcorda.services.config.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	private final SourceService sourceService;

	@Autowired
	public IndexController(SourceService sourceService) {
		this.sourceService = sourceService;
	}

	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("sources", sourceService.getSources());
		return "index";
	}

}
