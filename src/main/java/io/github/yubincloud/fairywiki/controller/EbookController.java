package io.github.yubincloud.fairywiki.controller;

import io.github.yubincloud.fairywiki.dto.req.EbookReqDto;
import io.github.yubincloud.fairywiki.dto.req.PageReqDto;
import io.github.yubincloud.fairywiki.dto.resp.EbookRespDto;
import io.github.yubincloud.fairywiki.dto.resp.ErrorCode;
import io.github.yubincloud.fairywiki.dto.resp.PageRespDto;
import io.github.yubincloud.fairywiki.dto.resp.RestfulModel;
import io.github.yubincloud.fairywiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    public RestfulModel<PageRespDto<EbookRespDto>> getAllEbook(PageReqDto pageReqDto) {
        PageRespDto<EbookRespDto> bookList = ebookService.queryAll(pageReqDto.getPageNum(), pageReqDto.getPageSize());
        return new RestfulModel<>(ErrorCode.SUCCESS, "", bookList);
    }

    /**
     * 根据书名进行电子书的模糊查询接口
     */
    @GetMapping("/list2")
    public RestfulModel<List<EbookRespDto>> list(EbookReqDto ebookReqDto) {
        List<EbookRespDto> bookList = ebookService.fuzzyQueryByName(ebookReqDto.getName());
        return new RestfulModel<>(ErrorCode.SUCCESS, "", bookList);
    }
}
