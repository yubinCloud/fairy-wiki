package io.github.yubincloud.fairywiki.controller;

import io.github.yubincloud.fairywiki.dto.req.EbookReqDto;
import io.github.yubincloud.fairywiki.dto.resp.EbookRespDto;
import io.github.yubincloud.fairywiki.dto.resp.ErrorCode;
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
     * 根据书名进行电子书的模糊查询接口
     */
    @GetMapping("/list")
    public RestfulModel<List<EbookRespDto>> list(EbookReqDto ebookReqDto) {
        List<EbookRespDto> bookList = ebookService.fuzzyQueryByName(ebookReqDto.getName());
        return new RestfulModel<>(ErrorCode.SUCCESS, "", bookList);
    }
}
