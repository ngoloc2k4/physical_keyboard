package moe.shizuku.api;

import rikka.shizuku.ShizukuRemoteProcess;

interface IShizukuService {
    
    /**
     * Get API version
     */
    int getVersion() = 2;
    
    /**
     * Get UID of Shizuku
     */
    int getUid() = 3;
    
    /**
     * Check permission
     */
    int checkPermission(String permission) = 4;
    
    /**
     * Create new process - ĐÂY LÀ HÀM QUAN TRỌNG NHẤT
     */
    ShizukuRemoteProcess newProcess(in String[] cmd, in String[] env, in String dir) = 6;
    
    /**
     * Get SELinux context
     */
    String getSELinuxContext() = 8;
    
    /**
     * Get system service
     */
    IBinder getSystemService(String name) = 10;
    
    /**
     * Exit Shizuku
     */
    void exit() = 100;
}