package io.github.eternalstone.attachment.algorithm;

/**
 * 加密算法接口
 *
 * @author Justzone on 2022/9/4 15:52
 */
public interface ICipherAlgorithm {

    /**
     * 加密方法
     * @param value 明文
     * @return 返回加密后的密文
     */
    String encrypt(String value);

    /**
     * 解密方法
     * @param value 密文
     * @return 返回解密后的明文, 如果算法不可逆，直接返回密文
     */
    String decrypt(String value);


}
