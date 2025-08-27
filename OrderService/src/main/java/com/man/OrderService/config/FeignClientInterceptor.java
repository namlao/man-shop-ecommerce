//package com.man.OrderService.config;
//
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//
//@Component
//public class FeignClientInterceptor implements RequestInterceptor{
//
//	@Override
//	public void apply(RequestTemplate template) {
//		// TODO Auto-generated method stub
//		String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
//		template.header("Authorization", "Bearer "+token);
//	}
//
//}
