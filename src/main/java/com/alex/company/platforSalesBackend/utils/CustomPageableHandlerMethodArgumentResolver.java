package com.alex.company.platforSalesBackend.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CustomPageableHandlerMethodArgumentResolver extends PageableHandlerMethodArgumentResolver {

    @Override
    public @NonNull Pageable resolveArgument(MethodParameter methodParameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String sortParam = request.getParameter("sort");
        if (sortParam != null && sortParam.startsWith("[")) {
            return PageRequest.of(
                    getPageParameter(request),
                    getSizeParameter(request),
                    Sort.by("createdDate").descending()
            );
        }

        return super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
    }
    private int getPageParameter(HttpServletRequest request) {
        String page = request.getParameter("page");
        return page != null ? Integer.parseInt(page) : 0;
    }

    private int getSizeParameter(HttpServletRequest request) {
        String size = request.getParameter("size");
        return size != null ? Integer.parseInt(size) : 10;
    }
}