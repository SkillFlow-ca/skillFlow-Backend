package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.exception.CustomExceptionHandler;
import com.skillflow.skillflowbackend.exception.UserServiceCustomException;
import com.skillflow.skillflowbackend.model.Answer;
import com.skillflow.skillflowbackend.model.Question;
import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.model.Vote;
import com.skillflow.skillflowbackend.repository.AnswerRepository;
import com.skillflow.skillflowbackend.repository.QuestionRepository;
import com.skillflow.skillflowbackend.repository.VoteRepository;
import com.skillflow.skillflowbackend.service.AnswerIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AnswerService implements AnswerIService {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private VoteRepository voteRepository;
    @Override
    public Answer addAnswerAndAssignToQuestion(Answer answer, long idQuestion) {
        Question question = questionRepository.findById(idQuestion).get();
        User user=this.sessionService.getUserBySession().get();
        answer.setQuestion(question);
        answer.setUser(user);
        answer.setCreatedAt(Instant.now());
        answer.setIsDeleted(false);
        answerRepository.save(answer);
        return answer;
    }

    @Override
    public Answer updateAnswer(Answer answer, long idAnswer) {
        Answer answer1 = answerRepository.findById(idAnswer).get();
        User user=this.sessionService.getUserBySession().get();
        answer.setUser(user);
        answer1.setUpdatedAt(Instant.now());
        answer1.setContent(answer.getContent());
        return answerRepository.save(answer1);
    }

    @Override
    public Answer deleteAnswer(long idAnswer) {
        Answer answer= answerRepository.findById(idAnswer).get();
        answer.setIsDeleted(true);
        return answer;
    }

    @Override
    public List<Answer> getAnswersByQuestionId(long idQuestion) {
        Question question = questionRepository.findById(idQuestion).get();
        return answerRepository.getAnswersByQuestionId(question.getIdQuestion());
    }
    @Override
    public Answer updateAnswerToAddAvote(long idAnswer, String vote) {
        Answer answer = answerRepository.findById(idAnswer).orElseThrow(() -> new UserServiceCustomException("Answer not found", "ANSWER_NOT_FOUND"));
        User user = sessionService.getUserBySession().orElseThrow(() -> new UserServiceCustomException("User not found", "USER_NOT_FOUND"));

        // Check if the user has already voted
        for (Vote existingVote : answer.getVoteList()) {
            if (existingVote.getUser().equals(user)) {
                throw new UserServiceCustomException("User has already voted", "ALREADY_VOTED");
            }
        }

        // Create a new vote
        Vote newVote = new Vote();
        newVote.setUser(user);
        newVote.setAnswer(answer);
        newVote.setCreatedAt(Instant.now());
        if ("UP".equalsIgnoreCase(vote)) {
            newVote.setUpVote(true);
            newVote.setDownVote(false);
            answer.setVotes(answer.getVotes() + 1);
        } else if ("DOWN".equalsIgnoreCase(vote)) {
            if (answer.getVotes() > 0) {
                newVote.setUpVote(false);
                newVote.setDownVote(true);
                answer.setVotes(answer.getVotes() - 1);
            }
        } else {
            throw new UserServiceCustomException("Invalid vote value", "INVALID_VOTE");
        }
        voteRepository.save(newVote);
        // Add the new vote to the answer
        answer.getVoteList().add(newVote);
        return answerRepository.save(answer);
    }
}
