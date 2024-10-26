package com.app.progTrack.security;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomRedirectFilter extends GenericFilterBean{
	// 「doFilter」メソッドはリクエストとレスポンスを処理し次のフィルタまたはリソースに渡す
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain  chain) throws IOException, ServletException {
		
		// リクエストとレスポンスのキャスト これによりHTTP特有のメソッドを使用できるようにする
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		
		// リクエストURIが「/」であり、ユーザーがログインしている場合「/studytime」へリダイレクトさせる
		if (httpRequest.getRequestURI().equals("/") && SecurityContextHolder.getContext().getAuthentication() != null) {
			httpResponse.sendRedirect("/studytime");
			return;
		}
		
		chain.doFilter(request, response);
	}

}
