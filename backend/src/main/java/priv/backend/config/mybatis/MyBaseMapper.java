package priv.backend.config.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface MyBaseMapper<T> extends BaseMapper<T> {
    // 批量插入
    int insertBatchSomeColumn(@Param("list") List<T> batchList);
}
