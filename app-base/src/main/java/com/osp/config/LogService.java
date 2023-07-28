package com.osp.config;

import com.osp.core.utils.H;
import com.osp.core.utils.UtilsClass;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Slf4j
@Aspect
@Component
public class LogService {

    /*
    * @Before : chạy trược method
    * @After: Chạy trong 2 trường hợp method chạy thành công hay có exception
    * @AfterReturning: Chạy khi method chạy thành công
    * @AfterThrowing: Chạy khi method có exception
    * @Around cũng được sử dụng để chèn đầu và cuối của method
    * */

    @Before("within(com.osp.enpoint.*)")
    public void before(JoinPoint joinPoint){
        log.info(" Before called " + joinPoint.toString());
    }

    @AfterThrowing("within(com.osp.enpoint.*)")
    public void AfterThrowing(JoinPoint joinPoint){
        log.info(" AfterThrowing called " + joinPoint.toString());
    }

    @AfterReturning("within(com.osp.enpoint.*)")
    public void AfterReturning(JoinPoint joinPoint){
        log.info(" AfterReturning called " + joinPoint.toString());
    }

    @After("within(com.osp.enpoint.*)")
    public void after(JoinPoint joinPoint){
        log.info(" After called " + joinPoint.toString());
    }

    @Around("@annotation(com.osp.core.config.TrackLog)") // around by annotation
    // @Around("within(com.osp.enpoint.*)")              // around by package
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String url = joinPoint.getTarget().getClass().getName();
        String method = joinPoint.getSignature().getName();
        Object req = (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) ? joinPoint.getArgs()[joinPoint.getArgs().length - 1] : null;

        Long startTime = System.currentTimeMillis();
        log.info("Around Start Time Taken by {} is {}", joinPoint, startTime);

        Object object = joinPoint.proceed();
        Object resp = (Object) object;

        //get data status in object resp.status
        Field myField = (resp != null) ? resp.getClass().getDeclaredField("status") : null;
        HttpStatus httpStatus = null;
        if (resp != null) {
            myField.setAccessible(true);
            httpStatus = (HttpStatus) myField.get(resp);
        }
        //get status code
        boolean isSuccess = H.isTrue(req) && H.isTrue(resp) && H.isTrue(httpStatus) && httpStatus.value() == HttpStatus.OK.value();

        Long timeTaken = System.currentTimeMillis() - startTime;
        log.info("Around Time Taken by {} is {}", joinPoint, timeTaken);

        return object;
    }

}
