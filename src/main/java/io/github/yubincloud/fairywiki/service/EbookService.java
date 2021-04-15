package io.github.yubincloud.fairywiki.service;

import io.github.yubincloud.fairywiki.domain.Ebook;
import io.github.yubincloud.fairywiki.mapper.EbookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookService {

    @Autowired
    private EbookMapper ebookMapper;

    public List<Ebook> list() {
        return ebookMapper.selectByExample(null);
    }
}
