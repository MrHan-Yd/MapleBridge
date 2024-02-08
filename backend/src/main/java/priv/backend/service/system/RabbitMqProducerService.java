package priv.backend.service.system;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : RabbitMQ生产者
 * @CreateDate :  2024-02-03 22:14
 */
public interface RabbitMqProducerService {
    String sendEmailVerifyCode(String type, String email, String ip) ;

}
