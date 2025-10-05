package com.hhhh.group.secwealth.mktdata.common.controller;


public interface IForm {
    public int getSize();

    public String getAction();

    public String get(String parameterName);

    public String getString(String parameterName);

    public String[] getStrings(String parameterName);

    public String getString(String parameterName, char split);

    public int getInt(String parameterName);

    public int[] getInts(String parameterName);

    public Integer getInteger(String parameterName);

    public Integer[] getIntegers(String parameterName);

    public long getlong(String parameterName);

    public long[] getlongs(String parameterName);

    public Long getLong(String parameterName);

    public Long[] getLongs(String parameterName);

    public float getfloat(String parameterName);

    public float[] getfloats(String parameterName);

    public Float getFloat(String parameterName);

    public Float[] getFloats(String parameterName);

    public double getdouble(String parameterName);

    public double[] getdoubles(String parameterName);

    public Double getDouble(String parameterName);

    public Double[] getDoubles(String parameterName);
}
