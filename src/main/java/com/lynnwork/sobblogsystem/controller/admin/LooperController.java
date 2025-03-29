package com.lynnwork.sobblogsystem.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lynnwork.sobblogsystem.pojo.Looper;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.service.ILooperService;
import com.lynnwork.sobblogsystem.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * tb_looper 前端控制器
 * </p>
 *
 * @author lynnkyle
 * @since 2025-01-24
 */
@RestController
@RequestMapping("/admin/looper")
public class LooperController {

    @Autowired
    private ILooperService looperService;

    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addLooper(@RequestBody Looper looper) {
        return looperService.addLooper(looper);
    }

    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{looperId}")
    public ResponseResult deleteLooper(@PathVariable("looperId") String looperId) {
        return looperService.deleteLooper(looperId);
    }

    @PreAuthorize("@permission.admin()")
    @PutMapping("/{looperId}")
    public ResponseResult updateLooper(@PathVariable("looperId") String looperId, @RequestBody Looper looper) {
        return looperService.updateLooper(looperId, looper);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/{looperId}")
    public ResponseResult getLooper(@PathVariable("looperId") String looperId) {
        return looperService.getLooper(looperId);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listLoopers(@PathVariable("page") int page, @PathVariable("size") int size) {
        return looperService.listLoopers(page, size);
    }
}

