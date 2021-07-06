package com.yyh.util;

import com.yyh.annotation.CustomService;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Objects;

public class ServiceRegister {

    private static final Class serviceAnnotationClass = CustomService.class;

    // 빈을 찾아서 등록 합니다.
    public static void findService(String packageName) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            throw new ClassNotFoundException("Failed to get the class loader.");

        }

        Enumeration<URL> resources = classLoader.getResources(packageName.replace('.', '/'));
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            URLConnection urlConnection = url.openConnection();

            if (urlConnection instanceof FileURLConnection) {
                File packageDict = new File(URLDecoder.decode(url.getPath(), "UTF-8"));
                recursiveFindServiceInDirectory(packageDict, packageName);

            }
        }
    }

    // 재귀적으로 들어가 빈을 등록합니다.
    private static void recursiveFindServiceInDirectory(File dict, String packageName) throws Exception {

        String[] files = dict.list();
        for (String file : files) {
            if (file.endsWith(".class")) {
                Class<?> classObject = Class.forName(packageName + '.' + file.substring(0, file.length() - 6));
                Annotation interfaceList = classObject.getAnnotation(serviceAnnotationClass);

                if (Objects.nonNull(interfaceList)) {
                    CustomBeanRegistry.getInstance().registerSingleton(classObject);
                }
            } else {
                File tempDirectory = new File(dict, file);
                if (tempDirectory.isDirectory()) {
                    recursiveFindServiceInDirectory(tempDirectory, packageName + "." + file);
                }
            }
        }

    }
}
