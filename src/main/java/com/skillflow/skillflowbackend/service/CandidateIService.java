package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.model.Candidate;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface CandidateIService {
    public Candidate addCandidateASSignToJob(Candidate candidate, long idJob) throws MessagingException;
    public Candidate updateCandidateToAddResume(long idCandidate, MultipartFile resume) throws IOException;
    public void deleteCandidate(long idCandidate);
    public List<Candidate> getCandidatesByJob(long idJob);
    public Candidate getCandidateByTrackCode(String trackCode);
}
