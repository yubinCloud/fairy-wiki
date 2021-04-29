package io.github.yubincloud.fairywiki.job;

import io.github.yubincloud.fairywiki.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DocJob {

    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Resource
    private DocService docService;

    /**
     * 每30秒更新电子书信息
     */
    @Scheduled(cron = "5/30 * * * * ?")
    public void cron() {
        LOG.info("更新电子书下的文档数据开始");
        long start = System.currentTimeMillis();
        docService.updateEbookFooter();
        LOG.info("更新电子书下的文档数据结束，耗时：{}毫秒", System.currentTimeMillis() - start);
    }

}
