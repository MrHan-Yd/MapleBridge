package priv.backend.enumeration;

import lombok.Data;
import lombok.Getter;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应常用消息
 * @CreateDate :  2024-07-01 1:27
 */
@Getter
public enum RespMessageEnum {
    SUCCESS("请求成功"),
    FAILED("请求失败"),
    PARAM_ERROR("参数错误"),
    PARAM_NOT_NULL("参数不能为空"),
    NOT_FOUND("未找到资源"),
    UNAUTHORIZED("未授权"),
    FORBIDDEN("无权限"),
    SERVER_ERROR("服务器内部错误"),
    DATA_EXIST("数据已存在"),
    DATA_NOT_EXIST("数据不存在"),
    DATA_UPDATE_FAILED("数据更新失败"),
    DATA_DELETE_FAILED("数据删除失败"),
    DATA_INSERT_FAILED("数据新增失败"),
    DATA_QUERY_FAILED("数据查询失败"),
    DATA_FORMAT_ERROR("数据格式错误"),
    DATA_DUPLICATE("数据重复"),
    DATA_NOT_MATCH("数据不匹配"),
    DATA_TOO_LONG("数据过长"),
    DATA_TOO_SHORT("数据过短"),
    DATA_TOO_BIG("数据过大"),
    DATA_TOO_SMALL("数据过小");

    private final String message ;

    RespMessageEnum(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }

}
