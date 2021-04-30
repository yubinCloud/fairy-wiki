package io.github.yubincloud.fairywiki.service;

import io.github.yubincloud.fairywiki.dto.resp.StatisticRespDto;
import io.github.yubincloud.fairywiki.mapper.EbookSnapshotMapperCustom;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookSnapshotService {

    @Resource
    private EbookSnapshotMapperCustom ebookSnapshotMapperCustom;

    public void genSnapshots() {
        ebookSnapshotMapperCustom.genSnapshot();
    }

    /**
     * 获取首页数值数据：总阅读数、总点赞数、今日阅读数、今日点赞数、今日预计阅读数、今日预计阅读增长
     */
    public List<StatisticRespDto> getStatistic() {
        return ebookSnapshotMapperCustom.getStatistic();
    }
}
