package me.freelife;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.PushBuilder;
import java.io.IOException;

public class PushBuilderFilter extends Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        PushBuilder pushBuilder = servletRequest.newPushBuilder();
        //wrapping
        chain.doFilter(request, response);
        // TODO: HTML 응답 본문 가져오기
        // TODO: 추가로 가져올 리소스 분석
        // TODO: PushBuilder로 보내기
    }
}