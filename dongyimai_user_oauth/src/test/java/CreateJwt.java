import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;


public class CreateJwt {

    /**
     * 创建令牌
     */
    @Test
    public void create() {
        //resources路径下的证书文件
        String key_location = "dongyimai.jks";
        //密钥库密码
        String key_password = "dongyimai";
        //密钥密码
        String key_pwd = "dongyimai";
        //密钥别名
        String alias = "dongyimai";

        //访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);

        //创建密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, key_password.toCharArray());

        //读取密钥对
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, key_pwd.toCharArray());

        //获取私钥
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

        //定义PlayLoad，自定义一些参数
        Map<String, String> map = new HashMap<>();
        map.put("id", "1");
        map.put("name", "自定义参数");
        map.put("roles", "ROLE_VIP,ROLE_USER");

        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(map), new RsaSigner(rsaPrivateKey));

        //取出令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
        //eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiLoh6rlrprkuYnlj4LmlbAiLCJpZCI6IjEifQ.cXGl0SW0iu1IGIbnhE5cxy78BkWhwJX8drisjwTIshioMY-hO6L0FB6iURWCBO0Blg3_UP2fAFheD_WX6t6NMP-olxyyQvU3q5HXEPXmnItnmyBOz5_zocBgk4k_xFamm29NwOqUtbJeCk99cQlcImb7fDl3_6tCXSqPq761guRhz-b54yXwTm3gTCyuESCSnl4KaoHiY4cDirHHckgzHyGBQKvPes-zSdVWMgZ3N7wXuMb0bb9Aeo-I9ZP-R1L-4qc1QibuUAqXSpj9a8fCBaoXxc4PtYCZIGc62Fd-a7_Joj9ik_NeBuIu-SVZ6wIcebnP33e51jfu29M3m8hlng
    }


    /**
     * 使用公钥 解析令牌
     */
    @Test
    public void resolve() {
        //令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiLoh6rlrprkuYnlj4LmlbAiLCJpZCI6IjEifQ.cXGl0SW0iu1IGIbnhE5cxy78BkWhwJX8drisjwTIshioMY-hO6L0FB6iURWCBO0Blg3_UP2fAFheD_WX6t6NMP-olxyyQvU3q5HXEPXmnItnmyBOz5_zocBgk4k_xFamm29NwOqUtbJeCk99cQlcImb7fDl3_6tCXSqPq761guRhz-b54yXwTm3gTCyuESCSnl4KaoHiY4cDirHHckgzHyGBQKvPes-zSdVWMgZ3N7wXuMb0bb9Aeo-I9ZP-R1L-4qc1QibuUAqXSpj9a8fCBaoXxc4PtYCZIGc62Fd-a7_Joj9ik_NeBuIu-SVZ6wIcebnP33e51jfu29M3m8hlng";

        //公钥
        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApmz4PzcO5vn2umxlsr343ttKhRiXTTLPbwoFYa4S13FRVjgJ+VsySn1CJrUCqDyunHKU17H6XY/c3E9tPwfSf/d1jdCxVTWDr8H2QwOsDRn/RrAqJiF9Xj9UITVHVx61vllysCzvYGLNLJFBy3sHfL9yYZJYvJqNcCufCn8dlE6Hh3BEmRVzmZolt174nvnmShX99JQYwKPCHqplAsI3AMjsHk76d8wJpxIY5K/u+RrMpkW3H9HS6c2guw4yk1NcvomDCnVG/ZFyJP756vvqBxyFwhYq/rewi1lAlJ5WXuJUGZirVZr1PRZoqS+oJTc1ZCEyZDWPPXjTy45CLErKtwIDAQAB-----END PUBLIC KEY-----";

        //校验jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));

        //获取jwt原始内容
        String claims = jwt.getClaims();
        System.out.println("claims:" + claims);
//{"roles":"ROLE_VIP,ROLE_USER","name":"自定义参数","id":"1"}

        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println("encoded:" + encoded);
//encoded:eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiLoh6rlrprkuYnlj4LmlbAiLCJpZCI6IjEifQ.cXGl0SW0iu1IGIbnhE5cxy78BkWhwJX8drisjwTIshioMY-hO6L0FB6iURWCBO0Blg3_UP2fAFheD_WX6t6NMP-olxyyQvU3q5HXEPXmnItnmyBOz5_zocBgk4k_xFamm29NwOqUtbJeCk99cQlcImb7fDl3_6tCXSqPq761guRhz-b54yXwTm3gTCyuESCSnl4KaoHiY4cDirHHckgzHyGBQKvPes-zSdVWMgZ3N7wXuMb0bb9Aeo-I9ZP-R1L-4qc1QibuUAqXSpj9a8fCBaoXxc4PtYCZIGc62Fd-a7_Joj9ik_NeBuIu-SVZ6wIcebnP33e51jfu29M3m8hlng
    }
}
