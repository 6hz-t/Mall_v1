package cn.net.susan.helper;

import cn.net.susan.util.SnowFlakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class IdGenerateHelper {

    @Autowired
    private SnowFlakeIdWorker snowFlakeIdWorker;

    /**
     * 生成分布式ID
     *
     * @return 分布式ID
     */
    public Long nextId() {
        return snowFlakeIdWorker.nextId();
    }
}
