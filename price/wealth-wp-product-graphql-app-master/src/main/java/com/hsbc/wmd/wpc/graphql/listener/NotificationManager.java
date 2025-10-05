package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.dummy.wmd.wpc.graphql.utils.OperationInputUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class NotificationManager implements ApplicationContextAware {
    private List<ChangeListener> listeners = new ArrayList<>();

    public NotificationManager() {

    }

    public void beforeInsert(Document doc) {
        listeners.forEach(listener -> listener.beforeInsert(doc));
    }

    public void afterInsert(Document doc) {
        listeners.forEach(listener -> listener.afterInsert(doc));
    }

    public void beforeUpdate(Document doc, List<OperationInput> operations) {
        listeners.stream()
                .filter(listener -> isAnyInterestPathChange(operations, listener))
                .forEach(listener -> listener.beforeUpdate(doc, operations));
    }

    public void afterAllUpdate(Map<Document, List<OperationInput>> operationMap) {
        listeners.forEach(listener -> {
            Map<Document, List<OperationInput>> changedOperationMap = new HashMap<>();
            operationMap.forEach((doc, operations) -> {
                if (isAnyInterestPathChange(operations, listener)) {
                    changedOperationMap.put(doc, operations);
                }
            });
            if (!changedOperationMap.isEmpty()) {
                listener.afterAllUpdate(new ArrayList<>(changedOperationMap.keySet()));
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ChangeListener> beans = applicationContext.getBeansOfType(ChangeListener.class);
        beans.forEach((name, bean) -> this.listeners.add(bean));

        // sort the listeners
        listeners.sort((o1, o2) -> {
            // a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
            if (o1.getOrder() < o2.getOrder()) {
                return -1;
            } else if (o1.getOrder() == o2.getOrder()) {
                return 0;
            }
            return 1;
        });
    }

    public void beforeValidation(Map<String, Object> oldProd, Map<String, Object> newProd, List<OperationInput> operations) {
        listeners.stream()
                .filter(listener -> isAnyInterestPathChange(operations, listener))
                .forEach(listener -> listener.beforeValidation(oldProd, newProd));
    }

    private boolean isAnyInterestPathChange(List<OperationInput> operations, ChangeListener listener) {
        Collection<String> interestJsonPaths = listener.interestJsonPaths();
        if (CollectionUtils.isEmpty(operations) || CollectionUtils.isEmpty(interestJsonPaths)) {
            return true;
        }
        return CollectionUtils.containsAny(interestJsonPaths, OperationInputUtils.extractAllPaths(operations));
    }
}
