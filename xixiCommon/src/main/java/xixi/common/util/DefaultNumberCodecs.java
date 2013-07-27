/**
 * 
 */
package xixi.common.util;




/**
 * @author isdom
 *
 */
public class DefaultNumberCodecs {
    
    private static int b2ui(byte b) {
        return (int)(b + 256) % 256;
    }
    
    private static long b2ul(byte b) {
        return (long)(b + 256) % 256;
    }

    private static NumberCodec littleEndianCodec = new NumberCodec() {

        public int bytes2Int(byte[] bytes, int byteLength) {
            int value = 0;
            for (int i = 0; i < byteLength; i++ ) {
                value |= b2ui( bytes[i] ) << (i * 8);
            }
            return  value;
        }

        public long bytes2Long(byte[] bytes, int byteLength) {
            long    value = 0;
            for (int i = 0; i < byteLength; i++ ) {
                value |= b2ul( bytes[i] ) << (i * 8);
            }
            
            return  value;
        }

        public short bytes2Short(byte[] bytes, int byteLength) {
            short   value = 0;
            for (int i = 0; i < byteLength; i++ ) {
                value |= b2ui( bytes[i] ) << (i * 8);
            }
            
            return  value;
        }

        public byte[] int2Bytes(int value, int byteLength) {
            byte[] bytes = new byte[byteLength];
            
            for (int i = 0; i < byteLength; i++ ) {
                int shiftCount = i * 8;
                bytes[i] = (byte)( ( value & ( 0x000000ff << shiftCount ) ) >> shiftCount );
            }
            return  bytes;
        }

        public byte[] long2Bytes(long value, int byteLength) {
            byte[] bytes = new byte[byteLength];
            
            for (int i = 0; i < byteLength; i++ ) {
                int shiftCount = i * 8;
                bytes[i] = (byte)( ( value & ( 0x00000000000000ffL << shiftCount ) ) >> shiftCount );
            }
            return  bytes;
        }

        public byte[] short2Bytes(short value, int byteLength) {
            byte[] bytes = new byte[byteLength];
            
            for (int i = 0; i < byteLength; i++ ) {
                int shiftCount = i * 8;
                bytes[i] = (byte)( ( value & ( 0x00ff << shiftCount ) ) >> shiftCount );
            }
            return  bytes;
        }

        public String convertCharset(String charset) {
            if ( charset.equals("UTF-16") ) {
                return  "UTF-16LE";
            }
            else {
                return charset;
            }
        }
    };
    
    private static NumberCodec bigEndianCodec = new NumberCodec() {

        public int bytes2Int(byte[] bytes, int byteLength) {
            int value = 0;
            for (int i = 0; i < byteLength; i++ ) {
                value |= b2ui( bytes[i] ) << ((byteLength -1 -i) * 8);
            }
            return  value;
        }

        public long bytes2Long(byte[] bytes, int byteLength) {
            long    value = 0;
            for (int i = 0; i < byteLength; i++ ) {
                value |= b2ul( bytes[i] ) << ((byteLength -1 -i) * 8);
            }
            
            return  value;
        }

        public short bytes2Short(byte[] bytes, int byteLength) {
            short   value = 0;
            for (int i = 0; i < byteLength; i++ ) {
                value |= b2ui( bytes[i] ) << ((byteLength -1 -i) * 8);
            }
            
            return  value;
        }

        public byte[] int2Bytes(int value, int byteLength) {
            byte[] bytes = new byte[byteLength];
            
            for (int i = 0; i < byteLength; i++ ) {
                int shiftCount = ((byteLength -1 -i) * 8);
                bytes[i] = (byte)( ( value & ( 0x000000ff << shiftCount ) ) >> shiftCount );
            }
            return  bytes;
        }

        public byte[] long2Bytes(long value, int byteLength) {
            byte[] bytes = new byte[byteLength];
            
            for (int i = 0; i < byteLength; i++ ) {
                int shiftCount = ((byteLength -1 -i) * 8);
                bytes[i] = (byte)( ( value & ( 0x00000000000000ffL << shiftCount ) ) >> shiftCount );
            }
            return  bytes;
        }

        public byte[] short2Bytes(short value, int byteLength) {
            byte[] bytes = new byte[byteLength];
            
            for (int i = 0; i < byteLength; i++ ) {
                int shiftCount = ((byteLength -1 -i) * 8);
                bytes[i] = (byte)( ( value & ( 0x00ff << shiftCount ) ) >> shiftCount );
            }
            return  bytes;
        }
        
        public String convertCharset(String charset) {
            if ( charset.equals("UTF-16") ) {
                return  "UTF-16BE";
            }
            else {
                return charset;
            }
        }
    };
    
    public static NumberCodec byteOrder2NumberCodec(ByteOrder byteOrder) {
        if ( ByteOrder.BigEndian == byteOrder ) {
            return  getBigEndianNumberCodec();
        }
        else if ( ByteOrder.LittleEndian == byteOrder ) {
            return  getLittleEndianNumberCodec();
        }
        else {
            throw new RuntimeException("byteOrder2NumberCodec: invalid byteOrder.");
        }
    }
    
    public static NumberCodec getBigEndianNumberCodec() {
        return  bigEndianCodec;
    }
    
    public static NumberCodec getLittleEndianNumberCodec() {
        return  littleEndianCodec;
    }
}
