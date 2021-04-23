package io.github.yubincloud.fairywiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.github.yubincloud.fairywiki.domain.Doc;
import io.github.yubincloud.fairywiki.domain.DocExample;
import io.github.yubincloud.fairywiki.dto.req.DocQueryReqDto;
import io.github.yubincloud.fairywiki.dto.req.DocSaveReqDto;
import io.github.yubincloud.fairywiki.dto.resp.DocQueryRespDto;
import io.github.yubincloud.fairywiki.dto.resp.PageRespDto;
import io.github.yubincloud.fairywiki.mapper.DocMapper;
import io.github.yubincloud.fairywiki.utils.CopyUtil;
import io.github.yubincloud.fairywiki.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Resource
    private DocMapper docMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 获取全部 Doc
     */
    public List<DocQueryRespDto> fetchAllCategories() {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        return CopyUtil.copyList(docList, DocQueryRespDto.class);
    }


    /**
     * 根据查询条件对数据库中的 doc 进行查询并返回查询到的 doc
     */
    public PageRespDto<DocQueryRespDto> queryDocs(DocQueryReqDto reqDto) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        PageHelper.startPage(reqDto.getPageNum(), reqDto.getPageSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // 列表复制
        List<DocQueryRespDto> list = CopyUtil.copyList(docList, DocQueryRespDto.class);

        PageRespDto<DocQueryRespDto> pageRespDto = new PageRespDto<>();
        pageRespDto.setTotal(pageInfo.getTotal());
        pageRespDto.setList(list);

        return pageRespDto;
    }

    /**
     * 根据 DocSaveReqDto 来保存一个 doc 记录，若 id 为空则新增，不为空则更新
     */
    public void save(DocSaveReqDto reqDto) {
        Doc docRecord = CopyUtil.copy(reqDto, Doc.class);
        if (ObjectUtils.isEmpty(docRecord.getId())) {  // 判断 id 是否为空
            docRecord.setId(snowFlake.nextId());
            docMapper.insertSelective(docRecord);
        } else {
            docMapper.updateByPrimaryKey(docRecord);
        }
    }

    public void deleteOneDoc(Long docId) {
        docMapper.deleteByPrimaryKey(docId);
    }
}
