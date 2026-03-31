package cn.net.susan.listener;

import cn.net.susan.annotation.AsyncTask;
import cn.net.susan.enums.CouponTypeEnum;
import cn.net.susan.factory.AsyncTaskStrategyContextFactory;
import cn.net.susan.service.marketing.strategy.CouponContext;
import cn.net.susan.service.marketing.strategy.ICouponStrategy;
import cn.net.susan.service.task.IAsyncTask;
import org.apache.commons.collections4.MapUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Component
public class CustomizeApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        initAsyncTask(applicationContext);
        initCouponStrategy(applicationContext);
    }

    private void initAsyncTask(ApplicationContext applicationContext) {
        Map<String, Object> beansWithMap = applicationContext.getBeansWithAnnotation(AsyncTask.class);
        int initSize = MapUtils.isEmpty(beansWithMap) ? 0 : beansWithMap.size();

        Map<Integer, IAsyncTask> handlerMap = new HashMap<>(initSize);
        if (MapUtils.isNotEmpty(beansWithMap)) {
            beansWithMap.forEach((beanName, object) -> {
                AsyncTask taskTypeEnum = object.getClass().getAnnotation(AsyncTask.class);
                if (Objects.nonNull(taskTypeEnum)) {
                    handlerMap.put(taskTypeEnum.value().getValue(), (IAsyncTask) object);
                }
            });

            AsyncTaskStrategyContextFactory.getInstance().initAsyncTaskMap(handlerMap);
        }
    }

    private void initCouponStrategy(ApplicationContext applicationContext) {
        Map<String, ICouponStrategy> beansWithMap = applicationContext.getBeansOfType(ICouponStrategy.class);
        int initSize = MapUtils.isEmpty(beansWithMap) ? 0 : beansWithMap.size();
        Map<CouponTypeEnum, ICouponStrategy> handlerMap = new HashMap<>(initSize);

        if (MapUtils.isNotEmpty(beansWithMap)) {
            beansWithMap.forEach((beanName, couponStrategy) -> {
                handlerMap.put(couponStrategy.getType(), couponStrategy);
            });

            CouponContext.getInstance().initMap(handlerMap);
        }
    }

}



