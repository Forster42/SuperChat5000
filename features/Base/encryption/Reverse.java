package Base.encryption;

/**
 *
 * @author Max Schiedermeier
 */
public class Reverse implements Encryptor
{

    @Override
    public String encrypt(String message)
    {
        //checking for a collon, since this indicates a username exists, therfore it is not DISCONNECT_REQ / DISCONNECT_ACKF
            //split at fors colon
            char[] choppedMessageContent = message.toCharArray();
            char[] invertedMessageContent = new char[choppedMessageContent.length];
            for (int i = 0; i < choppedMessageContent.length; i++) {
                invertedMessageContent[invertedMessageContent.length - i - 1] = choppedMessageContent[i];
            }
            return new String(invertedMessageContent);
     }

    /**
     * Using a symmetric encryption. Decryption method may internally use
     * encryption method.
     */
    @Override
    public String decrypt(String cypher)
    {
        return encrypt(cypher);
    }
}
