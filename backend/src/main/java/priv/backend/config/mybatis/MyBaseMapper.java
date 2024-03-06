package priv.backend.config.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : Map映射器
 * @CreateDate :  2024-01-31 10:10
 */
public interface MyBaseMapper<T> extends BaseMapper<T> {
    /**
     * 批量插入
     * @param batchList 数据列
     * @return 插入影响行数
     */
    int insertBatchSomeColumn(@Param("list") List<T> batchList);
}
