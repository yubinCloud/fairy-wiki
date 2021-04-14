package io.github.yubincloud.fairywiki.service;

import io.github.yubincloud.fairywiki.domain.Test;
import io.github.yubincloud.fairywiki.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestMapper testMapper;

    public List<Test> list() {
        return testMapper.list();
    }
}
