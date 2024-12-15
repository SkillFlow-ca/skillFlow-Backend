package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Blog;
import com.skillflow.skillflowbackend.model.Question;
import com.skillflow.skillflowbackend.model.enume.QuestionStatus;
import com.skillflow.skillflowbackend.model.enume.StatusBlog;
import com.skillflow.skillflowbackend.service.QuestionIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/question/")
public class QuestionController {
    @Autowired
    private QuestionIService questionIService;

    @PostMapping("add")
    public Question addQuestion(@RequestBody Question question) {
        return questionIService.addQuestion(question);
    }
    @GetMapping("/myQuestions")
    public ResponseModel<Question> myQuestions(@RequestParam(required = false,defaultValue="1")int pageNo,
                                               @RequestParam(required = false,defaultValue="10")int size) {
        Pageable pageRequestData = PageRequest.of(pageNo - 1, size);
        ResponseModel<Question> questions= questionIService.myQuestions(pageRequestData);
        return questions;
    }
    @PutMapping("update")
    public Question updateQuestion(@RequestParam long idQuestion, @RequestBody Question question) {
        return questionIService.updateQuestion(question, idQuestion);
    }
    @PutMapping("delete")
    public Question deleteQuestion(@RequestParam long idQuestion) {
        return questionIService.deleteQuestion(idQuestion);
    }
    @GetMapping("getQuestionById")
    public Question getQuestionById(@RequestParam long idQuestion) {
        return questionIService.getQuestionById(idQuestion);
    }
    @GetMapping("getQuestionByContraintes")
    public ResponseModel<Question> getQuestionByContraintes(@RequestParam(required = false) QuestionStatus questionStatus,
                                                            @RequestParam(required = false) String questionTitle,
                                                            @RequestParam(required = false) Integer views,
                                                            @RequestParam(required = false,defaultValue="1")int pageNo,
                                                            @RequestParam(required = false,defaultValue="10")int size) {
        Pageable pageRequestData = PageRequest.of(pageNo - 1, size);
        ResponseModel<Question> questions= questionIService.getQuestionByContraintes(questionStatus,questionTitle,views,pageRequestData);
        return questions;
    }
    @GetMapping("countQuesions")
    public int countQuesions() {
        return questionIService.countQuesions();
    }
    @GetMapping("getMyQuestionById")
    public Question getMyQuestionById(@RequestParam long idQuestion) {
        return questionIService.getMyQuestionById(idQuestion);
    }
    @PutMapping("updateStatusQuestion")
    public Question updateStatusQuestion(@RequestParam long idQuestion, @RequestParam QuestionStatus questionStatus) {
        return questionIService.updateStatusQuestion(idQuestion, questionStatus);
    }
    }
