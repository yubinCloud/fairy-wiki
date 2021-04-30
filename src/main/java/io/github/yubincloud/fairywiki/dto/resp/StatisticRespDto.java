package io.github.yubincloud.fairywiki.dto.resp;

import lombok.Data;

import java.util.Date;

@Data
public class StatisticRespDto {
    private Date date;

    private int viewCount;

    private int voteCount;

    private int viewIncrease;

    private int voteIncrease;
}
