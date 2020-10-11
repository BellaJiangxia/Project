package com.vastsoft.yingtai.action;

import com.vastsoft.util.common.FileTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.io.IOTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件缓存工具，此类线程同步<br/>
 * 文件缓存区默认是 20MB <br/>
 * 单个文件最大200KB
 * Created by jiangyubin on 2017/1/19.
 */
public class FileContentCacheTools {
    private FileContentCacheTools() {
    }

    private static Map<String, byte[]> mapFileCache = new HashMap<String, byte[]>();
    private static long file_total_cache_length = (20 * 1024 * 1024);
    private static long single_file_max_length = (200 * 1024);

    /**
     * 当前已使用的长度
     */
    private static long curr_cache_length = 0;

    /**
     * @param file
     * @return 如果未添加成功返回null
     * @throws IOException
     */
    public static synchronized String newCacheFile(File file) throws IOException {
        if (file == null)
            return null;
        if (file.length() <= 0)
            return null;
        long file_length = file.length();
        if (file_length > (file_total_cache_length - curr_cache_length))
            return null;
        if (file_length > single_file_max_length)
            return null;
        return newCacheFile(IOTools.streamToBytes(new FileInputStream(file)));

    }

    public static synchronized boolean exist(String uuid) {
        return mapFileCache.get(uuid) != null;
    }

    public static synchronized byte[] getFileByUuid(String uuid) {
        return mapFileCache.get(uuid);
    }

    /**
     * 获取单个文件的最大大小
     *
     * @return
     */
    public static synchronized long getSingle_file_max_length() {
        return single_file_max_length;
    }

    /**
     * 设置单个文件的最大大小
     *
     * @param single_file_max_length
     */
    public static synchronized void setSingle_file_max_length(long single_file_max_length) {
        FileContentCacheTools.single_file_max_length = single_file_max_length;
    }

    /**
     * 获取当前已使用了的缓存区域大小
     *
     * @return
     */
    public static synchronized long getCurr_cache_length() {
        return curr_cache_length;
    }

    /**
     * 获取缓存空间最大支持的长度
     *
     * @return
     */
    public static synchronized long getFile_total_cache_length() {
        return file_total_cache_length;
    }

    /**
     * 设置缓存空间最大支持的长度，如果设置的长度小于现有长度则设置失败
     *
     * @param file_total_cache_length
     */
    public static synchronized void setFile_total_cache_length(long file_total_cache_length) {
        FileContentCacheTools.file_total_cache_length = file_total_cache_length;
    }

    /**
     * @param fileContent
     * @return 如果未添加成功返回null
     */
    public static synchronized String newCacheFile(byte[] fileContent) {
        if (fileContent == null || fileContent.length <= 0)
            return null;
        if (fileContent.length > (file_total_cache_length - curr_cache_length))
            return null;
        if (fileContent.length > single_file_max_length)
            return null;
        if (wasExist(fileContent))
            return null;
        String uuid = StringTools.getUUID();
        mapFileCache.put(uuid, fileContent);
        return uuid;
    }

    private static boolean wasExist(byte[] fileContent) {
        if (mapFileCache.size() <= 0)
            return false;
        for (byte[] filec : mapFileCache.values()) {
            if (Arrays.equals(filec, fileContent))
                return true;
        }
        return false;
    }


    private static class FileCache {
        private String name;
        private String file_path;
        private byte[] file_content;
    }
}
