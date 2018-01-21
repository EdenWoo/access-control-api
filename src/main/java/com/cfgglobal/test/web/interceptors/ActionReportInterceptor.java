package com.cfgglobal.test.web.interceptors;

import com.cfgglobal.test.config.ActionReportProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

@Component
@EnableConfigurationProperties(ActionReportProperties.class)
public class ActionReportInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<SimpleDateFormat> sdf = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    private static Writer writer = new SystemOutWriter();
    @Autowired
    private ActionReportProperties actionReportProperties;

    private int maxOutputLengthOfParaValue = 512;

    private String fileName(Class clazz) {
        String controllerFile = System.getProperty("user.dir") + File.separator + actionReportProperties.getModule() + File.separator + "src";

        if (actionReportProperties.isMaven()) {
            controllerFile += File.separator + "main" + File.separator + "java";
        }

        for (String temp : clazz.getName().split("\\.")) {
            controllerFile = controllerFile + File.separator + temp;
        }
        return controllerFile + ".java";
    }

    private int lineNum(String codeFragment, String fileName) {
        List<String> lines;
        int lineNum = 1;
        Path path = Paths.get(fileName);
        try {
            lines = Files.readAllLines(path, Charset.forName("UTF-8"));
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (StringUtils.isNotBlank(line) && StringUtils.deleteWhitespace(line).contains(codeFragment)) {
                    lineNum = i + 1;
                    break;
                }
            }
        } catch (NoSuchFileException ignored) {
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return lineNum;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if (!actionReportProperties.isSwitcher()) {
            return true;
        }
        if (!(o instanceof HandlerMethod)) {
            return true;
        }
        report(request, (HandlerMethod) o);

        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
    }

    public final void report(HttpServletRequest request, HandlerMethod handlerMethod) {
        StringBuilder sb = new StringBuilder("\nSpring MVC controller report -------- ").append(sdf.get().format(new Date())).append(" ------------------------------\n");
        String str = handlerMethod.getReturnType().getMethod().toGenericString();
        String[] arr = str.split(" ");
        String search = arr[0] + StringUtils.substringAfterLast(arr[1], ".") + handlerMethod.getMethod().getName();
        Class cc = handlerMethod.getBeanType();
        sb.append("URL         :").append(request.getRequestURI()).append("\n");
        sb.append("Controller  : ").append(cc.getName()).append(".(").append(cc.getSimpleName()).append(".java:")
                .append(lineNum(search, fileName(cc))).append(")");
        sb.append("\nMethod      : ").append(handlerMethod.getMethod().getName()).append("\n");
        Enumeration<String> e = request.getParameterNames();
        if (e.hasMoreElements()) {
            sb.append("Parameter   : ");
            while (e.hasMoreElements()) {
                String name = e.nextElement();
                String[] values = request.getParameterValues(name);
                if (values.length == 1) {
                    sb.append(name).append("=");

                    if (values[0] != null && values[0].length() > maxOutputLengthOfParaValue) {
                        sb.append(values[0].substring(0, maxOutputLengthOfParaValue)).append("...");
                    } else {
                        sb.append(values[0]);
                    }
                } else {
                    sb.append(name).append("[]={");
                    for (int i = 0; i < values.length; i++) {
                        if (i > 0)
                            sb.append(",");
                        sb.append(values[i]);
                    }
                    sb.append("}");
                }
                sb.append("  ");
            }
            sb.append("\n");
        }
        sb.append("--------------------------------------------------------------------------------\n");

        try {
            writer.write(sb.toString());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static class SystemOutWriter extends Writer {
        public void write(String str) throws IOException {
            System.out.print(str);
        }

        public void write(char[] cbuf, int off, int len) throws IOException {
        }

        public void flush() throws IOException {
        }

        public void close() throws IOException {
        }
    }
}
