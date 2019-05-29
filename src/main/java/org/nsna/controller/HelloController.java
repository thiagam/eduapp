package org.nsna.controller;

import org.nsna.service.ScholarshipOriginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
@Autowired
ScholarshipOriginationService scholarshipOriginationService;

    @RequestMapping("/")
    public String index() {
        return "Welcome to "+ scholarshipOriginationService.getScholarshipOriginationLabel()  +" Edu App";
    }
 

}