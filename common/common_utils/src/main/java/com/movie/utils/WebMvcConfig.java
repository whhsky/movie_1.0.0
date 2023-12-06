package com.movie.utils;
import com.movie.utils.argumentResolverConfig.UnderlineToCamelArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    // 配置Swagger的Docket的Bean实例
    @Bean
    public Docket docket(Environment environment){
        Profiles profiles = Profiles.of("dev", "test");
        log.info("生成文档");
        // 通过environment.acceptsProfiles是否处在自己设定的环境中
        boolean flag = environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(info())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com"))
                .build();
    }
    private ApiInfo info(){
        return new ApiInfo("电影API文档",
                "暂无~~~",
                "1.0",
                "http://localhost:8000/",
                new Contact("yq", "暂无", "1282928349@qq.com"), // 作者信息
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList()
        );
    }


    /**
     * 处理请求参数
     * @param argumentResolvers
     */
    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new UnderlineToCamelArgumentResolver());
    }

    /**
     * 配置静态资源映射文件
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("静态资源映射开启");
//        String path = System.getProperty("user.dir") + "/test/static/";
        String path = System.getProperty("user.dir") + "\\static\\";
        log.info("静态资源路径："+path);
        registry.addResourceHandler("/static/**").addResourceLocations(path,"file:"+path);
        // 静态资源访问路径和存放路径配置
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        // swagger访问配置
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/","classpath:/META-INF/resources/webjars/");

//        // 上传文件路径配置
//        registry.addResourceHandler("/static/upload/**").addResourceLocations("file:"+path);
    }


//    @Override
//    protected void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowCredentials(true) // 是否发送 Cookie
////                .allowedOriginPatterns("http://121.43.120.223:8080") // 支持域
////                .allowedOriginPatterns("http://localhost:8080") // 支持域
//                .allowedOrigins("*") // 支持域
//                .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE"}) // 支持方法
//                .allowedHeaders("*")
//                .exposedHeaders("*");
//    }
}
