package kr.co.polycube.backendtest.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.polycube.backendtest.Exceptions.SpecialCharacterException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Pattern;

@Component
public class SpecialCharacterFilter implements Filter {

    private static final String ACCEPTED_PATTERNS = "^[a-zA-Z0-9-\\?&=:/]+$";
    private static final String H2_CONSOLE = "/h2-console";

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if(request.getRequestURI().startsWith(H2_CONSOLE)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        try {
            String url = request.getRequestURL().toString();
            String query = request.getQueryString();

            if((query != null && !Pattern.matches(ACCEPTED_PATTERNS, query))
                    || !Pattern.matches(ACCEPTED_PATTERNS, url)) {
                throw new SpecialCharacterException("Invalid characters in URL: " + url + query);
            }

            filterChain.doFilter(servletRequest, servletResponse);
        }
        // FilterConfig에서 문제가 생겨서 일단 우회하여 Exception Directly Catch 합니다
        catch (SpecialCharacterException e) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setStatus(400);
            response.getWriter().write("{\"reason\": \"Illegal characters in URL.\"}");
            response.setContentType("application/json");
        }
    }
}
