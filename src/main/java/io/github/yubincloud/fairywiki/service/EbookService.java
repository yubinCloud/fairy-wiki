package io.github.yubincloud.fairywiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.github.yubincloud.fairywiki.domain.Ebook;
import io.github.yubincloud.fairywiki.domain.EbookExample;
import io.github.yubincloud.fairywiki.dto.req.EbookSaveReqDto;
import io.github.yubincloud.fairywiki.dto.resp.EbookQueryRespDto;
import io.github.yubincloud.fairywiki.dto.resp.PageRespDto;
import io.github.yubincloud.fairywiki.mapper.EbookMapper;
import io.github.yubincloud.fairywiki.utils.CopyUtil;
import io.github.yubincloud.fairywiki.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 查询数据库中的全部 ebook
     */
    public PageRespDto<EbookQueryRespDto> queryAll(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);  // 对接下来遇到的第一个 SELECT 产生作用
        List<Ebook> ebookList = ebookMapper.selectByExample(null);
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);

        List<EbookQueryRespDto> ebookQueryRespDtoList = CopyUtil.copyList(ebookList, EbookQueryRespDto.class);
        PageRespDto<EbookQueryRespDto> pageRespDto = new PageRespDto<>();
        pageRespDto.setTotal(pageInfo.getTotal());
        pageRespDto.setList(ebookQueryRespDtoList);
        return pageRespDto;
    }

    /**
     * 根据 ebook 的 name 进行模糊查询
     * @param name ebook的书名
     */
    public List<EbookQueryRespDto> fuzzyQueryByName(String name) {
        // 对数据库进行模糊查询
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + name + "%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        // 将 ebookList 拷贝至 dto 对象列表并返回
        return CopyUtil.copyList(ebookList, EbookQueryRespDto.class);
    }

    /**
     * 根据 EbookSaveReqDto 来保存一个 ebook 记录，若 id 为空则新增，不为空则更新
     */
    public void save(EbookSaveReqDto reqDto) {
        Ebook ebookRecord = CopyUtil.copy(reqDto, Ebook.class);
        if (ObjectUtils.isEmpty(ebookRecord.getId())) {  // 判断 id 是否为空
            ebookRecord.setId(snowFlake.nextId());
            ebookMapper.insert(ebookRecord);
        } else {
            ebookMapper.updateByPrimaryKey(ebookRecord);
        }
    }
}
