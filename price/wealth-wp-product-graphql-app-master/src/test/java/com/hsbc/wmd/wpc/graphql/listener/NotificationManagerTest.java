package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.Operation;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class NotificationManagerTest {

    private List<ChangeListener> listeners = new ArrayList<>();

    @InjectMocks
    private NotificationManager notificationManager;

    private PriceHistoryListener priceHistoryListener = Mockito.mock(PriceHistoryListener.class);
    private TenorCntPreSetListener tenorCntPreSetListener = Mockito.mock(TenorCntPreSetListener.class);
    private PreFillDataListener preFillDataListener = Mockito.mock(PreFillDataListener.class);

    @Before
    public void setUp() {
        Mockito.doCallRealMethod().when(priceHistoryListener).interestJsonPaths();
        Mockito.doCallRealMethod().when(tenorCntPreSetListener).interestJsonPaths();
        Mockito.doCallRealMethod().when(preFillDataListener).interestJsonPaths();

        listeners.add(priceHistoryListener);
        listeners.add(tenorCntPreSetListener);
        listeners.add(preFillDataListener);
        ReflectionTestUtils.setField(notificationManager, "listeners", listeners);
    }


    @Test
    public void testBeforeInsert_given_prod() {
        Document prod = new Document();
        Assertions.assertDoesNotThrow(() -> notificationManager.afterInsert(prod));
    }

    @Test
    public void testBeforeUpdate_given_prodAndoperations() {
        Document prod = new Document();
        List<OperationInput> operations = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> notificationManager.beforeUpdate(prod, operations));
    }

    @Test
    public void testAfterAllUpdate_given_prodAndOperations() {
        Document prod = new Document();
        OperationInput operation = new OperationInput();
        operation.setOp(Operation.put);
        operation.setPath(Field.prodMktPrcAmt);
        operation.setValue(1.234D);
        notificationManager.afterAllUpdate(Collections.singletonMap(prod, Collections.singletonList(operation)));
        Mockito.verify(priceHistoryListener, Mockito.times(1)).afterAllUpdate(Mockito.anyList());
        Mockito.verify(preFillDataListener, Mockito.times(1)).afterAllUpdate(Mockito.anyList());
        Mockito.verify(tenorCntPreSetListener, Mockito.times(0)).afterAllUpdate(Mockito.anyList());
    }

    @Test
    public void testBeforeValidation_given_oldMapAndNewMap() {
        java.util.Map<String, Object> oldProd = new HashMap<>();
        Map<String, Object> newProd = new HashMap<>();
        Assertions.assertDoesNotThrow(() -> notificationManager.beforeValidation(oldProd, newProd, Collections.emptyList()));
    }
}

