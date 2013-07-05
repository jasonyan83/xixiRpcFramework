/**
 * 
 */
package xixi.register.predicate;

import org.apache.commons.collections.Predicate;

/**
 * @author hp
 *
 */
public class TypeAcceptor implements Predicate {


	/* (non-Javadoc)
	 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
	 */
	public boolean evaluate(Object object) {
		if ( null == object ) {
			return	false;
		}
		
		/*if(DefaultPropertiesSupport.class.isAssignableFrom(object.getClass())){
			return true;
		}*/
		
		return	false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		//return "TypeAcceptor [allowTypes=" + DefaultPropertiesSupport.class.getName() + "]";
		return null;
	}

}
