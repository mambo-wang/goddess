import com.google.gson.Gson;
import com.h3c.vdi.athena.common.model.DefaultUserDetails;
import feign.codec.Decoder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by JemmyZhang on 2018/2/24
 */
public class GsonTest {

    interface DecoderImpl extends Decoder{

    }

    public static void main(String[] args) {
        ClassA classA=new ClassA("tom","tom");
        Gson gson = new Gson();
        String jsonA = gson.toJson(classA);
        System.out.println(jsonA);
        ClassB aClone = gson.fromJson(jsonA, ClassB.class);
        System.out.println(aClone);

        ArrayList<String> roles=new ArrayList<>();
        roles.add("SYSTEM");
        roles.add("ADMIN");
        DefaultUserDetails details=new DefaultUserDetails("admin","admin",roles);
        String subject=new Gson().toJson(details);
        String suffix="Bearer ";
        Calendar instance = Calendar.getInstance();
        instance.set(9999,Calendar.JANUARY,1,0,0,0);
        Date date=new Date(instance.getTimeInMillis());
        System.out.println(date);
        String token = Jwts
                .builder()
                .setSubject(subject)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, "Athena")
                .compact();
        System.out.println(subject);
        System.out.println(suffix+token);
    }


}
