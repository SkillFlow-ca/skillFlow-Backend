package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.model.CommentAnswer;

public interface CommentIService {
    public CommentAnswer addCommentAndAssingToAnswer(CommentAnswer commentAnswer, long idAnswer);
}
