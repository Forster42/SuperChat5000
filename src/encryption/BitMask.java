package encryption;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 *
 * @author Max Schiedermeier
 */
public class BitMask implements Encryptor
{

    private byte mask;

   public BitMask(byte mask)
    {
        this.mask = mask;
    }

    @Override
    public String encrypt(String plain)
    {
        try {
            byte[] messageAsBytes = plain.getBytes(Charset.forName("UTF-8"));
            for (int i = 0; i < messageAsBytes.length; i++) {
                messageAsBytes[i] = bitFlip(messageAsBytes[i]);
            }

            return new String(messageAsBytes, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("bit error during encryption :-(");
        }
    }

    /**
     * symmetric algorithm. use encryption method for decryption, too.
     */
    @Override
    public String decrypt(String cypher)
    {
        return encrypt(cypher);
    }

    private byte bitFlip(byte originalByte)
    {
        return (byte) (0xff & (((int) originalByte) ^ ((int) mask)));
    }

}
