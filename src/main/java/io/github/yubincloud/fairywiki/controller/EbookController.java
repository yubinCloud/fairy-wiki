package io.github.yubincloud.fairywiki.controller;

import io.github.yubincloud.fairywiki.dto.req.EbookQueryReqDto;
import io.github.yubincloud.fairywiki.dto.req.EbookSaveReqDto;
import io.github.yubincloud.fairywiki.dto.req.PageReqDto;
import io.github.yubincloud.fairywiki.dto.resp.EbookQueryRespDto;
import io.github.yubincloud.fairywiki.dto.resp.ErrorCode;
import io.github.yubincloud.fairywiki.dto.resp.PageRespDto;
import io.github.yubincloud.fairywiki.dto.resp.RestfulModel;
import io.github.yubincloud.fairywiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    /**
     * 获取全部的电子书信息
     */
    @GetMapping("/list")
    public RestfulModel<PageRespDto<EbookQueryRespDto>> getAllEbook(@Valid PageReqDto pageReqDto) {
        PageRespDto<EbookQueryRespDto> bookList = ebookService.queryAll(pageReqDto.getPageNum(), pageReqDto.getPageSize());
        return new RestfulModel<>(ErrorCode.SUCCESS, "", bookList);
    }

    /**
     * 根据书名进行电子书的模糊查询接口
     */
    @GetMapping("/list2")
    public RestfulModel<List<EbookQueryRespDto>> list(EbookQueryReqDto ebookQueryReqDto) {
        List<EbookQueryRespDto> bookList = ebookService.fuzzyQueryByName(ebookQueryReqDto.getName());
        return new RestfulModel<>(ErrorCode.SUCCESS, "", bookList);
    }

    /**
     * 根据请求的参数保存一个 ebook，若id非空则为更新，否则为新增
     */
    @PostMapping("/save")
    public RestfulModel<Integer> saveEbook(@RequestBody EbookSaveReqDto ebookSaveReqDto) {
        ebookService.save(ebookSaveReqDto);
        return new RestfulModel<>(ErrorCode.SUCCESS, "", 0);
    }

    @DeleteMapping("/delete/{ebookId}")
    public RestfulModel<Integer> deleteEbook(@PathVariable Long ebookId) {
        ebookService.deleteOneEbook(ebookId);
        return new RestfulModel<>(ErrorCode.SUCCESS, "", 0);
    }
}
