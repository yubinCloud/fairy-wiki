package io.github.yubincloud.fairywiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.github.yubincloud.fairywiki.domain.Category;
import io.github.yubincloud.fairywiki.domain.CategoryExample;
import io.github.yubincloud.fairywiki.dto.req.CategoryQueryReqDto;
import io.github.yubincloud.fairywiki.dto.req.CategorySaveReqDto;
import io.github.yubincloud.fairywiki.dto.resp.CategoryQueryRespDto;
import io.github.yubincloud.fairywiki.dto.resp.PageRespDto;
import io.github.yubincloud.fairywiki.mapper.CategoryMapper;
import io.github.yubincloud.fairywiki.utils.CopyUtil;
import io.github.yubincloud.fairywiki.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 获取全部 Category
     */
    public List<CategoryQueryRespDto> fetchAllCategories() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        return CopyUtil.copyList(categoryList, CategoryQueryRespDto.class);
    }


    /**
     * 根据查询条件对数据库中的 category 进行查询并返回查询到的 category
     */
    public PageRespDto<CategoryQueryRespDto> queryCategorys(CategoryQueryReqDto reqDto) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        PageHelper.startPage(reqDto.getPageNum(), reqDto.getPageSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // 列表复制
        List<CategoryQueryRespDto> list = CopyUtil.copyList(categoryList, CategoryQueryRespDto.class);

        PageRespDto<CategoryQueryRespDto> pageRespDto = new PageRespDto<>();
        pageRespDto.setTotal(pageInfo.getTotal());
        pageRespDto.setList(list);

        return pageRespDto;
    }

    /**
     * 根据 CategorySaveReqDto 来保存一个 category 记录，若 id 为空则新增，不为空则更新
     */
    public void save(CategorySaveReqDto reqDto) {
        Category categoryRecord = CopyUtil.copy(reqDto, Category.class);
        if (ObjectUtils.isEmpty(categoryRecord.getId())) {  // 判断 id 是否为空
            categoryRecord.setId(snowFlake.nextId());
            categoryMapper.insertSelective(categoryRecord);
        } else {
            categoryMapper.updateByPrimaryKey(categoryRecord);
        }
    }

    public void deleteOneCategory(Long categoryId) {
        categoryMapper.deleteByPrimaryKey(categoryId);
    }
}
