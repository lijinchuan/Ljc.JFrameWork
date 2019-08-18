package Ljc.JFramework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanFieldAnnotation {
	/**
	 * 标注该属性的顺序
	 * 
	 * @return 该属性的顺序
	 */
	int order();

}
