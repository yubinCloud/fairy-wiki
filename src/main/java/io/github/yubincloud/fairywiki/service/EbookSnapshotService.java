package io.github.yubincloud.fairywiki.service;

import io.github.yubincloud.fairywiki.mapper.EbookSnapshotMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EbookSnapshotService {

    @Resource
    private EbookSnapshotMapper ebookSnapshotMapper;
}
