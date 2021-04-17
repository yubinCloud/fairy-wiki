package io.github.yubincloud.fairywiki.dto.resp;

import lombok.Data;

import java.util.List;

@Data
public class PageRespDto<T> {

    private long total;

    private List<T> list;
}
