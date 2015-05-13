package Base.encryption;

/**
 * Dummy encryption (encrypted cypher = plain message)
 *
 * @author Max Schiedermeier
 */
public class Identity implements Encryptor
{

    @Override
    public String encrypt(String plain)
    {
        return plain;
    }

    @Override
    public String decrypt(String cypher)
    {
        return cypher;
    }

}
