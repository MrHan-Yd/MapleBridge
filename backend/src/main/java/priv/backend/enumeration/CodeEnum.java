package priv.backend.enumeration;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 状态码枚举类
 * @CreateDate :  2023-11-01 10:33
 */
public enum CodeEnum {
    /* TODO: Written by - Han Yongding 2023/11/01 1xx临时响应，表示临时响应并需要请求者继续执行操作的状态 */
    /* 100(继续)： 请求这应当继续提出请求，服务器返回此状态码表示已收到请求的第一部分，正在等待其余部分 */
    HTTP_100_CONTINUE_REQUEST(100, "已收到请求的第一部分，正在等待其余部分") ,
    /* 101(切换协议)： 请求者已要求服务器切换协议，服务器已确认并准备切换 */
    HTTP_101_SWITCH_PROTOCOL(101, "服务器已确认，准备切换") ,
    /* TODO: Written by - Han Yongding 2023/11/01 2xx成功，表示成功处理了请求的状态 */
    /* 200(请求成功)： 请求被服务器正常处理 */
    HTTP_200_REQUEST_SUCCESSFUL(200, "请求被服务器正常处理") ,
    /* 201(创建好新的资源)： 请求成功且服务器已经创建好新的资源 */
    HTTP_201_REQUEST_SUCCESSFUL_SERVER_NEW_RESOURCE(201, "请求成功，已创建好新的资源") ,
    /* 202(接收请求，但尚未处理)： 服务器接收请求，但还未进行处理 */
    HTTP_202_REQUEST_SUCCESSFUL_UNPROCESSED(202, "请求已接收，但尚为处理") ,
    /* 203(返回了其他来源的信息)： 服务器成功处理请求，但可能返回了其他来源的信息 */
    HTTP_203_REQUEST_SUCCESSFUL_NON_AUTHORITATIVE_INFORMATION(203, "服务器已处理，可能会返回其他来源的信息") ,
    /* 204(没有内容返回)： 服务器正常处理，但是没有内容返回*/
    HTTP_204_REQUEST_SUCCESSFUL_NO_CONTENT_RETURNED(204, "服务器正常处理，没有内容返回") ,
    /* 205(重置内容)： 服务器成功处理了请求，但未返回任何内容。与204响应不同，此响应要求请求者重置文档视图(例如清除表单内容，以输入新内容) */
    HTTP_205_REQUEST_SUCCESSFUL_RESET_CONTENT(205, "服务器已处理请求，请重置文档视图") ,
    /* 206(完成部分请求)： 服务器完成了部分get请求 */
    HTTP_206_REQUEST_SUCCESSFUL_PARTIAL_GET_REQUESTS(206, "服务器完成了部分GET请求") ,
    /* TODO: Written by - Han Yongding 2023/11/01 3xx重定向，表示要完成请求，需要进一步操作 */
    /* 300(服务器提供多种选择) */
    HTTP_300_SERVER_PROVIDE_MULTIPLE_OPTIONS(300, "服务器提供多种选择") ,
    /* 301(永久重定向) */
    HTTP_301_PERMANENT_REDIRECT(301, "永久重定向") ,
    /* 302(临时重定向) */
    HTTP_302_TEMPORARY_REDIRECT(302, "临时重定向") ,
    /* TODO: Written by - Han Yongding 2023/11/02 4xx错误请求 */
    /* 400(错误请求)： 请求的语法错误，服务器无法理解 */
    HTTP_400_ERROR_REQUEST(400, "无效请求，可能是参数不正确或请求语法错误，服务器无法理解") ,
    /* 401(未授权)： 访问指定资源必须通过服务器的授权 */
    HTTP_401_UNAUTHORIZED(401, "未授权") ,
    /* 403(禁止访问)：不允许访问某些资源 */
    HTTP_403_PROHIBIT_ACCESS(403, "禁止访问") ,
    /* 404(找不到资源)： 找不到客户端请求的资源 */
    HTTP_404_RESOURCE_NOT_FOUND(404, "找不到资源") ,
    /* TODO: Written by - Han Yongding 2023/11/02 5xx服务器内部错误 */
    /* 500(服务器错误) */
    HTTP_500_INTERNAL_SERVER_ERROR(500, "服务器内部错误，请联系管理员")
    ;

    public Integer CODE;

    public String MESSAGE;

    CodeEnum(Integer code, String message) {
        this.CODE = code;
        this.MESSAGE = message;
    }

    CodeEnum() {
    }

    public Integer getCODE() {
        return CODE;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }
}
