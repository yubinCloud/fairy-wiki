package io.github.yubincloud.fairywiki.controller;

import io.github.yubincloud.fairywiki.dto.req.EbookQueryReqDto;
import io.github.yubincloud.fairywiki.dto.req.EbookSaveReqDto;
import io.github.yubincloud.fairywiki.dto.resp.EbookQueryRespDto;
import io.github.yubincloud.fairywiki.dto.resp.ErrorCode;
import io.github.yubincloud.fairywiki.dto.resp.PageRespDto;
import io.github.yubincloud.fairywiki.dto.resp.RestfulModel;
import io.github.yubincloud.fairywiki.service.EbookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api("电子书管理")
@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    /**
     * @param ebookQueryReqDto 查询条件的参数
     * @return 查询到的所有ebook
     */
    @ApiOperation("对 ebook 进行查询的接口")
    @GetMapping("/query")
    public RestfulModel<PageRespDto<EbookQueryRespDto>> queryEbooks(@Valid EbookQueryReqDto ebookQueryReqDto) {
        PageRespDto<EbookQueryRespDto> bookList = ebookService.queryEbooks(ebookQueryReqDto);
        return new RestfulModel<>(ErrorCode.SUCCESS, "", bookList);
    }

    /**
     * 根据请求的参数保存一个 ebook，若id非空则为更新，否则为新增
     */
    @ApiOperation(value = "根据请求的参数保存一个 ebook",
            notes = "若id非空则为更新，否则为新增")
    @PostMapping("/save")
    public RestfulModel<Integer> saveEbook(@RequestBody @Valid EbookSaveReqDto ebookSaveReqDto) {
        ebookService.save(ebookSaveReqDto);
        return new RestfulModel<>(ErrorCode.SUCCESS, "", 0);
    }

    @ApiOperation(value = "删除一个 ebook")
    @DeleteMapping("/delete/{ebookId}")
    public RestfulModel<Integer> deleteEbook(@PathVariable Long ebookId) {
        ebookService.deleteOneEbook(ebookId);
        return new RestfulModel<>(ErrorCode.SUCCESS, "", 0);
    }
}
