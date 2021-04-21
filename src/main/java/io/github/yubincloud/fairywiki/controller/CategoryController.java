package io.github.yubincloud.fairywiki.controller;

import io.github.yubincloud.fairywiki.dto.req.CategoryQueryReqDto;
import io.github.yubincloud.fairywiki.dto.req.CategorySaveReqDto;
import io.github.yubincloud.fairywiki.dto.resp.CategoryQueryRespDto;
import io.github.yubincloud.fairywiki.dto.resp.ErrorCode;
import io.github.yubincloud.fairywiki.dto.resp.PageRespDto;
import io.github.yubincloud.fairywiki.dto.resp.RestfulModel;
import io.github.yubincloud.fairywiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 对 category 进行查询的接口
     * @param categoryQueryReqDto 查询条件的参数
     * @return 查询到的所有category
     */
    @GetMapping("/query")
    public RestfulModel<PageRespDto<CategoryQueryRespDto>> queryCategorys(@Valid CategoryQueryReqDto categoryQueryReqDto) {
        PageRespDto<CategoryQueryRespDto> bookList = categoryService.queryCategorys(categoryQueryReqDto);
        return new RestfulModel<>(ErrorCode.SUCCESS, "", bookList);
    }

    /**
     * 根据请求的参数保存一个 category，若id非空则为更新，否则为新增
     */
    @PostMapping("/save")
    public RestfulModel<Integer> saveCategory(@RequestBody @Valid CategorySaveReqDto categorySaveReqDto) {
        categoryService.save(categorySaveReqDto);
        return new RestfulModel<>(ErrorCode.SUCCESS, "", 0);
    }

    @DeleteMapping("/delete/{categoryId}")
    public RestfulModel<Integer> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteOneCategory(categoryId);
        return new RestfulModel<>(ErrorCode.SUCCESS, "", 0);
    }
}
