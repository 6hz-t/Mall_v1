package cn.net.susan.service.task;

import cn.net.susan.entity.common.CommonTaskEntity;


public interface IAsyncTask {

    /**
     * 执行定时任务
     *
     * @param commonTaskEntity 数据
     */
    void doTask(CommonTaskEntity commonTaskEntity);
}
