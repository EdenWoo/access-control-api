package com.cfgglobal.generator.core;

import java.util.Map;

public interface ScriptHelper {
    @SuppressWarnings("unchecked")
    <T> T exec(String express, Map<String, Object> context);
}