package io.github.yubincloud.fairywiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.github.yubincloud.fairywiki.domain.Ebook;
import io.github.yubincloud.fairywiki.domain.EbookExample;
import io.github.yubincloud.fairywiki.dto.resp.EbookRespDto;
import io.github.yubincloud.fairywiki.dto.resp.PageRespDto;
import io.github.yubincloud.fairywiki.mapper.EbookMapper;
import io.github.yubincloud.fairywiki.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookService {

    @Autowired
    private EbookMapper ebookMapper;

    /**
     * 查询数据库中的全部 ebook
     */
    public PageRespDto<EbookRespDto> queryAll(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);  // 对接下来遇到的第一个 SELECT 产生作用
        List<Ebook> ebookList = ebookMapper.selectByExample(null);
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);

        List<EbookRespDto> ebookRespDtoList = CopyUtil.copyList(ebookList, EbookRespDto.class);
        PageRespDto<EbookRespDto> pageRespDto = new PageRespDto<>();
        pageRespDto.setTotal(pageInfo.getTotal());
        pageRespDto.setList(ebookRespDtoList);
        return pageRespDto;
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
        // 将 ebookList 拷贝至 dto 对象列表并返回
        return CopyUtil.copyList(ebookList, EbookRespDto.class);
    }
}
