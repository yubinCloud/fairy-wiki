package io.github.yubincloud.fairywiki.mapper;

import io.github.yubincloud.fairywiki.domain.Test;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestMapper {

    List<Test> list();
}
