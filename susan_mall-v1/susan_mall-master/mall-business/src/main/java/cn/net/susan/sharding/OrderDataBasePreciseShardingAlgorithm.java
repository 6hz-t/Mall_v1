package cn.net.susan.sharding;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
@Slf4j
public class OrderDataBasePreciseShardingAlgorithm implements PreciseShardingAlgorithm<String> {
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        // 获取分片键的值
        String orderCode = preciseShardingValue.getValue();
        int hashCode = orderCode.hashCode();
        if (hashCode < 0) {
            hashCode = 0 - hashCode;
        }
        // 根据userId对数据库进行了分片，这里可以根据实际情况进行修改
        String dataSourceName = "ds" + (hashCode % 2);
        log.info("分片的hashCode: {},分片结果 dataSourceName: {}", hashCode, dataSourceName);
        // 返回对应的数据源名称
        for (String name : collection) {
            if (name.endsWith(String.valueOf(hashCode % 2))) {
                return name;
            }
        }
        return dataSourceName;
    }
}
