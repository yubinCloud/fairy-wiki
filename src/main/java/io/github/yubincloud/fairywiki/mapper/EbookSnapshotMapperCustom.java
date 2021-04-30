package io.github.yubincloud.fairywiki.mapper;

import io.github.yubincloud.fairywiki.dto.resp.StatisticRespDto;

import java.util.List;

public interface EbookSnapshotMapperCustom {
    void genSnapshot();

    List<StatisticRespDto> getStatistic();

    List<StatisticRespDto> get30DayStatistic();
}
