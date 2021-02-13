package org.thunder.function;

import org.thunder.core.Context;
import org.thunder.core.ProcessingResult;

import java.util.function.BiFunction;

public interface ProcessingFunction<T, R> extends BiFunction<T, Context, ProcessingResult<R>> {

    Class<T> getParameterType();
}
