package io.github.yubincloud.fairywiki.controller;


import io.github.yubincloud.fairywiki.dto.resp.ErrorCode;
import io.github.yubincloud.fairywiki.dto.resp.RestfulModel;
import io.github.yubincloud.fairywiki.dto.resp.StatisticRespDto;
import io.github.yubincloud.fairywiki.service.EbookSnapshotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook-snapshot")
@Api("电子书快照管理")
public class EbookSnapshotController {

    @Resource
    private EbookSnapshotService ebookSnapshotService;

    @GetMapping("/get-statistic")
    @ApiOperation(value = "从电子书快照中获取统计数据")
    public RestfulModel<List<StatisticRespDto>> getStatistic() {
        List<StatisticRespDto> statisticRespDtoList = ebookSnapshotService.getStatistic();
        return new RestfulModel<>(ErrorCode.SUCCESS, "", statisticRespDtoList);
    }
}
