package io.github.yubincloud.fairywiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.github.yubincloud.fairywiki.domain.User;
import io.github.yubincloud.fairywiki.domain.UserExample;
import io.github.yubincloud.fairywiki.dto.resp.PageRespDto;
import io.github.yubincloud.fairywiki.mapper.UserMapper;
import io.github.yubincloud.fairywiki.dto.req.UserQueryReqDto;
import io.github.yubincloud.fairywiki.dto.req.UserSaveReqDto;
import io.github.yubincloud.fairywiki.dto.resp.UserQueryRespDto;
import io.github.yubincloud.fairywiki.utils.CopyUtil;
import io.github.yubincloud.fairywiki.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageRespDto<UserQueryRespDto> list(UserQueryReqDto req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo(req.getLoginName());
        }
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // 列表复制
        List<UserQueryRespDto> list = CopyUtil.copyList(userList, UserQueryRespDto.class);

        PageRespDto<UserQueryRespDto> pageRespDto = new PageRespDto<>();
        pageRespDto.setTotal(pageInfo.getTotal());
        pageRespDto.setList(list);

        return pageRespDto;
    }

    /**
     * 保存
     */
    public void save(UserSaveReqDto req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            user.setId(snowFlake.nextId());
            userMapper.insert(user);
        } else {
            // 更新
            userMapper.updateByPrimaryKey(user);
        }
    }

    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }
}
