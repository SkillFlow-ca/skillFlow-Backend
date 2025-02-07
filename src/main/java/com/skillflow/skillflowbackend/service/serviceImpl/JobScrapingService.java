package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.dto.UserDTO;
import com.skillflow.skillflowbackend.model.Blog;
import com.skillflow.skillflowbackend.model.Job;
import com.skillflow.skillflowbackend.model.JobScraping;
import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.model.enume.JobType;
import com.skillflow.skillflowbackend.model.enume.ScrapingStatus;
import com.skillflow.skillflowbackend.model.enume.SourceJob;
import com.skillflow.skillflowbackend.repository.JobRepository;
import com.skillflow.skillflowbackend.repository.JobScrapingRepository;
import com.skillflow.skillflowbackend.service.JobScrapingIService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class JobScrapingService implements JobScrapingIService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobScrapingRepository historyRepository;

    private static final List<String> KEYWORDS = Arrays.asList("DevOps", "Kubernetes", "Cloud", "IA","Software Developer", "Machine Learning", "Artificial Intelligence", "System Administration", "Network Administration");
    @Scheduled(cron = "0 0 0 * * ?") // Runs at midnight every day
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
                    System.out.println(title);
                    String company = jobElement.select("li.business").text();
                    System.out.println(company);
                    String location = jobElement.select("li.location").text();
                    String salary = jobElement.select("li.salary").text();
                    String type = jobElement.select("span.telework").text();
                    String id = jobElement.attr("id");
                    String number = id.replaceAll("\\D+", "");
                    String jobUrl = jobElement.select("a.resultJobItem").attr("href");
                    if (!jobRepository.existsByTitleAndCompanyName(title, company)) {
                        // Create a Job entity
                        Job job = new Job();
                        job.setTitle(title);
                        job.setCompanyName(company);
                        job.setLocation(location);
                        job.setSalary(salary);
                        job.setSourceJob(SourceJob.SCRAPED);
                        job.setJobUrl("https://www.jobbank.gc.ca/jobsearch/jobposting/"+number);
                        job.setCreatedAt(Instant.now());
                        job.setScrapedUrl(url);
                        job.setKeyword(keyword);
                        job.setIsDeleted(false);
                        if (type.contains("Telework")) {
                            job.setType(JobType.REMOTE);
                        } else if (type.contains("On site")) {
                            job.setType(JobType.ONSITE);
                        } else if (type.contains("Hybrid")) {
                            job.setType(JobType.HYBRID);
                        } else {
                            job.setType(JobType.OTHER);
                        }
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

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // Runs at midnight every day
    public List<Job> scrapeJobUSA() {
        List<Job> scrapedJobs = new ArrayList<>();

        for (String keyword : KEYWORDS) {
                String url = "https://www.workatastartup.com/companies?demographic=any&hasEquity=any&hasSalary=any&industry=any&interviewProcess=any&jobType=any&layout=list-compact&sortBy=created_desc&tab=any&usVisaNotRequired=any" ;
                System.out.println("Scraping URL: " + url);

                try {
                    // Fetch and parse the document
                    Document document = Jsoup.connect(url).get();
                    Elements jobElements = document.select("div.w-full.bg-beige-lighter.mb-2.rounded-md.p-2.border.border-gray-200.flex");
                    if (jobElements.isEmpty()) break;

                    for (Element jobElement : jobElements) {
                        String title = jobElement.select("div.job-name a").text().isEmpty() ? null : jobElement.select("div.job-name a").text();
                        String company = jobElement.select("div.company-details a span.font-bold").text().isEmpty() ? null : jobElement.select("div.company-details a span.font-bold").text();
                        String location = jobElement.select("p.job-details span:contains(Remote)").text().isEmpty() ? null : jobElement.select("p.job-details span:contains(Remote)").text();
                        String salary = jobElement.select("span:contains($)").text().isEmpty() ? null : jobElement.select("span:contains($)").text();
                        String jobUrl = jobElement.select("div.job-name a").attr("href").isEmpty() ? null : jobElement.select("div.job-name a").attr("href");

                        if (!jobRepository.existsByTitleAndCompanyName(title, company)) {
                            Job job = new Job();
                            job.setTitle(title);
                            job.setCompanyName(company);
                            job.setLocation(location);
                            job.setSalary(salary);
                            job.setType(JobType.REMOTE);
                            job.setSourceJob(SourceJob.SCRAPED);
                            job.setJobUrl(jobUrl.startsWith("http") ? jobUrl : "https://www.workatastartup.com" + jobUrl);
                            job.setCreatedAt(Instant.now());
                            job.setScrapedUrl(url);
                            job.setKeyword(keyword);
                            job.setCountry("United States");
                            job.setIsDeleted(false);

                            Job savedJob = jobRepository.save(job);
                            scrapedJobs.add(savedJob);

                            saveScrapingHistory(savedJob, url, "COMPLETED", null);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    saveScrapingHistory(null, url, "FAILED", e.getMessage());
                    break; // Exit pagination loop on error
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
