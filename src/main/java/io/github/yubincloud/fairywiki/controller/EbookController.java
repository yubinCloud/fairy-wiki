package io.github.yubincloud.fairywiki.controller;

import io.github.yubincloud.fairywiki.domain.Ebook;
import io.github.yubincloud.fairywiki.resp.ErrorCode;
import io.github.yubincloud.fairywiki.resp.RestfulModel;
import io.github.yubincloud.fairywiki.service.EbookService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/list")
    public RestfulModel<List<Ebook>> list() {
        List<Ebook> bookList = ebookService.list();
        return new RestfulModel<>(ErrorCode.SUCCESS, "", bookList);
    }
}
