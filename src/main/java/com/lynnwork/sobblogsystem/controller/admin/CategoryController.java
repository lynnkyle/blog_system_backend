package com.lynnwork.sobblogsystem.controller.admin;


import com.lynnwork.sobblogsystem.pojo.Category;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.service.ICategoryService;
import com.lynnwork.sobblogsystem.service.impl.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * tb_category 前端控制器
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    /*
       @Param
       @Return
    */
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }
}

