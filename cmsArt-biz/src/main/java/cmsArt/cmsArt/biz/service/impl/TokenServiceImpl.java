package cmsArt.cmsArt.biz.service.impl;

import cmsArt.cmsArt.biz.service.TokenService;
import cmsArt.cmsArt.biz.util.Digests;
import cmsArt.cmsArt.biz.util.Encodes;
import cmsArt.cmsArt.model.annotation.LogTime;
import cmsArt.cmsArt.model.constants.AppConstants;
import cmsArt.cmsArt.model.vo.Token;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author kangyonggan
 * @since 2017/1/19
 */
@Service
public class TokenServiceImpl extends BaseService<Token> implements TokenService {

    @Override
    @LogTime
    public String saveToken(String type, Long userId) {
        Token token = new Token();
        token.setType(type);
        token.setUserId(userId);

        String code = genTokenCode();
        token.setCode(code);
        token.setExpireTime(new Date(new Date().getTime() + 2 * 60 * 60 * 1000));// 2小时

        super.insertSelective(token);
        return code;
    }

    @Override
    @LogTime
    public Token findTokenByCode(String code) {
        Token token = new Token();
        token.setCode(code);

        return super.selectOne(token);
    }

    @Override
    @LogTime
    public void updateToken(Token token) {
        super.updateByPrimaryKeySelective(token);
    }

    private String genTokenCode() {
        byte[] hashKey = Digests.sha1(Digests.generateSalt(AppConstants.SALT_SIZE));
        return Encodes.encodeHex(hashKey);
    }
}
