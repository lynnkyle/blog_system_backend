package com.lynnwork.sobblogsystem.controller.admin;


import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.service.IImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * tb_image 前端控制器
 * </p>
 *
 * @author lynnkyle
 * @since 2024-11-04
 */
@Slf4j
@RestController
@RequestMapping("/admin/image")
public class ImageController {
    @Autowired
    private IImageService imageService;

    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult uploadImage(@RequestPart("file") MultipartFile file) {
        return imageService.uploadImage(file);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/{url}")
    public void getImage(@PathVariable String url, HttpServletResponse resp) {
        try {
            imageService.getImage(url, resp);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listImages(@PathVariable("page") int page, @PathVariable("size") int size) {
        return imageService.listImages(page, size);
    }

    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{imageId}")
    public ResponseResult deleteImage(@PathVariable("imageId") String imageId) {
        return imageService.deleteImage(imageId);
    }

}

