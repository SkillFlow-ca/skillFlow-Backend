package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.service.ModuleIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/module/")
@Validated
public class ModuleController {

    @Autowired
    private ModuleIService moduleIService;
}
