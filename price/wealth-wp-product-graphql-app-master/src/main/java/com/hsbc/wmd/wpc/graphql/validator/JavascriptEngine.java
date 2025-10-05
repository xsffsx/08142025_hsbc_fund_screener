package com.dummy.wmd.wpc.graphql.validator;

import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JavascriptEngine {
    private static ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    private static ScriptEngine engine = scriptEngineManager.getEngineByName("nashorn");

    public static Object eval(String script, Map<String, Object> context) {
        Bindings bindings = engine.createBindings();
        bindings.putAll(context);

        try {
            return engine.eval(script, bindings);
        }catch(ScriptException e){
            StringBuilder sb = new StringBuilder();
            sb.append(e.getMessage());
            sb.append("script: ").append(script);
            sb.append("context: ").append(context);
            throw new productErrorException(productErrors.RuntimeException, "Error running javascript: " + sb);
        }
    }
}
