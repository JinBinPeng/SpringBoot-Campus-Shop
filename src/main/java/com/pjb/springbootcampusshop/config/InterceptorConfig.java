package com.pjb.springbootcampusshop.config;

import com.pjb.springbootcampusshop.interceptor.shop.ShopLoginInterceptor;
import com.pjb.springbootcampusshop.interceptor.shop.ShopPermissionInterceptor;
import com.pjb.springbootcampusshop.interceptor.superadmin.SuperAdminLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author jinbin
 * @date 2019-01-16 18:14
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(shopLoginInterceptor())
                .excludePathPatterns("/shop/ownerlogin","/shop/ownerlogincheck","/shop/logout","/shop/register")
                .addPathPatterns("/shop/**");    // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
        registry.addInterceptor(shopPermissionInterceptor())
                .excludePathPatterns("/shop/ownerlogin","/shop/ownerlogincheck","/shop/register")
                .excludePathPatterns("/shop/shoplist","/shop/logout","/shop/list")
                .excludePathPatterns("/shop/changepsw","/shop/changelocalpwd")
                .excludePathPatterns("/shop/ownerbind","/shop/bindlocalauth")
                .excludePathPatterns("/shop/shopmanage","/shop/shopedit","/shop/getshopbyid","/shop/getshopinitinfo","/shop/registershop")
                .addPathPatterns("/shop/**");
        registry.addInterceptor(superAdminLoginInterceptor())
                .excludePathPatterns("/superadmin/login","/superadmin/logincheck","/superadmin/main","/superadmin/top")
                .addPathPatterns("/superadmin/**");
    }
    @Bean
    public ShopLoginInterceptor shopLoginInterceptor(){
        return new ShopLoginInterceptor();
    }
    @Bean
    public ShopPermissionInterceptor shopPermissionInterceptor(){
        return new ShopPermissionInterceptor();
    }
    @Bean
    public SuperAdminLoginInterceptor superAdminLoginInterceptor(){
        return new SuperAdminLoginInterceptor();
    }

}
