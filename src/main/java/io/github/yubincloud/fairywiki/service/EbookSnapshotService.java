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
        List<StatisticRespDto> statisticDataList = ebookSnapshotMapperCustom.getStatistic();
        if (statisticDataList.size() < 2) {
            if (statisticDataList.isEmpty()) {
                statisticDataList.add(null);
                statisticDataList.add(null);
            } else {
                statisticDataList.add(0, null);
            }
        }
        return statisticDataList;
    }

    /**
     * 30天数值统计
     */
    public List<StatisticRespDto> get30DayStatistic() {
        return ebookSnapshotMapperCustom.get30DayStatistic();
    }
}
