package com.movie.utils.argumentResolverConfig;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参数解析
 */
public class UnderlineToCamelArgumentResolver extends AbstractCustomizeResolver {
    /**
     * 匹配_加任意一个字符
     */
    private static final Pattern UNDER_LINE_PATTERN = Pattern.compile("_(\\w)");


    /**
     * 在参数或方法前面加@ParameterConvertzh注解时进行参数分解
     * @param methodParameter
     * @return boolean
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {

        return methodParameter.hasParameterAnnotation(ParameterConvert.class) || methodParameter.hasMethodAnnotation(ParameterConvert.class);
    }

    /**
     * 执行参数分解的具体方法,返回的Object就是controller方法上的形参对象。
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return Object
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Object org;
        try {
            if("java.lang.String".equals(methodParameter.getParameterType().getName())){
                org = ParameterNames(methodParameter, nativeWebRequest);
            } else if ("java.lang.Integer".equals(methodParameter.getParameterType().getName())) {
                org = Integer.parseInt((String) ParameterNames(methodParameter, nativeWebRequest));
            } else {
                org = handleParameterNames(methodParameter, nativeWebRequest);
                valid(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory, org);
            }
            return org;
        } catch (NullPointerException e) {
            return null;
        }

    }

    /**
     * 参数处理1
     */
    private Object ParameterNames(MethodParameter parameter, NativeWebRequest webRequest) {
        Object org;
        String parameterName = parameter.getParameterName();
        Map<String, String[]> parameterMap = webRequest.getParameterMap();
        String[] parValues = parameterMap.get(translate(parameterName));
        if (parValues.length > 1){
            org = parValues;
        }else {
            org = parValues[0];
        }
        return org;
    }

    /**
     * 处理参数2
     *
     * @param parameter
     * @param webRequest
     * @return
     */
    private Object handleParameterNames(MethodParameter parameter, NativeWebRequest webRequest) {
        Object obj = BeanUtils.instantiate(parameter.getParameterType());
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(obj);
        Iterator<String> paramNames = webRequest.getParameterNames();
        while (paramNames.hasNext()) {
            String paramName = paramNames.next();
            Object o = webRequest.getParameter(paramName);
            System.out.println(paramName + "=" + o);
            wrapper.setPropertyValue(underLineToCamel(paramName), o);
        }
        return obj;
    }

    public static boolean isInterface(Class<?> clazz) {
        return clazz.isInterface();
    }

    /**
                Map<String, String[]> parameterMap = nativeWebRequest.getParameterMap();
            for (Map.Entry<String, String[]> map : parameterMap.entrySet()) {
                String key = map.getKey();
                String[] value = map.getValue();
                parameterMap.remove(key);
                parameterMap.put(underLineToCamel(key), value);
            }
            Parameter[] parameters = methodParameter.getExecutable().getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Class<?> pars = parameters[i].getClass();
                Field field = pars.getDeclaredField("name");
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                field.setAccessible(true);
                field.set(parameters[i], translate(parameters[i].getName()));
            for (Map.Entry<String, String[]> map : parameterMap.entrySet()) {
                String key = map.getKey();
                String[] value = map.getValue();
                parameterMap.remove(key);
                parameterMap.put(underLineToCamel(key), value);
            }
     **/

    /**
     * 下换线转驼峰
     *
     * @param source
     * @return
     */
    private String underLineToCamel(String source) {
        Matcher matcher = UNDER_LINE_PATTERN.matcher(source);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * 驼峰转下划线
     * @param input
     * @return String
     */
    public static String translate(String input) {
        if (input == null) {
            return input;
        } else {
            int length = input.length();
            StringBuilder result = new StringBuilder(length * 2);
            int resultLength = 0;
            boolean wasPrevTranslated = false;

            for(int i = 0; i < length; ++i) {
                char c = input.charAt(i);
                if (i > 0 || c != '_') {
                    if (Character.isUpperCase(c)) {
                        if (!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '_') {
                            result.append('_');
                            ++resultLength;
                        }

                        c = Character.toLowerCase(c);
                        wasPrevTranslated = true;
                    } else {
                        wasPrevTranslated = false;
                    }

                    result.append(c);
                    ++resultLength;
                }
            }

            return resultLength > 0 ? result.toString() : input;
        }
    }

}