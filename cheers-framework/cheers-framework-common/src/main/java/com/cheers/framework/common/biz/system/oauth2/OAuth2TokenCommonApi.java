package com.cheers.framework.common.biz.system.oauth2;

/**
 * OAuth2.0 Token API 接口
 */
public interface OAuth2TokenCommonApi {

    /**
     * 获得登录用户
     *
     * @param accessToken 访问令牌
     * @return 登录用户
     */
    LoginUser getLoginUser(String accessToken);

    /**
     * 校验访问令牌
     *
     * @param accessToken 访问令牌
     * @return 访问令牌的用户编号
     */
    Long checkAccessToken(String accessToken);

    /**
     * 移除访问令牌
     *
     * @param accessToken 访问令牌
     */
    void removeAccessToken(String accessToken);

} 