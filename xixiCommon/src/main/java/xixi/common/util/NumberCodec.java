/**
 * 
 */
package xixi.common.util;

/**
 * @author isdom
 *
 */
public interface NumberCodec {
    public String convertCharset(String charset);
    public byte[] short2Bytes(short value, int byteLength);
    public byte[] int2Bytes(int value, int byteLength);
    public byte[] long2Bytes(long value, int byteLength);
    public short  bytes2Short(byte[] bytes, int byteLength);
    public int    bytes2Int(byte[] bytes, int byteLength);
    public long   bytes2Long(byte[] bytes, int byteLength);
}
