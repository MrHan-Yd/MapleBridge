package priv.backend.filter.wrapper;// SensitiveWordRequestWrapper
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequestWrapper;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 关键字词过滤请求参数备份包装器
 * @CreateDate :  2024-06-30 21:38
 */
public class SensitiveWordRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public SensitiveWordRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        //保存一份InputStream，将其转换为字节数组
        body = StreamUtils.copyToByteArray(request.getInputStream());
    }

    //转换成String
    public String getBodyString(){
        return new String(body, StandardCharsets.UTF_8);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    //把保存好的InputStream，传下去
    @Override
    public ServletInputStream getInputStream() throws IOException {

        final ByteArrayInputStream bais = new ByteArrayInputStream(body);

        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }


}
