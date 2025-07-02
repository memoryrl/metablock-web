package cetus.support;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.Alias;

import cetus.annotation.Key;
import cetus.annotation.TableName;
import cetus.bean.CetusBean;
import cetus.util.StringUtil;

public class CetusCreator {

    private String t = "    ";
    private String tt = t + t;
    private String dmlPrefix = "       ";

    Class<?> clazz;
    private String className;
    private String tableName;
    private String aliasName;
    private String packageString;

    private String serviceClassName;
    private String daoClassName;

    List<Field> fields = new ArrayList<>();

    public static void create(Class<? extends CetusBean> clazz) {
        try {
            new CetusCreator(clazz).create();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public CetusCreator(Class<? extends CetusBean> clazz) {
        this.clazz = clazz;

        className = clazz.getSimpleName();
        serviceClassName = className + "Service";
        daoClassName = className + "Dao";

        TableName atb = clazz.getAnnotation(TableName.class);
        if (atb != null) {
            tableName = atb.value();
        } else {
            tableName = StringUtil.toUpperUnderscore(clazz.getSimpleName());
        }

        Alias alias = clazz.getAnnotation(Alias.class);
        if (alias != null) {
            aliasName = alias.value();
        } else {
            aliasName = StringUtil.uncapitalize(clazz.getSimpleName());
        }

        java.lang.reflect.Field[] fs = clazz.getDeclaredFields();
        for(java.lang.reflect.Field f : fs) {
            Field sf = new Field();
            sf.name = f.getName();
            sf.uname = StringUtil.toUpperUnderscore(f.getName());
            Key key = f.getAnnotation(Key.class);
            sf.isKey = key != null;
            fields.add(sf);
        }

        packageString = clazz.getPackage().toString();
    }

    public void create() throws IOException {
        xml();
        dao();
        service();
    }

    ////////////////////////////////////////////////////////////////////////////////
    // dao
    public void dao() throws IOException {
        String fileName = getSrcPath(daoClassName + ".java");
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(gendao());
        writer.close();
    }
    
    public String gendao() throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append(packageString + ";\n\n");
        sb.append("import org.springframework.stereotype.Component;\nimport cetus.dao.CetusDao;\n\n");

        sb.append("@Component\n");
        sb.append(String.format("public class %s extends CetusDao<%s> {\n\n", daoClassName, className));
        sb.append(String.format(t + "public %s() {\n", daoClassName));
        sb.append(tt + "super(\"" + aliasName + "\");\n");
        sb.append(t + "}\n");
        sb.append("}\n");

        return sb.toString();
    }

    ////////////////////////////////////////////////////////////////////////////////
    // service
    public void service() throws IOException {
        String fileName = getSrcPath(serviceClassName + ".java");
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(genService());
        writer.close();
    }
    
    public String genService() throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append(packageString + ";\n\n");
        sb.append("import org.springframework.stereotype.Service;\nimport cetus.service.CetusService;\n\n");

        sb.append("@Service\n");
        sb.append(String.format("public class %s extends CetusService<%s, %s> {\n\n", serviceClassName, className, daoClassName));
        sb.append("}");
        return sb.toString();
    }

    ////////////////////////////////////////////////////////////////////////////////
    // xml
    public void xml() throws IOException {
        String fileName = getSrcPath(StringUtil.uncapitalize(className) + "-sqlmap.xml");
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(genXml());
        writer.close();
    }

    // create xml string
    private String genXml() {
        String h = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<!DOCTYPE mapper\n    PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n\n";
        String s = String.format("<mapper namespace=\"%s\">\n\n", aliasName);
        String e = "</mapper>";

        StringBuilder sb = new StringBuilder();
        sb.append(h).append(s);

        sb.append(list());
        sb.append(view());
        sb.append(insert());
        sb.append(update());
        sb.append(delete());
        sb.append(count());
        sb.append(page());

        sb.append(e);
        
        return sb.toString();
    }

    private String list() {
        String s = String.format(t + "<select id=\"list\" resultType=\"%s\">\n", aliasName);
        String e = t + "</select>\n\n";

        StringBuilder sb = new StringBuilder();
        sb.append(s);

        sb.append(tt + "SELECT\n");
        for(Field f : fields) {
            sb.append(tt + dmlPrefix + f.uname + ",\n");
        }
        sb.deleteCharAt(sb.length() - 2);
        sb.append(tt + "  FROM " + tableName + "\n");
        sb.append(tt + " WHERE 1 = 1\n");

        for(Field f : fields) {
            if(!f.isKey) {
                sb.append(tt + "   <if test=\"@Ognl@isNotEmpty(" + f.name + ")\">\n");
            }
            sb.append(tt + "   AND " + f.uname + " = #{" + f.name + "}\n");
            if(!f.isKey) {
                sb.append(tt + "   </if>\n");
            }
        }

        sb.append(e);
        return sb.toString();
    }

    private String view() {
        String s = String.format(t + "<select id=\"view\" resultType=\"%s\">\n", aliasName);
        String e = t + "</select>\n\n";

        StringBuilder sb = new StringBuilder();
        sb.append(s);

        sb.append(tt + "SELECT\n");
        for(Field f : fields) {
            sb.append(tt + dmlPrefix + f.uname + ",\n");
        }
        sb.deleteCharAt(sb.length() - 2);
        sb.append(tt + "  FROM " + tableName + "\n");
        sb.append(tt + " WHERE 1 = 1\n");

        for(Field f : fields) {
            if(f.isKey) {
                sb.append(tt + "   AND " + f.uname + " = #{" + f.name + "}\n");
            }
        }

        sb.append(e);
        return sb.toString();
    }

    private String insert() {
        String s = String.format(t + "<insert id=\"insert\" parameterType=\"%s\">\n", aliasName);
        String e = t + "</insert>\n\n";

        StringBuilder sb = new StringBuilder();
        sb.append(s);

        sb.append(tt + "INSERT INTO " + tableName + " (\n");
        for(Field f : fields) {
            sb.append(tt + dmlPrefix + f.uname + ",\n");
        }
        sb.deleteCharAt(sb.length() - 2);
        sb.append(tt + ") VALUES (\n");

        for(Field f : fields) {
            sb.append(tt + dmlPrefix + "#{" + f.name + "},\n");
        }
        sb.deleteCharAt(sb.length() - 2);
        sb.append(tt + ")\n");

        sb.append(e);
        return sb.toString();
    }

    private String update() {
        String s = String.format(t + "<update id=\"update\" parameterType=\"%s\">\n", aliasName);
        String e = t + "</update>\n\n";

        StringBuilder sb = new StringBuilder();
        StringBuilder field = new StringBuilder();
        StringBuilder where = new StringBuilder();
        sb.append(s);

        sb.append(tt + "UPDATE " + tableName + " SET\n");
        for(Field f : fields) {
            if(f.isKey) {
                where.append(tt + "   AND " + f.uname + " = #{" + f.name + "}\n");
            } else {
                field.append(tt + dmlPrefix + f.uname + " = #{" + f.name + "},\n");
            }
        }
        field.deleteCharAt(field.length() - 2);
        sb.append(field);
        sb.append(tt + " WHERE 1 = 1\n");
        sb.append(where);

        sb.append(e);
        return sb.toString();
    }

    private String delete() {
        String s = String.format(t + "<delete id=\"delete\" parameterType=\"%s\">\n", aliasName);
        String e = t + "</delete>\n\n";

        StringBuilder sb = new StringBuilder();
        sb.append(s);

        sb.append(tt + "DELETE FROM " + tableName + "\n");
        sb.append(tt + " WHERE 1 = 1\n");
        for(Field f : fields) {
            if(f.isKey) {
                sb.append(tt + "   AND " + f.uname + " = #{" + f.name + "}\n");
            }
        }

        sb.append(e);
        return sb.toString();
    }

    private String count() {
        String s = t + "<select id=\"count\" resultType=\"int\">\n";
        String e = t + "</select>\n\n";

        StringBuilder sb = new StringBuilder();
        sb.append(s);

        sb.append(tt + "SELECT COUNT(*)\n");
        sb.append(tt + "  FROM " + tableName + "\n");
        sb.append(tt + " WHERE 1 = 1\n");

        for(Field f : fields) {
            if(!f.isKey) {
                sb.append(tt + "   <if test=\"@Ognl@isNotEmpty(" + f.name + ")\">\n");
            }
            sb.append(tt + "   AND " + f.uname + " = #{" + f.name + "}\n");
            if(!f.isKey) {
                sb.append(tt + "   </if>\n");
            }
        }

        sb.append(e);
        return sb.toString();
    }

    private String page() {
        return "";
    }

    private String getSrcPath(String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(new File(".").getAbsolutePath());
        sb.deleteCharAt(sb.length() - 1);
        sb.append("src\\main\\java");

        String[] p = packageString.replace("package", "").split("\\.");

        for(String s : p) {
            sb.append("\\" + s.trim());
        }
        
        String path = sb.toString() + "\\" + fileName;

        return path;
    }

    private static class Field {
        boolean isKey;
        String name;
        String uname;
    }


}