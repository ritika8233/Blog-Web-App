package com.example.registration.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@Component
public class MyJwtRequestFilter extends OncePerRequestFilter{
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("helooo first filter");
		// TODO Auto-generated method stub
		Cookie token = WebUtils.getCookie(request, "token");
		String authorizationHeader = "";
		if (token != null) {
			authorizationHeader = ("Bearer " + token.getValue());
			response.setHeader("Authorization", authorizationHeader);
		}
		filterChain.doFilter(request, response);
	}
}
