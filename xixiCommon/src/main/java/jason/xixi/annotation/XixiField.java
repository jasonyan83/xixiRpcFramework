package jason.xixi.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) 
public @interface XixiField {
	 /**
     * ��Ϣ���е�����λ��
     * @return
     */
    public abstract int index();

    /**
     * ����Ϣ���е��ֽڳ��ȣ�Ϊ-1ʱ��ȡ�ֶ����͵ĳ���
     * @return
     */
    public abstract int bytes() default -1;
    
    public abstract String customType() default "";
    
    public abstract String description() default "";
}
