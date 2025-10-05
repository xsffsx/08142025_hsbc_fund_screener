package com.dummy.wmd.wpc.graphql.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class JavascriptEngineTest {

    @Mock
    private Bindings bindings;
    @Mock
    private ScriptEngine engine;
    @InjectMocks
    private JavascriptEngine javascriptEngine;

    @Test
    public void testEval_givenStringAndMap_returnsObject() throws ScriptException {
        ReflectionTestUtils.setField(javascriptEngine, "engine", engine);
        // Setup
        Map<String, Object> context = new HashMap<>();
        Object object = new Object();
        Mockito.when(engine.createBindings()).thenReturn(bindings);
        Mockito.when(engine.eval(any(String.class), any(Bindings.class))).thenReturn(object);
        // Run the test
        Object result = javascriptEngine.eval("script", context);
        // Verify the results
        assertNotNull(result);
    }
    @Test(expected = RuntimeException.class)
    public void testEval_givenStringAndMap_throwsException() throws ScriptException {
        ReflectionTestUtils.setField(javascriptEngine, "engine", engine);
        // Setup
        Map<String, Object> context = new HashMap<>();
        Mockito.when(engine.createBindings()).thenReturn(bindings);
        Mockito.when(engine.eval(any(String.class), any(Bindings.class))).thenThrow(ScriptException.class);
        // Run the test
        javascriptEngine.eval("script", context);
    }

}
