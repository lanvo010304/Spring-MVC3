package com.abc.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.abc.entities.Post;
import com.abc.entities.User;
import com.abc.services.FollowService;
import com.abc.services.PostService;
import jakarta.servlet.http.HttpSession;

@Controller
public class SearchController {

    private final FollowService followService;
    private final PostService postService;

    @Autowired
    public SearchController(FollowService followService, PostService postService) {
        this.followService = followService;
        this.postService = postService;
    }

    @GetMapping("/search")
    public String searchUsers(
            @RequestParam(value = "minFollowers", defaultValue = "5") int minFollowers,
            @RequestParam(value = "minFollowing", defaultValue = "3") int minFollowing,
            Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Đảm bảo giá trị không âm
        minFollowers = Math.max(0, minFollowers);
        minFollowing = Math.max(0, minFollowing);

        // Lấy kết quả tìm kiếm
        List<User> searchResults = followService.searchUsersByFollowCriteria(minFollowers, minFollowing);

        // Lấy dữ liệu trang chủ
        List<User> userfed = followService.getUserFollower(user.getId());
        List<Post> posts = postService.getAllPost(user.getId());
        List<User> suggestfollow = followService.getSuggestFollow(user.getId());

        // Thêm vào model
        model.addAttribute("searchResults", searchResults != null ? searchResults : new ArrayList<User>());
        model.addAttribute("userfed", userfed != null ? userfed : new ArrayList<User>());
        model.addAttribute("posts", posts != null ? posts : new ArrayList<Post>());
        model.addAttribute("suggestfollow", suggestfollow != null ? suggestfollow : new ArrayList<User>());
        model.addAttribute("user", user);

        return "home";
    }
}