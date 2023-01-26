package com.example.demo.responseAdvice.advice;

import com.example.demo.responseAdvice.response.ErrorWrapper;
import com.example.demo.responseAdvice.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

/**
 * Используется для обертки response в {@link ResponseWrapper}.
 */
//@RestControllerAdvice
public class PILResponseWrapperAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        return null;
    }

    /**
     * Пакет с логикой контроллеров (пример ru.atc.fgislk.pil.forest.stead.controller)
     */
//    @Value("${application.baseControllerPackage}")
//    private String baseControllerPackage;
//
//    @Override
//    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
//        return returnType.getContainingClass().getPackageName().contains(baseControllerPackage);
//    }
//
//    @Override
//    public Object beforeBodyWrite(Object body,
//                                  MethodParameter returnType,
//                                  MediaType selectedContentType,
//                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
//                                  ServerHttpRequest request,
//                                  ServerHttpResponse response) {
//
//        if (exceptionHandled(returnType.getContainingClass())) {
//            ErrorMessageDto errorMessage = (ErrorMessageDto) body;
//            String errorDetails = errorMessage.getError() + " " + errorMessage.getStatus() + " " + errorMessage.getPath();
//            ErrorWrapper errorWrapper = new ErrorWrapper(errorMessage.getCode(), errorMessage.getMsg(), errorDetails);
//            return ResponseWrapper.wrap(null, List.of(errorWrapper), null);
//        }
//        return ResponseWrapper.wrap(body);
//    }

    /**
     * Обработчики ошибок наследуются от общего класса {@link CommonExceptionHandlerControllerAdvicer}.
     * Здесь идет проверка был ли задействован данный обработчик при отправке сообщения.
     *
     * @param handlerMethodClass Класс обработчика ошибок
     * @return True - если была обработана ошибка
     */
//    private boolean exceptionHandled(Class<?> handlerMethodClass) {
//        return CommonExceptionHandlerControllerAdvicer.class.isAssignableFrom(handlerMethodClass);
//    }
}
