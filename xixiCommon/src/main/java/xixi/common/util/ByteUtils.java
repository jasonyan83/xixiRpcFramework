/**
 * 
 */
package xixi.common.util;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author hp
 *
 */
public class ByteUtils {
    private ByteUtils() {}
    
    public static String bytesAsHexString(byte[] bytes, int maxShowBytes) {
        int idx = 0;
        StringBuilder  body = new StringBuilder();
        body.append("bytes size is:[");
        body.append(bytes.length);
        body.append("]\r\n");
        
        for ( byte b : bytes) {
            int hex = ((int)b) & 0xff;
            String shex = Integer.toHexString(hex).toUpperCase();
            if ( 1 == shex.length() ) {
                body.append( "0" );
            }
            body.append( shex );
            body.append( " " );
            idx++;
            if ( 16 == idx ) {
                body.append("\r\n");
                idx = 0;
            }
            maxShowBytes--;
            if ( maxShowBytes <= 0 ) {
                break;
            }
        }
        if ( idx != 0 ) {
            body.append("\r\n");
        }
        return  body.toString();
    }

    /**
     * ��byte����ת����ÿ����ʾһ��TLVֵ��Hex��ʽ�ַ�,�������ڷ��TLV�淶��BigEndian bytes
     * @param bytes TLV bytes
     * @param maxShowBytes �����ʾ�ֽ���
     * @return
     */
    public static String bytesAsTLVHexString(byte[] bytes, int maxShowBytes) {
        StringBuilder  body = new StringBuilder();
        body.append("bytes size is:[");
        body.append(bytes.length);
        body.append("]\r\n");

        NumberCodec numberCodec = DefaultNumberCodecs.getBigEndianNumberCodec();
        while (bytes.length > 8 && maxShowBytes > 0) {
            int l = numberCodec.bytes2Int(ArrayUtils.subarray(bytes, 4, 8), 4);
            for (byte b : ArrayUtils.subarray(bytes, 0, 8+l)) {
                int hex = ((int) b) & 0xff;
                String shex = Integer.toHexString(hex).toUpperCase();
                if (1 == shex.length()) {
                    body.append("0");
                }
                body.append(shex);
                body.append(" ");

                maxShowBytes--;
                if (maxShowBytes <= 0) {
                    break;
                }
            }
            body.append("\r\n");
            bytes = ArrayUtils.subarray(bytes, 8+l, bytes.length);
        }

        return  body.toString();
    }
    
    public static byte[] union(List<byte[]> byteList) {
    	int size = 0;
    	for ( byte[] bs : byteList ) {
    		size += bs.length;
    	}
    	byte[] ret = new byte[size];
    	int pos = 0;
    	for ( byte[] bs : byteList ) {
    		System.arraycopy(bs, 0, ret, pos, bs.length);
    		pos += bs.length;
    	}
    	return	ret;
    }
    
    public static int totalByteSizeOf(List<byte[]> byteList) {
        int len = 0;
        for ( byte[] bs : byteList ) {
        	len += bs.length;
        }
        
        return	len;
    }
    
    public static byte[] string2BCD(String src) {
    	byte[] ret = new byte[(src.length() + 1)/ 2];
    	
    	int	charIdx = 0;
    	for ( int idx = 0; idx < ret.length; idx++) {
    		byte Htemp = (byte)(src.charAt(charIdx) - '0');
    		Htemp = (byte)(Htemp << 4);
    		Htemp = (byte)(Htemp & 0xf0);
    		charIdx++;
    		byte Ltemp = (byte)(src.charAt(charIdx) - '0');
    		Ltemp = (byte)(Ltemp & 0x0f);
    		ret[idx] = (byte)(Htemp | Ltemp);
    		charIdx++;
    	}
    	
    	return	ret;
    }
    
    public static void main(String[] args) {
    	System.out.println( 
    		ToStringBuilder.reflectionToString(string2BCD("460028691110438" + "0"), 
    			ToStringStyle.SHORT_PREFIX_STYLE) );
    }
}
