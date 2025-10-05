package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.RecStatCde;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.service.AmendmentService;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentImpl;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class AmendmentDeleteFetcherTest {

    @Mock
    private AmendmentService amendmentService;
    private DataFetchingEnvironment environment;

    private AmendmentDeleteFetcher amendmentDeleteFetcher;

    @Before
    public void setUp() {
        amendmentDeleteFetcher = new AmendmentDeleteFetcher(amendmentService);
        environment = new DataFetchingEnvironmentImpl.Builder()
                .arguments(new Document("amendmentId", 1L))
                .build();

    }

    @Test
    public void testDeleteAmendment_returnSuccessResult() {
        Document amendment = new Document(Field.recStatCde, RecStatCde.draft.name());
        Mockito.when(amendmentService.getAmendmentById(environment.getArgument("amendmentId"))).thenReturn(amendment);
        Assert.assertEquals(RecStatCde.deleted.name(), amendmentDeleteFetcher.get(environment).get(Field.recStatCde));
    }

    @Test(expected = productErrorException.class)
    public void testDeleteAmendment_giveNullAmendmentThenThrowException() {
        amendmentDeleteFetcher.get(environment).get(Field.recStatCde);
    }

    @Test(expected = productErrorException.class)
    public void testDeleteAmendment_giveApprovedAmendmentThenThrowException() {
        Document amendment = new Document(Field.recStatCde, RecStatCde.approved.name());
        Mockito.when(amendmentService.getAmendmentById(environment.getArgument("amendmentId"))).thenReturn(amendment);
        amendmentDeleteFetcher.get(environment).get(Field.recStatCde);
    }
}
