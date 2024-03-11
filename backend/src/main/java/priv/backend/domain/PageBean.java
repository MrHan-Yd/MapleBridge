package priv.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 分页常用属性
 * @CreateDate :  2024-03-09 17:58
 */
@Data
public class PageBean {
    private Integer pageNum;
    private Integer pageSize;
    private Boolean isItPaginated;

    // 默认构造函数
    public PageBean() {
        this.pageNum = 1;
        this.pageSize = 10;
        this.isItPaginated = true;
    }

    public PageBean(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.isItPaginated = true ;
    }

    public PageBean(Boolean isItPaginated) {
        this.pageNum = 1;
        this.pageSize = 10;
        this.isItPaginated = (isItPaginated != null) ? isItPaginated : true;
    }

    public PageBean(Integer pageNum, Integer pageSize, Boolean isItPaginated) {
        this.pageNum = (pageNum != null) ? pageNum : 1; // 设置默认值为 1
        this.pageSize = (pageSize != null) ? pageSize : 10; // 设置默认值为 10
        this.isItPaginated = (isItPaginated != null) ? isItPaginated : true; // 设置默认值为 true
    }

}
