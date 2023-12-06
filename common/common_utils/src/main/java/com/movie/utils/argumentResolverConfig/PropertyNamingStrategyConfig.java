package com.movie.utils.argumentResolverConfig;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * 返回参数转转型
 */
public class PropertyNamingStrategyConfig implements Serializable {
    public static final PropertyNamingStrategy SNAKE_CASE = new PropertyNamingStrategy.SnakeCaseStrategy();
    public static final PropertyNamingStrategy UPPER_CAMEL_CASE = new PropertyNamingStrategy.UpperCamelCaseStrategy();
    public static final PropertyNamingStrategy LOWER_CAMEL_CASE = new PropertyNamingStrategy();
    public static final PropertyNamingStrategy LOWER_CASE = new PropertyNamingStrategy.LowerCaseStrategy();
    public static final PropertyNamingStrategy KEBAB_CASE = new PropertyNamingStrategy.KebabCaseStrategy();
    //依照  PropertyNamingStrategy.SnakeCaseStrategy()  新增数字方法
    public static final PropertyNamingStrategy NUM_CASE = new NumCaseStrategy();

    /** @deprecated */
    @Deprecated
    public static final PropertyNamingStrategy CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES;
    /** @deprecated */
    @Deprecated
    public static final PropertyNamingStrategy PASCAL_CASE_TO_CAMEL_CASE;

    public PropertyNamingStrategyConfig() {
    }

    public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
        return defaultName;
    }

    public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        return defaultName;
    }

    public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        return defaultName;
    }

    public String nameForConstructorParameter(MapperConfig<?> config, AnnotatedParameter ctorParam, String defaultName) {
        return defaultName;
    }

    static {
        CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES = SNAKE_CASE;
        PASCAL_CASE_TO_CAMEL_CASE = UPPER_CAMEL_CASE;
    }

    /** @deprecated */
    @Deprecated
    public static class PascalCaseStrategy extends PropertyNamingStrategy.UpperCamelCaseStrategy {
        public PascalCaseStrategy() {
        }
    }

    /** @deprecated */
    @Deprecated
    public static class LowerCaseWithUnderscoresStrategy extends PropertyNamingStrategy.SnakeCaseStrategy {
        public LowerCaseWithUnderscoresStrategy() {
        }
    }

    public static class KebabCaseStrategy extends PropertyNamingStrategy.PropertyNamingStrategyBase {
        public KebabCaseStrategy() {
        }

        public String translate(String input) {
            if (input == null) {
                return input;
            } else {
                int length = input.length();
                if (length == 0) {
                    return input;
                } else {
                    StringBuilder result = new StringBuilder(length + (length >> 1));
                    int upperCount = 0;

                    for(int i = 0; i < length; ++i) {
                        char ch = input.charAt(i);
                        char lc = Character.toLowerCase(ch);
                        if (lc == ch) {
                            if (upperCount > 1) {
                                result.insert(result.length() - 1, '-');
                            }

                            upperCount = 0;
                        } else {
                            if (upperCount == 0 && i > 0) {
                                result.append('-');
                            }

                            ++upperCount;
                        }

                        result.append(lc);
                    }

                    return result.toString();
                }
            }
        }
    }

    public static class LowerCaseStrategy extends PropertyNamingStrategy.PropertyNamingStrategyBase {
        public LowerCaseStrategy() {
        }

        public String translate(String input) {
            return input.toLowerCase();
        }
    }

    public static class UpperCamelCaseStrategy extends PropertyNamingStrategy.PropertyNamingStrategyBase {
        public UpperCamelCaseStrategy() {
        }

        public String translate(String input) {
            if (input != null && input.length() != 0) {
                char c = input.charAt(0);
                char uc = Character.toUpperCase(c);
                if (c == uc) {
                    return input;
                } else {
                    StringBuilder sb = new StringBuilder(input);
                    sb.setCharAt(0, uc);
                    return sb.toString();
                }
            } else {
                return input;
            }
        }
    }

    public static class SnakeCaseStrategy extends PropertyNamingStrategy.PropertyNamingStrategyBase {
        public SnakeCaseStrategy() {
        }

        public String translate(String input) {
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

    public abstract static class PropertyNamingStrategyBase extends PropertyNamingStrategy {
        public PropertyNamingStrategyBase() {
        }

        public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
            return this.translate(defaultName);
        }

        public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
            return this.translate(defaultName);
        }

        public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
            return this.translate(defaultName);
        }

        public String nameForConstructorParameter(MapperConfig<?> config, AnnotatedParameter ctorParam, String defaultName) {
            return this.translate(defaultName);
        }

        public abstract String translate(String var1);
    }


    public static class NumCaseStrategy extends PropertyNamingStrategy.SnakeCaseStrategy {
        public NumCaseStrategy() {
        }

        public String translate(String input) {
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
                        } else if(!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '_' && Pattern.compile("^-?\\d+(\\.\\d+)?$").matcher(String.valueOf(c)).matches() && !Pattern.compile("^-?\\d+(\\.\\d+)?$").matcher(String.valueOf(input.charAt(i-1))).matches() ){
                            //新增数字判断
                            //  Pattern.compile("^-?\\d+(\\.\\d+)?$").matcher(String.valueOf(c)).matches() && !Pattern.compile("^-?\\d+(\\.\\d+)?$").matcher(String.valueOf(input.charAt(i-1))).matches()
                            //  表示 用正则表达式判断 当前位是数字 且 前一位不是数字
                            result.append('_');
                            ++resultLength;
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

}
