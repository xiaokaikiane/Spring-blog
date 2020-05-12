package HMX.config;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class ControllerInterceptor {

    @ExceptionHandler(Exception.class)
    public ModelAndView handler(Exception e){
        StringWriter stringWriter=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        ModelAndView mv=new ModelAndView();
        mv.addObject("message",e.getMessage());
        mv.addObject("stackTrace",stringWriter.toString());
        mv.setViewName("error");
        return mv;
    }
}
