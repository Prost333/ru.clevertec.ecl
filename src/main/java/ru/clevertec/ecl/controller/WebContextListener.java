package ru.clevertec.ecl.controller;


import java.util.List;
import javax.servlet.annotation.WebListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@WebListener
public class WebContextListener implements WebMvcConfigurer {


}