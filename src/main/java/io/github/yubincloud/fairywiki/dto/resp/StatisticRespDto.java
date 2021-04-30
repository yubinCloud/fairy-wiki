package io.github.yubincloud.fairywiki.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class StatisticRespDto {

    @JsonFormat(pattern="MM-dd", timezone = "GMT+8")
    private Date date;

    private int viewCount;

    private int voteCount;

    private int viewIncrease;

    private int voteIncrease;
}
