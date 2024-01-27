package priv.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import priv.backend.domain.vo.response.RespCreateJwtVO;
import priv.backend.domain.vo.response.RespRefreshTokenVO;
import org.springframework.security.core.GrantedAuthority;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : JWt工具类
 * @CreateDate :  2024-01-09 22:18
 */
@Log4j2
@Component
public class JwtUtils {
    /** TODO: Written by - Han Yongding 2023/09/11 加密Key */
    @Value("${spring.security.jwt.key}")
    private String key ;

    /** TODO: Written by - Han Yongding 2023/09/11 Jwt令牌过期时间 */
    @Value("${spring.security.jwt.expire}")
    private int expire ;

    /** TODO: Written by - Han Yongding 2023/10/20 刷新令牌过期时间 */
    @Value("${spring.security.jwt.refresh_expire}")
    private int refreshExpire ;

    /** TODO: Written by - Han Yongding 2023/09/11 注入RedisString依赖 */
    @Resource
    private StringRedisTemplate template ;

    /** TODO: Written by - Han Yongding 2023/09/11 创建Jwt令牌 */
    public String createJwt(UserDetails details, String id, String username, String refreshToken) {
        /* TODO: Written by - Han Yongding 2023/09/11 创建令牌并返回 */
        return JWT.create()
                /* TODO: Written by - Han Yongding 2023/10/20 令牌唯一标识 */
                .withJWTId(CurrentUtils.getUuId())
                /* TODO: Written by - Han Yongding 2023/10/20 用户数据库唯一标识 */
                .withClaim("id", id)
                /* TODO: Written by - Han Yongding 2023/10/20 用户名 */
                .withClaim("username", username)
                /* TODO: Written by - Han Yongding 2023/10/20 角色拥有的权限 */
                .withClaim("authorities", details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                /* TODO: Written by - Han Yongding 2023/10/20 刷新token */
                .withClaim("refreshToken", refreshToken)
                /* TODO: Written by - Han Yongding 2023/09/11 过期时间计算 */
                .withExpiresAt(this.expireTime())
                /* TODO: Written by - Han Yongding 2023/10/20 系统当前时间 */
                .withIssuedAt(new Date())
                /* TODO: Written by - Han Yongding 2023/09/11 加密算法 */
                .sign(this.getAlgorithm()) ;
    }

    /** TODO: Written by - Han Yongding 2023/11/02 创建Jwt令牌 */
    public String createJwt(RespCreateJwtVO vo, String refreshToken) {
        /* TODO: Written by - Han Yongding 2023/11/16 有错误，过期时间有误  */
        this.deleteToken(template.opsForValue().get(this.getDecodedJWTByToken(refreshToken).getId()), new Date(10000)) ;
        /* TODO: Written by - Han Yongding 2023/09/11 创建令牌并返回 */
        return JWT.create()
                /* TODO: Written by - Han Yongding 2023/10/20 令牌唯一标识 */
                .withJWTId(CurrentUtils.getUuId())
                /* TODO: Written by - Han Yongding 2023/10/20 用户数据库唯一标识 */
                .withClaim("id", vo.getId())
                /* TODO: Written by - Han Yongding 2023/10/20 用户名 */
                .withClaim("username", vo.getUsername())
                /* TODO: Written by - Han Yongding 2023/10/20 角色拥有的权限 */
                .withClaim("authorities", vo.getAuthorities())
                /* TODO: Written by - Han Yongding 2023/10/20 刷新token */
                .withClaim("refreshToken", refreshToken)
                /* TODO: Written by - Han Yongding 2023/09/11 过期时间计算 */
                .withExpiresAt(this.expireTime())
                /* TODO: Written by - Han Yongding 2023/10/20 系统当前时间 */
                .withIssuedAt(new Date())
                /* TODO: Written by - Han Yongding 2023/09/11 加密算法 */
                .sign(this.getAlgorithm()) ;
    }

    /** TODO: Written by - Han Yongding 2023/09/11 令牌过期时间计算 */
    public Date expireTime() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, this.expire) ;
        return instance.getTime() ;
    }

    /** TODO: Written by - Han Yongding 2023/10/20 刷新token有效时间计算 */
    public Date refreshTokenExpireTime() {
        Calendar instance = Calendar.getInstance() ;
        instance.add(Calendar.HOUR, this.refreshExpire * 24) ;
        return instance.getTime() ;
    }

    /** TODO: Written by - Han Yongding 2023/09/14 令牌有效状态：有效或无效 */
    public boolean invalidateJwt(String headerToken) {
        /* TODO: Written by - Han Yongding 2023/09/17 判断token是否合法 */
        String token = this.convertToken(headerToken) ;
        /* TODO: Written by - Han Yongding 2023/09/14 token为空 */
        if (token == null) {
            return false ;
        }
        /* TODO: Written by - Han Yongding 2023/09/14 解析Token */
        /* TODO: Written by - Han Yongding 2023/09/14 jwt有问题会抛出验证异常(运行时异常) */
        try {
            DecodedJWT jwt = this.getDecodedJWTByToken(token) ;
            return deleteToken(jwt.getId(), jwt.getExpiresAt()) ;
        } catch (JWTVerificationException e) {
            return false ;
        }
    }

    /** TODO: Written by - Han Yongding 2023/11/02 解析Token，返回DecodedJWT对象 */
    private DecodedJWT getDecodedJWTByToken(String token) {
        return JWT.require(this.getAlgorithm()).build().verify(token) ;
    }

    /** TODO: Written by - Han Yongding 2023/09/14 判断Token是否合法 */
    public String convertToken(String headerToken) {
        /* TODO: Written by - Han Yongding 2023/08/11 如果为空或者不以规定字符开头，判断为不合法，返回空，否则返回解析后的token */
        if (headerToken == null || !headerToken.startsWith(Const.TOKEN_PREFIX)) {
            return null ;
        }
        /* TODO: Written by - Han Yongding 2023/08/11 因为Bearer是7个字符 */
        return headerToken.substring(7) ;
    }

    /**TODO: Written by - Han Yongding 2023/09/14 删除Token */
    private boolean deleteToken(String uuid, Date time) {
        /* TODO: Written by - Han Yongding 2023/09/14 如果已经失效，那就返回false */
        if (this.isInvalidToken(uuid)) {
            return false ;
        }
        Date now = new Date() ;
        /* TODO: Written by - Han Yongding 2023/09/14 防止用户令牌已经过期，还退出登录，redis中会存入负值，所以此处用了Math.max，如果小于0，用0 */
        long expire = Math.max(time.getTime() - now.getTime(), 0) ;
        /* TODO: Written by - Han Yongding 2023/09/14 id， value， 过期时间，单位：毫秒  */
        template.opsForValue().set(Const.JWT_BLACK_LIST + uuid, "", expire, TimeUnit.MILLISECONDS) ;
        return true ;
    }

    /** TODO: Written by - Han Yongding 2023/09/14 该令牌是否是已过期（失效）的令牌 */
    private boolean isInvalidToken(String uuid) {
        return Boolean.TRUE.equals(template.hasKey(Const.JWT_BLACK_LIST + uuid)) ;
    }

    /** TODO: Written by - Han Yongding 2023/09/17 解析JWT */
    public DecodedJWT resolveJwt(String headerToken) {
        /* TODO: Written by - Han Yongding 2023/09/17 判断Token是否合法 */
        String token = this.convertToken(headerToken) ;
        /* TODO: Written by - Han Yongding 2023/09/17 传入token为空，直接返回null */
        if (headerToken == null) {
            return null ;
        }

        /* TODO: Written by - Han Yongding 2023/09/17 解析JWT */
        Algorithm algorithm = this.getAlgorithm() ;
        JWTVerifier build = JWT.require(algorithm).build() ;

        /* TODO: Written by - Han Yongding 2023/09/17 jwt有问题会抛出验证异常(运行时异常) */
        try {
            /* TODO: Written by - Han Yongding 2023/09/17 验证token */
            DecodedJWT verify = build.verify(token) ;
            /* TODO: Written by - Han Yongding 2023/09/17 如果token失效了，返回nul */
            if (this.isInvalidToken(verify.getId())) {
                return null ;
            }
            /* TODO: Written by - Han Yongding 2023/09/17 判断令牌是否已过期 */
            Date expiresAt = verify.getExpiresAt() ;
            /* TODO: Written by - Han Yongding 2023/09/17 此处三目判断为：判断令牌是否超过当前规定的日期，超过返回null，否则返回解析后的token */
            return new Date().after(expiresAt) ? null : verify ;
        } catch (JWTVerificationException e) {
            return null ;
        }
    }

    /** TODO: Written by - Han Yongding 2023/09/17 解析用户 */
    public UserDetails toUser(DecodedJWT jwt) {
        /* TODO: Written by - Han Yongding 2023/09/17 获取解码后的JSON Web Token */
        Map<String, Claim> claims = jwt.getClaims() ;
        /* TODO: Written by - Han Yongding 2023/09/17 authorities是createJwt方法中配置的 */
        return User.withUsername(claims.get("username").asString())
                .password("*******")
                .authorities(claims.get("authorities").asArray(String.class))
                .build() ;
    }

    /** TODO: Written by - Han Yongding 2023/11/02 解析用户的权限 */
    public List<String> toAuthorities(DecodedJWT jwt) {
        return jwt.getClaim("authorities").asList(String.class) ;
    }

    /** TODO: Written by - Han Yongding 2023/09/17 解析ID */
    public String toId(DecodedJWT jwt) {
        return jwt.getClaims().get("id").asString() ;
    }

    /** TODO: Written by - Han Yongding 2023/11/02 解析用户名 */
    public String toUsername(DecodedJWT jwt) {
        return jwt.getClaims().get("username").asString() ;
    }

    /** TODO: Written by - Han Yongding 2023/10/20 获取加密的Algorithm对象 */
    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(this.key) ;
    }

    /** TODO: Written by - Han Yongding 2023/10/20 创建刷新token */
    public String createRefreshToken(UserDetails details, String id, String username) {
        /* TODO: Written by - Han Yongding 2023/10/20 创建令牌并返回 */
        return JWT.create()
                /* TODO: Written by - Han Yongding 2023/10/20 刷新令牌唯一标识 */
                .withJWTId(CurrentUtils.getUuId())
                /* TODO: Written by - Han Yongding 2023/10/20 数据库唯一标识 */
                .withClaim("id", id)
                /* TODO: Written by - Han Yongding 2023/10/26 用户名 */
                .withClaim("username", username)
                /* TODO: Written by - Han Yongding 2023/10/20 角色拥有的权限 */
                .withClaim("authorities", details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                /* TODO: Written by - Han Yongding 2023/10/20 过期时间计算 */
                .withExpiresAt(this.refreshTokenExpireTime())
                /* TODO: Written by - Han Yongding 2023/10/20 当前时间 */
                .withIssuedAt(new Date())
                /* TODO: Written by - Han Yongding 2023/10/20 加密算法 */
                .sign(this.getAlgorithm()) ;
    }

    /** TODO: Written by - Han Yongding 2023/10/20 解析刷新Token */
    public String toRefreshToken(DecodedJWT jwt) {
        return jwt.getClaims().get("refreshToken").asString() ;
    }

    /** TODO: Written by - Han Yongding 2023/10/26 生成刷新token和token */
    public String createTokenAndRefreshToken(UserDetails details, String id, String username) {
        /* TODO: Written by - Han Yongding 2023/10/26 为用户创建刷新token */
        String refreshToken = this.createRefreshToken(details, id, username) ;

        /* TODO: Written by - Han Yongding 2023/09/11 返回Jwt(给用户颁发token) */
        String token = this.createJwt(details, id, username, refreshToken) ;

        template.opsForValue().set(this.getDecodedJWTByToken(refreshToken).getId(), this.getDecodedJWTByToken(token).getId()) ;

        return refreshToken + "," + token ;
    }

    /** TODO: Written by - Han Yongding 2023/10/26 获取刷新Token */
    public String getRefreshToken(String tokenArr) {
        return tokenArr.split(",")[0] ;
    }

    /** TODO: Written by - Han Yongding 2023/10/26 获取Token */
    public String getToken(String tokenArr) {
        return tokenArr.split(",")[1] ;
    }

    /** TODO: Written by - Han Yongding 2023/11/02 token是否已退出登录 */
    public boolean loggedOutThroughToken(String token) {
        return this.isInvalidToken(this.getDecodedJWTByToken(this.convertToken(token)).getId()) ;
    }

    /** TODO: Written by - Han Yongding 2023/11/02 令牌是否过期 */
    public boolean tokenExpiredOrNot(String token) {
        /* TODO: Written by - Han Yongding 2023/11/02 token合法，通过截取获取token */
        String tokens = token.substring(7) ;

        /* TODO: Written by - Han Yongding 2023/11/02 获取jwt对象 */
        DecodedJWT jwt = this.getDecodedJWTByToken(tokens);

        /* TODO: Written by - Han Yongding 2023/11/09
         * 当前时间是否超过了规定时间 == new Date.after(jwt.getExpiresAt()) ;
         * after方法是超过返回true，没超过范围false
         * 此处为了方便判断没超过，所以取反，则变成没超过返回true，超过返回false
         */
        /* TODO: Written by - Han Yongding 2023/11/02 令牌是否过期 */
        /* TODO: Written by - Han Yongding 2023/11/02 返回true表示未过期，否则返回false，表示已过期，和Date的after方法相反，所以此处取反 */
        return !new Date().after(jwt.getExpiresAt()) ;
    }

    /** TODO: Written by - Han Yongding 2023/11/02 通过刷新token颁发新token */
    public RespRefreshTokenVO issueANewTokenByRefreshToken(String refreshToken) {
        /* TODO: Written by - Han Yongding 2023/11/02 截取刷新令牌 */
        String refreshTokens = this.convertToken(refreshToken) ;
        /* TODO: Written by - Han Yongding 2023/11/02 获取令牌的jwt对象 */
        DecodedJWT jwt = this.getDecodedJWTByToken(refreshTokens) ;
        /* TODO: Written by - Han Yongding 2023/11/02 解析权限 */
        List<String> authorities = this.toAuthorities(jwt) ;
        /* TODO: Written by - Han Yongding 2023/11/02 解析id */
        String id = this.toId(jwt) ;
        /* TODO: Written by - Han Yongding 2023/11/02 解析用户名 */
        String username = this.toUsername(jwt) ;

        return new RespRefreshTokenVO(refreshTokens, this.createJwt(new RespCreateJwtVO(id, username, authorities), refreshTokens)) ;
    }
}
