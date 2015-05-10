package encryption;

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
        if (message.contains(":")) {
            //split at fors colon
            String[] messageSplice = message.split(":", 2);
            char[] choppedMessageContent = messageSplice[1].toCharArray();
            char[] invertedMessageContent = new char[choppedMessageContent.length];
            for (int i = 0; i < choppedMessageContent.length; i++) {
                invertedMessageContent[invertedMessageContent.length - i - 1] = choppedMessageContent[i];
            }
            return messageSplice[0] + ":" + new String(invertedMessageContent);
        }
        return message;
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
