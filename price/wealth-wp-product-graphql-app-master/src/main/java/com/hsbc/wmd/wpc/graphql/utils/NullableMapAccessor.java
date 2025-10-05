package com.dummy.wmd.wpc.graphql.utils;

import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.TypedValue;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Allow not exists attribute to be read as null from a Map, the original MapAccessor will raise an exception for the case.
 * For example, when evaluate below spel against an empty Map object, we can get the result 'false' instead of an exception.
 *     wlthAccumGoalInd=='Y' or planForRtireGoalInd=='Y' or educGoalInd=='Y' or liveInRtireGoalInd=='Y' or protcGoalInd=='Y'
 */
public class NullableMapAccessor extends MapAccessor {
    @Override
    public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
        return target instanceof Map;
    }

    @Override
    public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
        Assert.state(target instanceof Map, "Target must be of type Map");
        Map<?, ?> map = (Map<?, ?>) target;
        Object value = map.get(name);
        return new TypedValue(value);
    }
}
