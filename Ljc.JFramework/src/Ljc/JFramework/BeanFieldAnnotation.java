package Ljc.JFramework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanFieldAnnotation {
	/**
	 * ��ע�����Ե�˳��
	 * 
	 * @return �����Ե�˳��
	 */
	int order();

}
