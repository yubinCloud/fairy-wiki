package io.github.yubincloud.fairywiki.controller;

import io.github.yubincloud.fairywiki.dto.req.DocDeleteReqDto;
import io.github.yubincloud.fairywiki.dto.req.DocQueryReqDto;
import io.github.yubincloud.fairywiki.dto.req.DocSaveReqDto;
import io.github.yubincloud.fairywiki.dto.resp.DocQueryRespDto;
import io.github.yubincloud.fairywiki.dto.resp.ErrorCode;
import io.github.yubincloud.fairywiki.dto.resp.PageRespDto;
import io.github.yubincloud.fairywiki.dto.resp.RestfulModel;
import io.github.yubincloud.fairywiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService docService;

    /**
     * 获取全部 Doc 的接口
     */
    @GetMapping("/all")
    public RestfulModel<List<DocQueryRespDto>> allCategories() {
        List<DocQueryRespDto> docList = docService.fetchAllCategories();
        return new RestfulModel<>(ErrorCode.SUCCESS, "", docList);
    }

    /**
     * 对 doc 进行查询的接口
     * @param docQueryReqDto 查询条件的参数
     * @return 查询到的所有doc
     */
    @GetMapping("/query")
    public RestfulModel<PageRespDto<DocQueryRespDto>> queryDocs(@Valid DocQueryReqDto docQueryReqDto) {
        PageRespDto<DocQueryRespDto> bookList = docService.queryDocs(docQueryReqDto);
        return new RestfulModel<>(ErrorCode.SUCCESS, "", bookList);
    }

    /**
     * 根据请求的参数保存一个 doc，若id非空则为更新，否则为新增
     */
    @PostMapping("/save")
    public RestfulModel<Integer> saveDoc(@RequestBody @Valid DocSaveReqDto docSaveReqDto) {
        docService.save(docSaveReqDto);
        return new RestfulModel<>(ErrorCode.SUCCESS, "", 0);
    }

    @DeleteMapping("/delete")
    public RestfulModel<Integer> deleteDoc(@RequestBody @Valid DocDeleteReqDto docDeleteReqDto) {
        docService.deleteDocs(docDeleteReqDto.getIds());
        return new RestfulModel<>(ErrorCode.SUCCESS, "", 0);
    }
}
