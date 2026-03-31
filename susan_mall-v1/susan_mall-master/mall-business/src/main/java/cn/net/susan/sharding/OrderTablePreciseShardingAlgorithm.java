package cn.net.susan.sharding;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
@Slf4j
public class OrderTablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<String> {
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        // 获取分片键的值
        String orderCode = preciseShardingValue.getValue();
        int hashCode = orderCode.hashCode();
        if (hashCode < 0) {
            hashCode = 0 - hashCode;
        }
        String logicTableName = preciseShardingValue.getLogicTableName();
        String physicalTableName = logicTableName + "_" + (hashCode % 2);
        log.info("分片的hashCode: {},逻辑表名 logicTableName: {}", hashCode, physicalTableName);
        if (collection.contains(physicalTableName)) {
            return physicalTableName;
        }
        return physicalTableName;
    }
}
