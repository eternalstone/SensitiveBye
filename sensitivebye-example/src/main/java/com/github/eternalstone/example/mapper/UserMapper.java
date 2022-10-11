package com.github.eternalstone.example.mapper;

import com.github.eternalstone.annotation.EnableCipher;
import com.github.eternalstone.enums.CipherType;
import com.github.eternalstone.example.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * to do something
 *
 * @author Justzone on 2022/10/10 10:34
 */
@Mapper
public interface UserMapper {

    @EnableCipher(parameter = CipherType.ENCRYPT)
    int insertAndReturnId(User user);

    @EnableCipher(parameter = CipherType.ENCRYPT)
    int insert(User user);

    @EnableCipher(parameter = CipherType.ENCRYPT)
    int insertBatch(@Param("list") List<User> users);

    @EnableCipher(parameter = CipherType.ENCRYPT)
    int updateById(User user);

    @EnableCipher(result = CipherType.DECRYPT)
    User selectById(@Param("id") Integer id);

    @EnableCipher(result = CipherType.DECRYPT)
    List<User> selectList(@Param("list") List<Integer> ids);

    @EnableCipher(parameter = CipherType.ENCRYPT, result = CipherType.DECRYPT)
    User selectByUser(User user);

}
