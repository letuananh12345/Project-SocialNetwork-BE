package com.meta.socialnetwork.controller;

import com.meta.socialnetwork.dto.response.ResponseMessage;
import com.meta.socialnetwork.model.*;
import com.meta.socialnetwork.security.userPrinciple.UserDetailServiceImpl;
import com.meta.socialnetwork.security.userPrinciple.UserPrinciple;
import com.meta.socialnetwork.service.comment.ICommentService;
import com.meta.socialnetwork.service.friend.IFriendService;
import com.meta.socialnetwork.service.like.ILikeService;
import com.meta.socialnetwork.service.notification.INotificationService;
import com.meta.socialnetwork.service.post.IPostService;
import com.meta.socialnetwork.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.ASC;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    IPostService postService;
    @Autowired
    IUserService userService;
    @Autowired
    ILikeService likeService;
    @Autowired
    ICommentService commentService;
    @Autowired
    IFriendService friendService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    INotificationService notificationService;

    @GetMapping("/showPost")
    public ResponseEntity<?> getListPost() {
        Iterable<Post> postPage = postService.findAllByOrderByIdDesc();
        return new ResponseEntity<>(postPage, HttpStatus.OK);
    }

    @GetMapping("/showPostProfile")
    public ResponseEntity<Iterable<Post>> postProfile() {
        Iterable<Post> posts = postService.findPostsByUser_Id(userDetailService.getCurrentUser().getId());
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // show post theo trạng thái
    @GetMapping("/showPostPublic")
    public ResponseEntity<?> getPostPublic(@RequestParam String status) {
        Iterable<Post> list = postService.findByStatusOrderByIdDesc(status);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //show post theo id_user
    @GetMapping("/showPostUser/{id}/{idPost}")
    public ResponseEntity<?> showPostUser(@PathVariable Long id, @PathVariable Long idPost) {
        User user = userService.findById(id).get();
        User user1 = userService.findById(idPost).get();
        if (friendService.isFriend(user, user1, true, user1, user, true)) {
            List<Post> list = postService.findPostsByStatus(id, "public", "friend");
            if (list != null) {
                return new ResponseEntity<>(list, HttpStatus.OK);
            }
            return new ResponseEntity<>("no post", HttpStatus.OK);
        } else return new ResponseEntity<>("no friend", HttpStatus.OK);
    }

    // tạo bài post
    @PostMapping("/createPost")
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        User user = userDetailService.getCurrentUser();
        post.setUser(user);
        LocalDate localDate = LocalDate.now();
        post.setCreated_date(localDate);
        postService.save(post);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    //xóa bài post
    @GetMapping("/deletepost/{idPost}")
    public ResponseEntity<String> deletePost(@PathVariable Long idPost) {
        Iterable<Comment> listComment = commentService.findAllByPost_Id(idPost);
        commentService.deleteAll(listComment);
        Iterable<Like> likes = likeService.findAllLikeByPosts_Id(idPost);
        likeService.deleteAll(likes);
        postService.remove(idPost);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @GetMapping("/findPost/{idPost}")
    public ResponseEntity<?> findPostById(@PathVariable("idPost") Long idPost) {
        Optional<Post> post = postService.findById(idPost);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    // tạo like
    @GetMapping("/likeshow/{idPost}")
    public ResponseEntity<Like> createlike(@PathVariable("idPost") Long idPost) {
        User user = userDetailService.getCurrentUser();
        Like like = likeService.findByPostsIdAndUserId(idPost, user.getId());
        if (like != null) {
            likeService.remove(like.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            Post post = postService.findById(idPost).get();
            Like like1 = new Like();
            like1.setUser(user);
            like1.setPosts(post);
            return new ResponseEntity<>(likeService.saves(like1), HttpStatus.OK);
        }
    }

    //listlike
    @GetMapping("/listlike")
    public ResponseEntity<?> getlistlike() {
        List<Like> lists = (List<Like>) likeService.findAll();
        int count = lists.size();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    //showlike theo post
    @GetMapping("/listlike/{id}")
    public ResponseEntity<Iterable<Like>> getlistlikePost(@PathVariable Long id) {
        Iterable<Like> lists =  likeService.findAllLikeByPosts_Id(id);
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }

    // create comment
    @PostMapping("/comment/{idPost}")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment, @PathVariable("idPost") Long idPost) {
        User user = userDetailService.getCurrentUser();
        Post post = new Post();
        post.setId(idPost);
        comment.setUser(user);
        comment.setPost(post);
        LocalDate localDate = LocalDate.now();
        comment.setCreated_date(localDate);
        commentService.saves(comment);
        Notification notification = new Notification();
        notification.setComment(comment);
        notification.setNotify(notification.getComment().getUser().getFullName() + " đã comment bài viết");
        notificationService.saves(notification);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @GetMapping("/showComment/{idPost}")
    public ResponseEntity<?> listComment(@PathVariable("idPost") Long id) {
        Iterable<Comment> comments = commentService.findAllByPost_Id(id);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // sửa comment
    @PutMapping("/updatecomment/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        Comment comment1 = commentService.findById(id).get();
        comment1.setContent(comment.getContent());
        LocalDate localDate = LocalDate.now();
        comment1.setModified_date(localDate);
        return new ResponseEntity<>(commentService.saves(comment1), HttpStatus.OK);
    }

    // xóa comment
    @DeleteMapping("/deletecomment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") Long id) {
        commentService.remove(id);
        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }

    // tìm kiếm bạn theo tên
    @GetMapping("findFriend/{username}")
    public ResponseEntity<?> findFriend(@PathVariable String username) {
        Iterable<User> user1 = userService.findAllByUsernameIsContaining(username);
        return new ResponseEntity<>(user1, HttpStatus.OK);
    }

    // gửi yêu cầu kết bạn
    @GetMapping("/sendaddfriend/{idFriend}")
    public ResponseEntity<Friend> sendAddFriend(@PathVariable("idFriend") Long idFriend) {
        User user = userDetailService.getCurrentUser();
        User friend = userService.findById(idFriend).get();
        Friend friend1 = friendService.findByUser_idAndFriend_id(user, friend);
        if (friend1 == null) {
            Friend newFriend = new Friend();
            newFriend.setUser(user);
            newFriend.setFriend(friend);
            newFriend.setStatus(false);
            friendService.save(newFriend);
            return new ResponseEntity<>(newFriend, HttpStatus.OK);
        }
        return new ResponseEntity<>(friend1, HttpStatus.OK);
    }

    // chấp nhận kết bạn
    @GetMapping("/confirmfriend/{idFriend}")
    public ResponseEntity<User> confirmFriend(@PathVariable("idFriend") Long idFriend) {
        User user = userDetailService.getCurrentUser();
        User friend = userService.findById(idFriend).get();
        Friend friend2 = friendService.findByUser_idAndFriend_id(friend, user);
        friend2.setStatus(true);
        friendService.save(friend2);
        return new ResponseEntity<>(friend, HttpStatus.OK);
    }

    @GetMapping("/setFriend/{idFriend}")
    public ResponseEntity<String> setFriend(@PathVariable("idFriend") Long idFriend) {
        User user = userDetailService.getCurrentUser();
        User friend = userService.findById(idFriend).get();
        Friend friend2 = friendService.findByUser_idAndFriend_id(friend, user);
        if (friend2 != null) {
            return new ResponseEntity<>("Da send add friend", HttpStatus.OK);
        }
        return new ResponseEntity<>("Chua ket ban", HttpStatus.OK);
    }

    // từ chối kết bạn
    @DeleteMapping("/refuse/{idFriend}")
    public ResponseEntity<User> refuseFriend(@PathVariable("idFriend") Long idFriend) {
        User user = userDetailService.getCurrentUser();
        User friend = userService.findById(idFriend).get();
        Friend f = friendService.findByUser_idAndFriend_id(friend, user);
        friendService.remove(f.getId());
        return new ResponseEntity<>(friend, HttpStatus.OK);
    }

    // xóa bạn
    @DeleteMapping("deleteFriend/{id}")
    public ResponseEntity<ResponseMessage> removeFriend(@PathVariable("id") Long id) {
        User user = userService.findById(id).get();
        List<Friend> list = friendService.findAllByIdAcc(user, true, user, true);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                friendService.remove(list.get(i).getId());
            }
            return new ResponseEntity<>(new ResponseMessage("remove"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseMessage("null"), HttpStatus.OK);
    }

    // xem danh sách bạn
    @GetMapping("/showfriend")
    public ResponseEntity<List<User>> showListFriend() {
        User user = userDetailService.getCurrentUser();
        List<Friend> list = friendService.findAllByIdAcc(user, true, user, true);
        List<User> userList = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUser().getId() == user.getId()) {
                    userList.add(list.get(i).getFriend());
                } else {
                    userList.add(list.get(i).getUser());
                }
            }
        }
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    // hien user gui kb
    @GetMapping("/showfriendadd")
    public ResponseEntity<List<User>> showAddFriend() {
        User user = userDetailService.getCurrentUser();
        List<Friend> list = friendService.findFriendAdd(user, false);
        List<User> userList = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                userList.add(list.get(i).getUser());
            }
        }
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/listNotify/{id}")
    public ResponseEntity<Notification> getNot(@PathVariable Long id) {
        Notification notification = notificationService.findById(id).get();
        return new ResponseEntity<>(notification, HttpStatus.OK);
    }

}
