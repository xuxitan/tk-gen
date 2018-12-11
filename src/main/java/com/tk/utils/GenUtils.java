package com.tk.utils;

import com.tk.entity.ColumnEntity;
import com.tk.entity.TableEntity;
import com.tk.entity.TargetDirectory;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器   工具类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月19日 下午11:40:24
 */
public class GenUtils {


    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();
        templates.add("template/Entity.java.vm");
        templates.add("template/Dao.java.vm");
        templates.add("template/Dao.xml.vm");
//        templates.add("template/Service.java.vm");
//        templates.add("template/ServiceImpl.java.vm");
//        templates.add("template/Controller.java.vm");
//        templates.add("template/menu.sql.vm");
//        templates.add("template/index.vue.vm");
//        templates.add("template/add-or-update.vue.vm");

        return templates;
    }

    /**
     * 生成代码
     */
    public static void generatorCode(Map<String, String> table,
                                     List<Map<String, String>> columns,TargetDirectory directory) throws Exception {
        //配置信息
        Configuration config = getConfig();
        boolean hasBigDecimal = false;
        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.get("tableName"));
        tableEntity.setComments(table.get("tableComment"));
        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), config.getString("tablePrefix"));
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        for (Map<String, String> column : columns) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("columnName"));
            columnEntity.setDataType(column.get("dataType"));
            columnEntity.setComments(column.get("columnComment"));
            columnEntity.setExtra(column.get("extra"));

            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), "unknowType");
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && attrType.equals("BigDecimal")) {
                hasBigDecimal = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null) {
                columnEntity.setFlag(true);
                tableEntity.setPk(columnEntity);
            }
            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

//        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        String mainPath = config.getString("mainPath");
        mainPath = StringUtils.isBlank(mainPath) ? "io.renren" : mainPath;
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("mainPath", mainPath);
        map.put("poPackage", directory.getPackagePoName());
        map.put("daoPackage", directory.getPackageDaoName());
        map.put("moduleName", config.getString("moduleName"));
        map.put("author", config.getString("author"));
        map.put("email", config.getString("email"));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);
            FileOutputStream output = null;
            try {
                String fileName = getFileName(directory, template, tableEntity.getClassName());
                System.out.println("===fileName==="+fileName);
                File file = new File(getFileName(directory,template, tableEntity.getClassName()));
                output = new FileOutputStream(file);
                output.write(sw.toString().getBytes("UTF-8"));
            } catch (Exception e) {
                throw new Exception("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            }
        }
    }


    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replaceFirst(tablePrefix, "");
        }
        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new RRException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(TargetDirectory directory,String template, String className) {
        String projectName = null;

        //项目目录
        if(StringUtils.isNotBlank(directory.getProjectName())){
            projectName = mdkir(directory.getProjectName().replace("/","\\"));
        }

        //entity
        if (template.contains("Entity.java.vm")) {
//            return packagePath + "entity" + File.separator + className + "Entity.java";
            System.out.println("进来了");
            String pathEntity = projectName + File.separator + directory.getBasePackagePoName().replace("/", "\\") +
                    File.separator + directory.getPackagePoName().replace(".", File.separator);
            pathEntity = mdkir(pathEntity);
            return pathEntity + File.separator + className + ".java";

        }
        //dao
        if (template.contains("Dao.java.vm")) {
            String pathDao = projectName + File.separator + directory.getBasePackageDaoName().replace("/", "\\") +
                    File.separator + directory.getPackageDaoName().replace(".", File.separator);
            pathDao = mdkir(pathDao);

            return pathDao + File.separator + className + "Dao.java";
        }

        //xml
        if (template.contains("Dao.xml.vm")) {
            String pathXml = projectName + File.separator + directory.getBasePackageMapperXmlName().replace("/", "\\") +
                    File.separator + directory.getPackageMapperXmlName().replace(".", File.separator);
            pathXml = mdkir(pathXml);

            return pathXml + File.separator + className + "Dao.xml";
        }

//        if (template.contains("Service.java.vm")) {
//            return packagePath + "service" + File.separator + className + "Service.java";
//        }
//
//        if (template.contains("ServiceImpl.java.vm")) {
//            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
//        }
//
//        if (template.contains("Controller.java.vm")) {
//            return packagePath + "controller" + File.separator + className + "Controller.java";
//        }
//
//        if (template.contains("menu.sql.vm")) {
//            return className.toLowerCase() + "_menu.sql";
//        }
//
//        if (template.contains("index.vue.vm")) {
//            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" +
//                    File.separator + moduleName + File.separator + className.toLowerCase() + ".vue";
//        }
//
//        if (template.contains("add-or-update.vue.vm")) {
//            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" +
//                    File.separator + moduleName + File.separator + className.toLowerCase() + "-add-or-update.vue";
//        }

        return null;
    }

    //如果没有目录则创建
    public static String mdkir(String mdkir){
        try {
            File file = new File(mdkir);
            if(!file.exists()){
                file.mkdirs();
            }
        }catch (Exception e){
            System.out.println("创建失败");
        }
        return mdkir;
    }
}
