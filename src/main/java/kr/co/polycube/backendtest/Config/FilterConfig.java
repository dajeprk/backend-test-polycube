package kr.co.polycube.backendtest.Config;

import kr.co.polycube.backendtest.Component.SpecialCharacterFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {

    @Bean // 필터 활성화 DispatcherServlet 보다 우선순위 올리기
    public FilterRegistrationBean<SpecialCharacterFilter> filterRegistrationbean() {
        FilterRegistrationBean<SpecialCharacterFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SpecialCharacterFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}
