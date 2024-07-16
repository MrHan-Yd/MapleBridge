package priv.backend.filter;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import priv.backend.domain.RestBean;
import priv.backend.filter.wrapper.SensitiveWordRequestWrapper;
import priv.backend.service.system.SensitiveWordService;
import priv.backend.util.Const;
import priv.backend.util.LogUtils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author weiguang
 * @Description 敏感词过滤器
 * @CreateDate 2024-06-30 13:17
 */
@Component
@Order(Const.ORDER_SENSITIVE_WORDS)
public class SensitiveWordFilter extends HttpFilter {

    @Resource
    private SensitiveWordService service;

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws ServletException, IOException {

        // 获取敏感词列表
        Map<String, String> map = service.getSensitiveWords();
        // 判断请求内容类型是否为 JSON
        boolean isJsonContentType = request.getContentType() != null && request.getContentType().startsWith("application/json");

        // 根据请求内容类型处理参数
        if (isJsonContentType) {
            /* TODO: Written by - Han Yongding 2024/06/30 请求参数备份，从输入流中读取参数后，控制器后续会获取不到参数 */
            SensitiveWordRequestWrapper requestWrapper = new SensitiveWordRequestWrapper(request);
            // 处理 JSON 请求体
            for (String str : this.extractRequestBody(requestWrapper.getBodyString())) {
                if (containsSensitiveWord(map, str)) {
                    handleSensitiveRequest(response);
                    return;
                }
            }
            // 没有敏感词，放行
            chain.doFilter(requestWrapper, response);
        } else {
            String contentType = request.getContentType();

            if (contentType != null && contentType.startsWith("multipart/form-data")) {
                // 处理表单数据
                Collection<Part> parts = request.getParts();
                for (Part part : parts) {
                    // 获取表单数据参数
//                    String paramName = part.getName();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream()));
                    StringBuilder paramValueBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        paramValueBuilder.append(line);
                    }
                    String paramValue = paramValueBuilder.toString();
                    // 使用 paramName 和 paramValue 进行处理
                    if (containsSensitiveWord(map, paramValue)) {
                        handleSensitiveRequest(response);
                        return;
                    }
                }
            } else {
                // 处理普通请求参数
                Map<String, String[]> parameterMap = request.getParameterMap();
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
//                    String paramName = entry.getKey();
                    String[] paramValues = entry.getValue();
                    // 对于每个参数名和参数值，进行处理
                    for (String paramValue : paramValues) {
                        // 使用 paramName 和 paramValue 进行处理
                        if (containsSensitiveWord(map, paramValue)) {
                            handleSensitiveRequest(response);
                            return;
                        }
                    }
                }
            }
            // 没有敏感词，放行
            chain.doFilter(request, response);
        }

    }

    // 判断是否有敏感词
    private boolean containsSensitiveWord(Map<String, String> map, String content) {
        if (ObjectUtils.isEmpty(content)) {
            return false;
        }
        if (ObjectUtils.isEmpty(map)) {
            return false;
        }
        return map.containsKey(content.toUpperCase());
    }

    // 处理含有敏感词的请求
    private void handleSensitiveRequest(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(RestBean.forbidden("参数中含敏感词，拒绝请求").asJsonString());
        } catch (IOException e) {
            LogUtils.error(this.getClass(), e.getMessage());
        }
    }

    // 提取请求体内容
    private List<String> extractRequestBody(String jsonString) {
        try {
            JSONObject json = JSONObject.parseObject(jsonString);
            List<String> requestBody = new java.util.ArrayList<>();
            json.forEach((key, value) -> {
                requestBody.add(value.toString());
            });
            return requestBody;
        } catch (JSONException e) {
            return Collections.singletonList(jsonString);
        }
    }
}
