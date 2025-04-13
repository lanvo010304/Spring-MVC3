package com.abc.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.abc.entities.Post;
import com.abc.entities.User;
import com.abc.services.FollowService;
import com.abc.services.PostService;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    private final PostService postService;
    private final FollowService followService;

    @Autowired
    public HomeController(PostService postService, FollowService followService) {
        this.postService = postService;
        this.followService = followService;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Lấy dữ liệu
        List<User> userfed = followService.getUserFollower(user.getId());
        List<Post> posts = postService.getAllPost(user.getId());
        List<User> suggestfollow = followService.getSuggestFollow(user.getId());

        // Thêm vào model
        model.addAttribute("userfed", userfed != null ? userfed : new ArrayList<User>());
        model.addAttribute("posts", posts != null ? posts : new ArrayList<Post>());
        model.addAttribute("suggestfollow", suggestfollow != null ? suggestfollow : new ArrayList<User>());
        model.addAttribute("user", user);

        return "home";
    }
}