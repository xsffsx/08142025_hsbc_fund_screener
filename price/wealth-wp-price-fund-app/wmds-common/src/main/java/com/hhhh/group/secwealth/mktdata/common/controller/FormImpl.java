package com.hhhh.group.secwealth.mktdata.common.controller;

import java.util.Map;

import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;

public final class FormImpl implements IForm {

    private static final char SPLIT_CHAR = '|';

    private Map<String, String[]> requestDatas;

    public FormImpl(final Map<String, String[]> map) {
        this.requestDatas = map;
    }

    public int getSize() {
        if (null != this.requestDatas) {
            return this.requestDatas.size();
        } else {
            return 0;
        }
    }

    public String getAction() {
        return getString("method", FormImpl.SPLIT_CHAR);
    }

    public String get(final String parameterName) {
        return getString(parameterName, FormImpl.SPLIT_CHAR);
    }

    public String getString(final String parameterName) {
        return get(parameterName);
    }

    public String getString(final String parameterName, final char split) {
        String value = "";

        try {
            String[] parameter = getStrings(parameterName);
            for (int i = 0; i < parameter.length; i++) {
                value += parameter[i] + split;
            }
        } catch (Exception e) {
            LogUtil.error(FormImpl.class, "error:" + parameterName + "-->" + e.toString(), e);
        }

        if (0 != value.length()) {
            value = value.substring(0, value.length() - 1);
        }

        return value;
    }

    public String[] getStrings(final String parameterName) {
        try {
            String[] parameter = (String[]) this.requestDatas.get(parameterName);
            if (null != parameter) {
                return parameter;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            LogUtil.error(FormImpl.class, "SystemException: FormImpl, getStrings, Param: " + parameterName, e);
            return new String[0];
        }
    }

    /**
     * 
     * @param parameterName
     * @return int
     */
    public int getInt(final String parameterName) {
        String value = this.get(parameterName);

        if (-1 == value.indexOf(FormImpl.SPLIT_CHAR)) {
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {
                LogUtil.error(FormImpl.class, "SystemException: FormImpl, getInt, Param: " + value, e);
                return 0;
            }
        } else {
            LogUtil.error(FormImpl.class, "SystemException: FormImpl, getInt, Param: " + value);
            return 0;
        }
    }

    public int[] getInts(final String parameterName) {
        try {
            String[] parameter = (String[]) this.requestDatas.get(parameterName);
            if (null != parameter) {
                int[] value = new int[parameter.length];
                for (int i = 0; i < parameter.length; i++) {
                    value[i] = Integer.parseInt(parameter[i]);
                }

                return value;
            } else {
                return new int[0];
            }
        } catch (Exception e) {
            LogUtil.error(FormImpl.class, "SystemException: FormImpl, getInts, Param: " + parameterName, e);
            return new int[0];
        }
    }

    /**
     * @param parameterName
     * @return Integer
     */
    public Integer getInteger(final String parameterName) {
        String value = this.get(parameterName);

        if (-1 == value.indexOf(FormImpl.SPLIT_CHAR)) {
            try {
                return new Integer(value);
            } catch (Exception e) {
                LogUtil.error(FormImpl.class, "SystemException: FormImpl, getInteger, Param: " + parameterName, e);
                return new Integer(0);
            }
        } else {
            LogUtil.error(FormImpl.class, "SystemException: FormImpl, getInteger, Param: " + parameterName);
            return new Integer(0);
        }
    }

    /**
     * @param parameterName
     * @return Integer[]
     */
    public Integer[] getIntegers(final String parameterName) {
        try {
            String[] parameter = (String[]) this.requestDatas.get(parameterName);
            if (null != parameter) {
                Integer[] value = new Integer[parameter.length];
                for (int i = 0; i < parameter.length; i++) {
                    value[i] = new Integer(parameter[i]);
                }

                return value;
            } else {
                return new Integer[0];
            }
        } catch (Exception e) {
            LogUtil.error(FormImpl.class, "SystemException: FormImpl, getIntegers, Param: " + parameterName, e);
            return new Integer[0];
        }
    }

    /**
     * @param parameterName
     * @return long
     */
    public long getlong(final String parameterName) {
        String value = this.get(parameterName);

        if (-1 == value.indexOf(FormImpl.SPLIT_CHAR)) {
            try {
                return Long.parseLong(value);
            } catch (Exception e) {
                LogUtil.error(FormImpl.class, "SystemException: FormImpl, getlong, Param: " + parameterName, e);
                return 0;
            }
        } else {
            LogUtil.error(FormImpl.class, "SystemException: FormImpl, getlong, Param: " + parameterName);
            return 0;
        }
    }

    /**
     * @param parameterName
     * @return long[]
     */
    public long[] getlongs(final String parameterName) {
        try {
            String[] parameter = (String[]) this.requestDatas.get(parameterName);
            if (null != parameter) {
                long[] value = new long[parameter.length];
                for (int i = 0; i < parameter.length; i++) {
                    value[i] = Long.parseLong(parameter[i]);
                }

                return value;
            } else {
                return new long[0];
            }
        } catch (Exception e) {
            LogUtil.error(FormImpl.class, "SystemException: FormImpl, getlongs, Param: " + parameterName, e);
            return new long[0];
        }
    }

    /**
     * @param parameterName
     * @return Long
     */
    public Long getLong(final String parameterName) {
        return new Long(this.getlong(parameterName));
    }

    /**
     * @param parameterName
     * @return Long[]
     */
    public Long[] getLongs(final String parameterName) {
        try {
            String[] parameter = (String[]) this.requestDatas.get(parameterName);
            if (null != parameter) {
                Long[] value = new Long[parameter.length];
                for (int i = 0; i < parameter.length; i++) {
                    value[i] = new Long(parameter[i]);
                }

                return value;
            } else {
                return new Long[0];
            }
        } catch (Exception e) {
            LogUtil.error(FormImpl.class, "SystemException: FormImpl, getLongs, Param: " + parameterName, e);
            return new Long[0];
        }
    }

    /**
     * @param parameterName
     * @return float
     */
    public float getfloat(final String parameterName) {
        String value = this.get(parameterName);

        if (-1 == value.indexOf(FormImpl.SPLIT_CHAR)) {
            try {
                return Float.parseFloat(value);
            } catch (Exception e) {
                LogUtil.error(FormImpl.class, "SystemException: FormImpl, getfloat, Param: " + parameterName, e);
                return 0;
            }
        } else {
            LogUtil.error(FormImpl.class, "SystemException: FormImpl, getfloat, Param: " + parameterName);
            return 0;
        }
    }

    /**
     * @param parameterName
     * @return float[]
     */
    public float[] getfloats(final String parameterName) {
        try {
            String[] parameter = (String[]) this.requestDatas.get(parameterName);
            if (null != parameter) {
                float[] value = new float[parameter.length];
                for (int i = 0; i < parameter.length; i++) {
                    value[i] = Float.parseFloat(parameter[i]);
                }

                return value;
            } else {
                return new float[0];
            }
        } catch (Exception e) {
            LogUtil.error(FormImpl.class, "SystemException: FormImpl, getfloats, Param: " + parameterName, e);
            return new float[0];
        }
    }

    /**
     * @param parameterName
     * @return Float
     */
    public Float getFloat(final String parameterName) {
        return new Float(getfloat(parameterName));
    }

    /**
     * @param parameterName
     * @return Float[]
     */
    public Float[] getFloats(final String parameterName) {
        try {
            String[] parameter = (String[]) this.requestDatas.get(parameterName);
            if (null != parameter) {
                Float[] value = new Float[parameter.length];
                for (int i = 0; i < parameter.length; i++) {
                    value[i] = new Float(parameter[i]);
                }

                return value;
            } else {
                return new Float[0];
            }
        } catch (Exception e) {
            LogUtil.error(FormImpl.class, "SystemException: FormImpl, getFloats, Param: " + parameterName, e);
            return new Float[0];
        }
    }

    /**
     * @param parameterName
     * @return double
     */
    public double getdouble(final String parameterName) {
        String value = this.get(parameterName);

        if (-1 == value.indexOf(FormImpl.SPLIT_CHAR)) {
            try {
                return Double.parseDouble(value);
            } catch (Exception e) {
                LogUtil.error(FormImpl.class, "SystemException: FormImpl, getdouble, Param: " + parameterName, e);
                return 0;
            }
        } else {
            LogUtil.error(FormImpl.class, "SystemException: FormImpl, getdouble, Param: " + parameterName);
            return 0;
        }
    }

    /**
     * @param parameterName
     * @return double[]
     */
    public double[] getdoubles(final String parameterName) {
        try {
            String[] parameter = (String[]) this.requestDatas.get(parameterName);
            if (null != parameter) {
                double[] value = new double[parameter.length];
                for (int i = 0; i < parameter.length; i++) {
                    value[i] = Double.parseDouble(parameter[i]);
                }

                return value;
            } else {
                return new double[0];
            }
        } catch (Exception e) {
            LogUtil.error(FormImpl.class, "SystemException: FormImpl, getdoubles, Param: " + parameterName, e);
            return new double[0];
        }
    }

    /**
     * @param parameterName
     * @return Double
     */
    public Double getDouble(final String parameterName) {
        return new Double(getdouble(parameterName));
    }

    /**
     * @param parameterName
     * @return Double[]
     */
    public Double[] getDoubles(final String parameterName) {
        try {
            String[] parameter = (String[]) this.requestDatas.get(parameterName);
            if (null != parameter) {
                Double[] value = new Double[parameter.length];
                for (int i = 0; i < parameter.length; i++) {
                    value[i] = new Double(parameter[i]);
                }

                return value;
            } else {
                return new Double[0];
            }
        } catch (Exception e) {
            LogUtil.error(FormImpl.class, "SystemException: FormImpl, getDoubles, Param: " + parameterName, e);
            return new Double[0];
        }
    }
}
