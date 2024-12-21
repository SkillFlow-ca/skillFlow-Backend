package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.model.Candidate;
import com.skillflow.skillflowbackend.service.CandidateIService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/candidate/")
public class CandidateController {
    @Autowired
    private CandidateIService candidateIService;

    @PostMapping("addCandidateToJob")
    public Candidate addCandidateToJob(@RequestBody Candidate candidate, @RequestParam long idJob) throws MessagingException {
        return candidateIService.addCandidateASSignToJob(candidate, idJob);
    }

    @PutMapping("updateResume")
    public Candidate updateCandidateResume(@RequestParam long idCandidate, @RequestParam MultipartFile resume) throws IOException {
        return candidateIService.updateCandidateToAddResume(idCandidate, resume);
    }
    @DeleteMapping("delete")
    public void deleteCandidate(@RequestParam long idCandidate) {
        candidateIService.deleteCandidate(idCandidate);
    }
    @GetMapping("candidates")
    public List<Candidate> getCandidatesByJob(@RequestParam long idJob) {
        return candidateIService.getCandidatesByJob(idJob);
    }
    @GetMapping("getByTrackCode")
    public Candidate getCandidateByTrackCode(@RequestParam String trackCode) {
        return candidateIService.getCandidateByTrackCode(trackCode);
    }
    }
