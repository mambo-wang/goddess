package com.h3c.vdi.athena.common.utils;

import java.io.IOException;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FuncUtil {
    private static Log log = LogFactory.getLog(FuncUtil.class);

    public FuncUtil() {
    }

    public static String convertObjectToXml(Object var0, Class var1) {
        if(var0 == null) {
            log.error("Class Name:" + var1.getName());
            return null;
        } else {
            StringWriter var2 = null;
            boolean var10 = false;

            String var18;
            label150: {
                label151: {
                    try {
                        var10 = true;
                        var2 = new StringWriter();
                        getMarshaller(var1).marshal(var0, var2);
                        log.debug("Convert " + var0 + " to " + var2.toString());
                        var18 = var2.toString();
                        var18 = new String(gbkToUtf8(var18), "UTF-8");
                        var10 = false;
                        break label150;
                    } catch (JAXBException var23) {
                        log.warn((Object)null, var23);
                        var10 = false;
                    } catch (Exception var24) {
                        log.error((Object)null, var24);
                        var10 = false;
                        break label151;
                    } finally {
                        if(var10 && var2 != null) {
                            try {
                                var2.close();
                            } catch (IOException var20) {
                                log.warn((Object)null, var20);
                            }
                        }

                    }

                    if(var2 != null) {
                        try {
                            var2.close();
                        } catch (IOException var19) {
                            log.warn((Object)null, var19);
                        }
                    }

                    return null;
                }

                if(var2 != null) {
                    try {
                        var2.close();
                    } catch (IOException var21) {
                        log.warn((Object)null, var21);
                    }
                }

                return null;
            }

            try {
                var2.close();
            } catch (IOException var22) {
                log.warn((Object)null, var22);
            }

            return var18;
        }
    }

    public static Marshaller getMarshaller(Class var0) throws JAXBException {
        Marshaller var1;
        (var1 = JAXBContext.newInstance(new Class[]{var0}).createMarshaller()).setProperty("jaxb.formatted.output", Boolean.valueOf(true));
        var1.setProperty("jaxb.fragment", Boolean.valueOf(true));
        return var1;
    }

    public static byte[] gbkToUtf8(String var0) {
        char[] var8 = var0.toCharArray();
        byte[] var1 = new byte[3 * var8.length];
        int var2 = 0;

        for(int var9 = 0; var9 < var8.length; ++var9) {
            char var4;
            if((var4 = var8[var9]) >= 0 && var4 <= 255) {
                var1[var2] = (byte)var4;
                ++var2;
            } else {
                String var10 = Integer.toBinaryString(var4);
                StringBuffer var5 = new StringBuffer();
                int var6 = 16 - var10.length();

                for(int var15 = 0; var15 < var6; ++var15) {
                    var5.append("0");
                }

                var5.append(var10);
                var5.insert(0, "1110");
                var5.insert(8, "10");
                var5.insert(16, "10");
                String var161 = var5.substring(0, 8);
                var10 = var5.substring(8, 16);
                String var12 = var5.substring(16);
                byte var14 = Integer.valueOf(var161, 2).byteValue();
                byte var11 = Integer.valueOf(var10, 2).byteValue();
                byte var13 = Integer.valueOf(var12, 2).byteValue();
                byte[] var16;
                (var16 = new byte[3])[0] = var14;
                var16[1] = var11;
                var16[2] = var13;
                var1[var2] = var16[0];
                var1[var2 + 1] = var16[1];
                var1[var2 + 2] = var16[2];
                var2 += 3;
            }
        }

        byte[] var151 = new byte[var2];
        System.arraycopy(var1, 0, var151, 0, var2);
        return var151;
    }
}