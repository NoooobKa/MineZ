package com.NBK.MineZ.util;

import java.lang.reflect.Field;

public final class NMSUtil {

	public static Object getPrivateField(String fieldName, Class<?> clazz, Object object){
        Field field;
        Object o = null;
        try
        {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);
        }
        catch(NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return o;
    }
	
}
