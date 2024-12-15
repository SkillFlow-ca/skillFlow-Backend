package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.model.CommentAnswer;
import com.skillflow.skillflowbackend.service.CommentIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/comment/")
public class CommentController {
    @Autowired
    private CommentIService commentIService;

    @PostMapping("add")
    public CommentAnswer addComment(@Validated @RequestBody CommentAnswer commentAnswer, @RequestParam long idAnswer) {
        return commentIService.addCommentAndAssingToAnswer(commentAnswer, idAnswer);
    }
}
