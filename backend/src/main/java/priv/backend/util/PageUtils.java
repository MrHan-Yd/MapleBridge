package priv.backend.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用于处理转换 MyBatis Plus Page 对象的实用工具类。
 * @CreateDate :  2024-01-23 14:57
 */

public class PageUtils {
    /**
     * 将 MyBatis Plus Page 对象转换为另一种具有不同泛型类型的 Page 对象。
     *
     * @param sourcePage 源 Page 对象。
     * @param records    目标 Page 的记录列表。
     * @param <T>        Page 的目标类型。
     * @return 具有指定类型和记录的新 Page 对象。
     */
    public static <T> Page<T> convertToPage(Page<?> sourcePage, List<T> records) {
        Page<T> targetPage = new Page<>();
        targetPage.setRecords(records);
        targetPage.setTotal(sourcePage.getTotal());
        targetPage.setSize(sourcePage.getSize());
        targetPage.setCurrent(sourcePage.getCurrent());
        targetPage.setPages(sourcePage.getPages());
        return targetPage;
    }
}
