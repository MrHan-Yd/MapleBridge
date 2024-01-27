package priv.backend.domain;

import priv.backend.domain.dto.StatusRole;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 拷贝用户实体工具接口(相同属性)
 * @CreateDate :  2023-09-11 15:56
 */
public interface BaseData {


    default <V> V asViewObject(Class<V> clazz, Consumer<V> consumer) {
        V v = this.asViewObject(clazz) ;
        consumer.accept(v) ;
        return v ;
    }

    /** TODO: Written by - Han Yongding 2023/08/12 对象转换 */
    default <V> V asViewObject(Class<V> clazz) {
        try {
            /* TODO: Written by - Han Yongding 2023/08/12 拿到所有已声明的字段 */
            Field[] declaredFields = clazz.getDeclaredFields() ;
            /* TODO: Written by - Han Yongding 2023/08/12 获取构造函数 */
            Constructor<V> constructors = clazz.getConstructor();
//            Constructor<V> constructors = clazz.getDeclaredConstructor();

            V v = constructors.newInstance() ;

            /* TODO: Written by - Han Yongding 2023/08/12 转换 */
            for (Field declaredField : declaredFields) {
                convert(declaredField, v) ;
            }
            return v ;

        } catch(ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage()) ;
        }
    }

    private void convert(Field field, Object vo) {
        try {
            Field source = this.getClass().getDeclaredField(field.getName()) ;
            field.setAccessible(true) ;
            source.setAccessible(true) ;
            field.set(vo, source.get(this)) ;
        } catch(NoSuchFieldException | IllegalAccessException e) {
        }
    }

}
