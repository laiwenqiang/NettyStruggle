package com.inaction.chatper02;

/**
 * Created by laiwenqiang on 2018/12/21.
 */
public class ActionMapping {

    public static String getMappingRequest(String beanName) throws Exception {
        return Class.forName("com.inaction.chatper02.bean." + beanName).newInstance().toString();
    }
}
