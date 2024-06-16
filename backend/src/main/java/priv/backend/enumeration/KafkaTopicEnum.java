package priv.backend.enumeration;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : Kafka生产者枚举
 * @CreateDate :  2024-05-07 21:21
 */
public enum KafkaTopicEnum {
    COLLECT_USER_PREFERENCES("collect_user_preferences"),
    ANALYZE_AND_PERSIST_USER_PREFERENCES("analyze_and_persist_user_preferences"),
    WEBSITE_TRAFFIC_LOGS("website_traffic_logs"),
    LOGIN_LOGS("login_logs"),
    REQUEST_LOGS("request_logs"),
    COLLECT_GROUP("collect-group"),
    PERSISTENCE_GROUP("persistence-group"),
    WEBSITE_TRAFFIC_GROUP("website-traffic-group"),
    LOGIN_LOG_GROUP("login-log-group"),
    REQUEST_LOG_GROUP("request-log-group");

    public final String topic ;

    KafkaTopicEnum(String topic){
        this.topic = topic;
    }
}
