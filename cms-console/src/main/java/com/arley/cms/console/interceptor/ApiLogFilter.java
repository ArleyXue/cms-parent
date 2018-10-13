package com.arley.cms.console.interceptor;


import com.arley.cms.console.util.CommonUtils;
import com.arley.cms.console.util.RequestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.*;

/**
 * @author XueXianlei
 * @Description: 校验签名过滤器 同一返回数据
 * @date 2018/9/13 18:19
 */
public class ApiLogFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(ApiLogFilter.class);
    private PathMatcher pathMatcher;
    private List<String> excludeUrlPatterns;

    public void setExcludeUrlPatterns(String excludeUrlPatterns) {
        if (StringUtils.isNotBlank(excludeUrlPatterns)) {
            this.excludeUrlPatterns = new ArrayList<>(Arrays.asList(excludeUrlPatterns.split(",")));
            this.pathMatcher = new AntPathMatcher();
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String contextPath = request.getContextPath();
        if (isExclude(request.getRequestURI().replace(contextPath, ""))) {
            chain.doFilter(request, response);
            return;
        }
        RequestWrapper requestWrapper = new RequestWrapper(request);
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        request.setAttribute("inTime", System.currentTimeMillis());
        chain.doFilter(requestWrapper, responseWrapper);
        write(requestWrapper, responseWrapper);
    }

    /**
     * 响应
     * @param request
     * @param response
     * @throws IOException
     */
    private void write(RequestWrapper request, ResponseWrapper response) throws IOException {
        String message = new String(response.getDataStream());
        String url = request.getRequestURI();
        String ip = RequestUtils.getClientIpAddr(request);
        Long startTime = (Long) request.getAttribute("inTime");
        long time = System.currentTimeMillis() - startTime;
        // 输出接口访问日志
        logger.info("【接口访问日志】请求路径={} | 来源IP={} | 请求参数={} | 响应参数={} | 处理时间={}ms",
                url,
                ip,
                RequestUtils.getAllRequestParam(request),
                message,
                time);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(message);
    }

    private class ListEnumeration<T> implements Enumeration<T> {
        private int current;
        private List<T> list;

        public ListEnumeration(List<T> list) {
            this.list = list;
        }

        @Override
        public boolean hasMoreElements() {
            return current < list.size();
        }

        @Override
        public T nextElement() {
            return list.get(current++);
        }
    }


    /**
     * 重写request
     */
    public class RequestWrapper extends HttpServletRequestWrapper {
        private byte[] data;
        private ByteArrayInputStream input;
        private FilterServletInputStream inputStream;
        private Map<String, String> headers;

        public RequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            data = IOUtils.toByteArray(request.getInputStream());
            input = new ByteArrayInputStream(data);
            headers = new HashMap<>();
        }

        @Override
        public ServletInputStream getInputStream() {
            if (inputStream == null) {
                inputStream = new FilterServletInputStream(input);
            }
            return this.inputStream;
        }

        public void setHeader(String name, String value) {
            headers.put(name.toLowerCase(), value);
        }

        @Override
        public String getHeader(String name) {
            String value = headers.get(name.toLowerCase());
            if (value == null) {
                return super.getHeader(name);
            }
            return value;
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            String value = getHeader(name);
            if (StringUtils.isEmpty(value)) {
                return super.getHeaders(name);
            } else {
                return new ListEnumeration<>(Collections.singletonList(value));
            }
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> list = new ArrayList<>();
            for (String key : headers.keySet()) {
                list.add(key);
            }
            Enumeration<String> enumeration = super.getHeaderNames();
            while (enumeration.hasMoreElements()) {
                String name = enumeration.nextElement();
                if (!headers.containsKey(name.toLowerCase())) {
                    list.add(name);
                }
            }
            return new ListEnumeration<>(list);
        }

        public byte[] getDataStream() {
            return data;
        }
    }

    /**
     * 重写response
     */
    public class ResponseWrapper extends HttpServletResponseWrapper {
        ByteArrayOutputStream output;
        FilterServletOutputStream filterOutput;
        //总是输出200
        int status = 200;

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
            output = new ByteArrayOutputStream();
            super.setStatus(status);
        }

        @Override
        public void resetBuffer() {
            if (!super.isCommitted()) {
                super.resetBuffer();
            }
            output = new ByteArrayOutputStream();
            filterOutput = new FilterServletOutputStream(output);
        }

        @Override
        public void setStatus(int sc) {
            //禁止设定status，避免重定向到网页
            super.setStatus(sc);
            this.status = sc;
        }

        @Override
        public int getStatus() {
            return status;
        }

        @Override
        public void sendError(int sc) throws IOException {
            //禁止重定向
            setStatus(sc);
            // super.sendError(sc);
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            //禁止重定向
            setStatus(sc);
            // super.sendError(sc, msg);
        }

        @Override
        public void sendRedirect(String location) throws IOException {
            //禁止重定向
            //setStatus();
            super.sendRedirect(location);
        }

        @Override
        public ServletOutputStream getOutputStream() {
            if (filterOutput == null) {
                filterOutput = new FilterServletOutputStream(output);
            }
            return filterOutput;
        }

        public byte[] getDataStream() {
            return output.toByteArray();
        }

    }

    private class FilterServletInputStream extends ServletInputStream {
        private DataInputStream inputStream;

        public FilterServletInputStream(InputStream inputStream) {
            this.inputStream = new DataInputStream(inputStream);
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        /**
         * 兼容Servlet API 3.0
         * @return
         */
        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener arg0) {

        }
    }

    /**
     *  重写 ServletOutputStream
     */
    public class FilterServletOutputStream extends ServletOutputStream {
        DataOutputStream output;

        public FilterServletOutputStream(OutputStream output) {
            this.output = new DataOutputStream(output);
        }

        @Override
        public void write(int arg0) throws IOException {
            output.write(arg0);
        }



        /**
         * 兼容Servlet API 3.0
         * @return
         */
        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener arg0) {

        }
    }


    /**
     * 是否排除此路径
     * @param url
     * @return
     */
    private boolean isExclude (String url) {
        boolean isExclude = false;
        if (CommonUtils.isNotEmptyCollection(this.excludeUrlPatterns)) {
            for (String pattern : this.excludeUrlPatterns) {
                if (this.pathMatcher.match(pattern, url)) {
                    isExclude = true;
                    break;
                }
            }
        }
        return isExclude;
    }
}