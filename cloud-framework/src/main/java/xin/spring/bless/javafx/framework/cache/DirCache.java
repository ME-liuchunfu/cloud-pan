package xin.spring.bless.javafx.framework.cache;

import xin.spring.bless.javafx.framework.security.EncodeAndDecode;

import java.io.*;

/**
 * 本地目录缓存
 */
public class DirCache {

    /**
     * 缓存数据
     * @param data
     */
    public String cacheUser(String data){
        String encode = EncodeAndDecode.base64encode(data);
        String homeDir = getHomeDir();
        File file = new File(homeDir, "cloud-pan");
        if(!file.exists()){
            file.mkdirs();
        }
        File tag = new File(file.getAbsolutePath(), "user-cache.txt");
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try{
            fileWriter = new FileWriter(tag);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(encode);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(bufferedWriter);
        }
        return encode;
    }

    /**
     * 读取数据
     * @return
     */
    public String getUserData(){
        File file = new File(getHomeDir(),"cloud-pan/user-cache.txt");
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            StringBuffer sbf = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null){
                sbf.append(line);
            }
            String s = EncodeAndDecode.base64decode(sbf.toString());
            return s;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(bufferedReader);
        }
        return null;
    }

    private void close(BufferedReader reader){
        try{
            if (reader != null) {
                reader.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void close(BufferedWriter writer){
        try{
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getHomeDir(){
        String home = System.getProperties().getProperty("user.home");
        return home;
    }

}
