package io.github.yubincloud.fairywiki.service;

import io.github.yubincloud.fairywiki.domain.Ebook;
import io.github.yubincloud.fairywiki.domain.EbookExample;
import io.github.yubincloud.fairywiki.dto.resp.EbookRespDto;
import io.github.yubincloud.fairywiki.mapper.EbookMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    @Autowired
    private EbookMapper ebookMapper;

    public List<Ebook> list() {
        return ebookMapper.selectByExample(null);
    }

    public List<EbookRespDto> fuzzyQueryByName(String name) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + name + "%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        List<EbookRespDto> ebookRespDtoList = new ArrayList<>();
        for (Ebook ebook : ebookList) {
            EbookRespDto ebookRespDto = new EbookRespDto();
            BeanUtils.copyProperties(ebook, ebookRespDto);
            ebookRespDtoList.add(ebookRespDto);
        }
        return ebookRespDtoList;
    }
}
