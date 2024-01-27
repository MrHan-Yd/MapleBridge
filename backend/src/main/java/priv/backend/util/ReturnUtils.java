package priv.backend.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.RestBean;
import priv.backend.enumeration.CodeEnum;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 控制器返回值为空时简化代码
 * @CreateDate :  2024-01-22 20:12
 */
public class ReturnUtils {
    /**
     * 处理带返回消息的操作，根据操作结果生成 RestBean。
     *
     * @param vo       要处理的数据对象。
     * @param function 用于从数据对象中提取消息的函数。
     * @param <T>      数据对象的类型。
     * @return 如果消息为 null，则返回成功的 RestBean；否则返回包含错误消息的 RestBean。
     */
    public static <T> RestBean<Void> messageHandle(T vo, Function<T, String> function) {
        return messageHandle(() -> function.apply(vo));
    }

    /**
     * 处理带返回消息的操作，根据操作结果生成 RestBean。
     *
     * @param action 函数式接口，用于执行具体的操作并返回消息。
     * @return 如果消息为 null，则返回成功的 RestBean；否则返回包含错误消息的 RestBean。
     */
    public static RestBean<Void> messageHandle(Supplier<String> action) {
        /* TODO: Written by - Han Yongding 2023/08/14 具体要调用的方法 */
        String message = action.get();
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }


    /**
     * 处理返回数据的工具方法，根据返回的对象类型判断是否为空，如果为空则返回未查询到数据的提示。
     *
     * @param action 供应商函数，用于执行具体的查询或操作。
     * @param <T>    返回的数据类型。
     * @return 如果查询结果为空，则返回未查询到数据的提示；否则返回成功的 RestBean 包装。
     */
    public static <T> RestBean<T> messageHandleData(Supplier<T> action) {
        /* TODO: Written by - Han Yongding 2024/01/23 具体要调用的方法 */
        T returnRs = action.get();

        if (returnRs instanceof Page<?> page) {
            // 如果返回的是 Page 对象，判断 records 是否为空
            if (page.getRecords().isEmpty()) {
                return RestBean.failure(CodeEnum.HTTP_400_ERROR_REQUEST.CODE, "未查询到数据");
            }
        } else if (returnRs instanceof Collection<?> collection) {
            // 如果返回的是 Collection，判断是否为空
            if (collection.isEmpty()) {
                return RestBean.failure(CodeEnum.HTTP_400_ERROR_REQUEST.CODE, "未查询到数据");
            }
        } else if (returnRs == null) {
            // 如果返回为空，也返回未查询到数据
            return RestBean.failure(CodeEnum.HTTP_400_ERROR_REQUEST.CODE, "未查询到数据");
        }

        return RestBean.success(returnRs);
    }
}
