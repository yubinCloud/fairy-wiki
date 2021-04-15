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

    /**
     * 根据 ebook 的 name 进行模糊查询
     * @param name ebook的书名
     */
    public List<EbookRespDto> fuzzyQueryByName(String name) {
        // 对数据库进行模糊查询
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + name + "%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        // 将 ebookList 中的数据映射到 dto 数据
        List<EbookRespDto> ebookRespDtoList = new ArrayList<>();
        for (Ebook ebook : ebookList) {
            EbookRespDto ebookRespDto = new EbookRespDto();
            BeanUtils.copyProperties(ebook, ebookRespDto);
            ebookRespDtoList.add(ebookRespDto);
        }
        return ebookRespDtoList;
    }
}
