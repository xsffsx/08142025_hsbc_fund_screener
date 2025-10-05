package com.dummy.wmd.wpc.graphql.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BooleanUtils {

    public static boolean and(final Boolean... array) {
        Objects.requireNonNull(array);
        return Stream.of(array).allMatch(Boolean.TRUE::equals);
    }
}
