package com.example.demo.maker;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class StubValues {

    public static final String STUB_STRING = "string";

    public static final Long STUB_LONG = 0L;

    public static final Integer STUB_INT = 0;

    public static final OffsetDateTime STUB_OFFSET_DATE_TIME = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC);

    public static final LocalDate STUB_LOCAL_DATE = LocalDate.now();

    public static final Double STUB_DOUBLE = 0.0;

    public static final Short STUB_YEAR = 2022;
}
