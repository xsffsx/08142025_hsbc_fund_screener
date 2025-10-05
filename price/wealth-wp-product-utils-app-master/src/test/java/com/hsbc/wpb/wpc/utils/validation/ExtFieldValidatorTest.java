package com.dummy.wpb.wpc.utils.validation;


import com.dummy.wpb.wpc.utils.MockUtils;
import com.dummy.wpb.wpc.utils.constant.Field;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
/*@RunWith(PowerMockRunner.class)*/
public class ExtFieldValidatorTest {

    @Mock
    private List<Map<String, Object>> mockMetadataList;

    @Mock(lenient=true)
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;

    private ExtFieldValidator extFieldValidatorUnderTest;

    @Test
    public void testValidate() throws NoSuchFieldException, IllegalAccessException {

        /*Mockito.mockConstruction(HashMap.class,(mock, context) ->{
            when(mock.keySet()).thenReturn(new HashSet(Arrays.asList("a","b")));
        }); */


        mockMetadataList = new ArrayList();
        mockMetadataList.add(new HashMap<String,Object>(){{put("parent","ext");put("attrName","fieldCde");}});
        ExtFieldValidator ext = new ExtFieldValidator(mockMetadataList, mockNamedParameterJdbcTemplate);
        extFieldValidatorUnderTest = spy(ext);

        MockUtils.mockPrivate(extFieldValidatorUnderTest,"namedParameterJdbcTemplate",mockNamedParameterJdbcTemplate);

        MockUtils.mockPrivate(extFieldValidatorUnderTest,"metadataList",mockMetadataList);


        Mockito.doAnswer(invocationOnMock -> {
            ResultSet resultSet = mock(ResultSet.class);
            //Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
            Mockito.when(resultSet.getString(Field.FIELD_CDE)).thenReturn("fieldCde");
            Mockito.when(resultSet.getString(Field.FIELD_DATA_TYPE_TEXT)).thenReturn("test");
            RowCallbackHandler rowCallbackHandler = invocationOnMock.getArgument(2);
            rowCallbackHandler.processRow(resultSet);
            return null;
        }).when(mockNamedParameterJdbcTemplate).query(
                Mockito.anyString(),
                Mockito.any(Map.class),
                Mockito.any(RowCallbackHandler.class)
        );

        //when(mockMetadataList.stream()).thenReturn(spyStream);

        final List<Error> result = extFieldValidatorUnderTest.validate();
    }

}
