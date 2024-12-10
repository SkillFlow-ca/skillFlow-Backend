package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.model.Answer;
import com.skillflow.skillflowbackend.service.AnswerIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/answer/")
public class AnswerController {
    @Autowired
    private AnswerIService answerIService;
    @PostMapping("add")
    public Answer addAnswerAndAssignToQuestion(@RequestBody Answer answer, @RequestParam long idQuestion) {
        return answerIService.addAnswerAndAssignToQuestion(answer, idQuestion);
    }

    @PutMapping("update")
    public Answer updateAnswer(@RequestBody Answer answer, @RequestParam long idAnswer) {
        return answerIService.updateAnswer(answer, idAnswer);
    }

    @PutMapping("delete")
    public Answer deleteAnswer(@RequestParam long idAnswer) {
        return answerIService.deleteAnswer(idAnswer);
    }

    @GetMapping("getAnswersByQuestionId")
    public List<Answer> getAnswersByQuestionId(@RequestParam long idQuestion) {
        return answerIService.getAnswersByQuestionId(idQuestion);
    }
}
