package com.xcorda.web.controller;

import com.xcorda.services.config.Source;
import com.xcorda.services.config.SourceService;
import com.xcorda.services.corda.CordaConnectionService;
import com.xcorda.web.model.AddSourceFormRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
@RequestMapping("/source")
public class SourceController {
    private final SourceService sourceService;
    private final CordaConnectionService cordaConnectionService;

    @Autowired
    public SourceController(SourceService sourceService, CordaConnectionService cordaConnectionService) {
        this.sourceService = sourceService;
        this.cordaConnectionService = cordaConnectionService;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public String addSource (@ModelAttribute AddSourceFormRequest request) throws IOException {

        sourceService.addSource(String.valueOf(sourceService.getSources().size() + 1), new Source(
                request.getName(),
                request.getHostname(),
                request.getPort(),
                request.getUsername(),
                request.getPassword()
        ));

        return "redirect:/";
    }

    @RequestMapping(path = "/{id}/view")
    public String viewSource (@PathVariable("id") String id, Model model) {
        model.addAttribute("source", sourceService.getSource(id));
        model.addAttribute("states", cordaConnectionService.get(id).list());
        return "source";
    }

    @RequestMapping(path = "/{id}/remove", method = RequestMethod.POST)
    public String removeSource (@PathVariable("id") String id, Model model) {
        cordaConnectionService.close(id);
        sourceService.removeSource(id);
        return "redirect:/";
    }
}
