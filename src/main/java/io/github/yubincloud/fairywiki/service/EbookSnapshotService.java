package io.github.yubincloud.fairywiki.service;

import io.github.yubincloud.fairywiki.mapper.EbookSnapshotMapperCustom;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EbookSnapshotService {

    @Resource
    private EbookSnapshotMapperCustom ebookSnapshotMapperCustom;

    public void genSnapshots() {
        ebookSnapshotMapperCustom.genSnapshot();
    }
}
