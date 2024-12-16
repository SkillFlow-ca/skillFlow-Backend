package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.dto.UserDTO;
import com.skillflow.skillflowbackend.model.Blog;
import com.skillflow.skillflowbackend.model.Job;
import com.skillflow.skillflowbackend.model.JobScraping;
import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.model.enume.ScrapingStatus;
import com.skillflow.skillflowbackend.model.enume.SourceJob;
import com.skillflow.skillflowbackend.repository.JobRepository;
import com.skillflow.skillflowbackend.repository.JobScrapingRepository;
import com.skillflow.skillflowbackend.service.JobScrapingIService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class JobScrapingService implements JobScrapingIService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobScrapingRepository historyRepository;

    private static final List<String> KEYWORDS = Arrays.asList("DevOps", "Kubernetes", "Cloud", "IA", "Machine Learning", "Artificial Intelligence", "System Administration", "Network Administration");

    public List<Job> scrapeJobs() {
        List<Job> scrapedJobs = new ArrayList<>();
        String baseUrl = "https://www.jobbank.gc.ca/jobsearch/jobsearch?searchstring=";

        for (String keyword : KEYWORDS) {
            String url = baseUrl + keyword + "&locationstring=&locationparam=";
            try {
                // Fetch the HTML content
                Document document = Jsoup.connect(url).get();

                // Select job elements
                Elements jobElements = document.select("article.action-buttons");

                for (Element jobElement : jobElements) {
                    String title = jobElement.select("span.noctitle").text();
                    String company = jobElement.select("li.business").text();
                    String location = jobElement.select("li.location").text();
                    String salary = jobElement.select("li.salary").text();
                    String jobUrl = jobElement.select("a.resultJobItem").attr("href");

                    if (!jobRepository.existsByTitleAndCompanyName(title, company)) {
                        // Create a Job entity
                        Job job = new Job();
                        job.setTitle(title);
                        job.setCompanyName(company);
                        job.setLocation(location);
                        job.setSalary(salary);
                        job.setSourceJob(SourceJob.SCRAPED);
                        job.setJobUrl(url + jobUrl);
                        job.setCreatedAt(Instant.now());
                        job.setScrapedUrl(url);
                        job.setKeyword(keyword);
                        job.setIsDeleted(false);

                        // Save to database
                        Job savedJob = jobRepository.save(job);
                        scrapedJobs.add(savedJob);

                        // Log scraping success
                        saveScrapingHistory(savedJob, url, "COMPLETED", null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Log scraping failure
                saveScrapingHistory(null, url, "FAILED", e.getMessage());
            }
        }
        return scrapedJobs;
    }

    private void saveScrapingHistory(Job job, String url, String status, String errorMessage) {
        JobScraping history = new JobScraping();
        history.setJob(job);
        history.setScraperName("DefaultScraper");
        history.setScrapedAt(Instant.now());
        history.setStatus(ScrapingStatus.valueOf(status));
        history.setErrorMessage(errorMessage);
        historyRepository.save(history);
    }
}
