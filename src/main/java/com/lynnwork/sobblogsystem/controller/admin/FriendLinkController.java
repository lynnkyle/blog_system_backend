package com.lynnwork.sobblogsystem.controller.admin;


import com.lynnwork.sobblogsystem.pojo.FriendLink;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.service.IFriendLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * tb_friend_link 前端控制器
 * </p>
 *
 * @author lynnkyle
 * @since 2024-12-20
 */
@RestController
@RequestMapping("/admin/friend-link")
public class FriendLinkController {

    @Autowired
    private IFriendLinkService friendLinkService;

    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addFriendLink(@RequestBody FriendLink friendLink) {
        return friendLinkService.addFiendLink(friendLink);
    }

    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{friendLinkId}")
    public ResponseResult deleteFriendLink(@PathVariable("friendLinkId") String friendLinkId) {
        return friendLinkService.deleteFriendLink(friendLinkId);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/{friendLinkId}")
    public ResponseResult getFriendLink(@PathVariable("friendLinkId") String friendLinkId) {
        return friendLinkService.getFriendLink(friendLinkId);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listFriendLinks(@RequestParam("page") int page, @RequestParam("sizes") int size) {
        return friendLinkService.listFriendLinks(page, size);
    }

    @PutMapping("/{friendLinkId}")
    public ResponseResult updateFriendLinks(@PathVariable("friendLinkId") String friendLinkId, @RequestBody FriendLink friendLink) {
        return friendLinkService.updateFriendLink(friendLinkId, friendLink);
    }
}

