package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Blog;
import com.skillflow.skillflowbackend.model.BlogCategory;
import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.model.enume.StatusBlog;
import com.skillflow.skillflowbackend.repository.BlogCategoryRepository;
import com.skillflow.skillflowbackend.repository.BlogRepository;
import com.skillflow.skillflowbackend.service.BlogIService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogService implements BlogIService {
    private final ChatModel chatModel;
    private final ImageModel imageModel;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private BlogCategoryRepository blogCategoryRepository;

    public BlogService(ChatModel chatModel, ImageModel imageModel) {
        this.chatModel = chatModel;
        this.imageModel = imageModel;
    }

    @Override
    public List<Blog> create3BlogWitAI() {
        List<Blog> blogs = new ArrayList<>();
        List<BlogCategory> blogCategoryList= (List<BlogCategory>) blogCategoryRepository.findAll();
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            Blog blog = new Blog();
            BlogCategory category = blogCategoryList.get(random.nextInt(blogCategoryList.size()));
            PromptTemplate promptTemplate = new PromptTemplate("Create a blog about " + category.getDescription());
            Prompt prompt = promptTemplate.create();
            ChatResponse response = chatModel.call(prompt);
            String content = response.getResult().getOutput().getContent();
            String title = content.split("\\.")[0]; // Extract the first sentence as the title
            blog.setContent(content);
            blog.setTitle(title);
            blog.setAiGenerated(true);
            blog.setCreatedAt(Instant.now());
            blog.setBlogCategoryList(List.of(category));
            blog.setStatusBlog(StatusBlog.PENDING);
            blog.setIsDeleted(false);
            // Generate image based on blog content
            byte[] image = getImageAboutBlog(title);
            blog.setBlogPicture(image);
            blogRepository.save(blog);
            blogs.add(blog);
        }
        return blogs;
    }

    @Override
    public Blog getBlogById(long idBlog) {
        return blogRepository.findById(idBlog).orElse(null);
    }



    @Override
    public ResponseModel<Blog> getBlogsByStatusOrderByCreatedAt(StatusBlog status, Pageable pageable) {
        Page<Blog> blogList=blogRepository.findByStatusBlogAndIsDeletedFalseOrderByCreatedAtDesc(status,pageable);
        return buildResponse(blogList);
    }

    @Override
    public Blog changeStatusOfBlog(long idBlog, StatusBlog statusBlog) {
        Blog blog= blogRepository.findById(idBlog).get();
        blog.setStatusBlog(statusBlog);
        blogRepository.save(blog);
        return blog;
    }

    @Override
    public Blog updateBlog(long id, Blog blog) {
        Blog blog1= blogRepository.findById(id).orElse(null);
        if(blog1!=null){
            blog1.setTitle(blog.getTitle());
            blog1.setContent(blog.getContent());
            blog1.setUpdatedAt(Instant.now());
            blog1.setStatusBlog(blog.getStatusBlog());
            blog1.setBlogCategoryList(blog.getBlogCategoryList());
            blog1.setAdmin(blog.getAdmin());
            blog1.setBlogPicture(blog.getBlogPicture());
            blog1.setAiGenerated(blog.isAiGenerated());
            blog1.setIsDeleted(blog.getIsDeleted());
            blogRepository.save(blog1);
            return blog1;
        }
        return blog1;
    }

    @Override
    public Blog updateBlogToAddImage(long idBlog,MultipartFile img) throws IOException {
        Blog blog= blogRepository.findById(idBlog).get();
        blog.setBlogPicture(img.getBytes());
        return blogRepository.save(blog);
    }

    @Override
    public void deleteBlog(long id) {
        Blog blog= blogRepository.findById(id).get();
        blog.setIsDeleted(true);
        blogRepository.save(blog);
    }


    private byte[] getImageAboutBlog(String prompt){
        try {
            var options = OpenAiImageOptions.builder()
                    .withHeight(1024).withWidth(1024)
                    .withResponseFormat("b64_json")
                    .withModel("dall-e-3")
                    .build();

            ImagePrompt imagePrompt = new ImagePrompt(prompt, options);
            var imageResponse = imageModel.call(imagePrompt);

            // Ensure response structure is correct
            String base64Image = imageResponse.getResult().getOutput().getB64Json();
            if (base64Image == null) {
                throw new IllegalStateException("Base64 image data is missing in the response.");
            }

            // Decode base64 to byte array
            return Base64.getDecoder().decode(base64Image);

        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve image data", e);
        }
    }

    private ResponseModel<Blog> buildResponse(Page<Blog> blog) {
        List<Blog> listBlog = blog.toList()
                .stream()
                .collect(Collectors.toList());
        return ResponseModel.<Blog>builder()
                .pageNo(blog.getNumber())
                .pageSize(blog.getSize())
                .totalElements(blog.getTotalElements())
                .totalPages(blog.getTotalPages())
                .data(listBlog)
                .isLastPage(blog.isLast())
                .build();
    }
}
