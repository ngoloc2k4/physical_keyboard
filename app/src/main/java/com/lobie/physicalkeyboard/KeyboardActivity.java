package com.lobie.physicalkeyboard;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder; // QUAN TRỌNG: Thêm import này
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

// Các import cần thiết
import moe.shizuku.api.IShizukuService; // QUAN TRỌNG: Thêm import này
import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuRemoteProcess;
public class KeyboardActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "KeyboardActivity";
    private static final int SHIZUKU_REQUEST_CODE = 123;

    // Sử dụng Map để quản lý các lệnh, rất hiệu quả
    private final Map<Integer, String[]> keyCommandMap = new HashMap<>();

   // Listener mới để lắng nghe khi Shizuku sẵn sàng
    private final Shizuku.OnBinderReceivedListener binderReceivedListener = () -> {
        Log.d(TAG, "Binder của Shizuku đã nhận được. Dịch vụ đã sẵn sàng.");
        checkShizukuPermission();
    };

    // Listener xử lý kết quả yêu cầu quyền
    private final Shizuku.OnRequestPermissionResultListener permissionResultListener = (requestCode, grantResult) -> {
        if (requestCode == SHIZUKU_REQUEST_CODE) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Đã cấp quyền Shizuku!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Quyền Shizuku bị từ chối.", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        
        // Flag này rất quan trọng để bàn phím hoạt động như một lớp phủ (overlay)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        // Đăng ký listener để xử lý kết quả xin quyền
        Shizuku.addBinderReceivedListener(binderReceivedListener);
        Shizuku.addRequestPermissionResultListener(permissionResultListener);

        initializeKeyMapAndListeners();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy đăng ký các listener
        Shizuku.removeBinderReceivedListener(binderReceivedListener);
        Shizuku.removeRequestPermissionResultListener(permissionResultListener);
    }


    // Gán lệnh cho các nút và thiết lập listener
    private void initializeKeyMapAndListeners() {
        keyCommandMap.clear();

          // --- HÀNG 1: SỐ ---
        keyCommandMap.put(R.id.btn_1, new String[]{"input", "text", "1"});
        keyCommandMap.put(R.id.btn_2, new String[]{"input", "text", "2"});
        keyCommandMap.put(R.id.btn_3, new String[]{"input", "text", "3"});
        keyCommandMap.put(R.id.btn_4, new String[]{"input", "text", "4"});
        keyCommandMap.put(R.id.btn_5, new String[]{"input", "text", "5"});
        keyCommandMap.put(R.id.btn_6, new String[]{"input", "text", "6"});
        keyCommandMap.put(R.id.btn_7, new String[]{"input", "text", "7"});
        keyCommandMap.put(R.id.btn_8, new String[]{"input", "text", "8"});
        keyCommandMap.put(R.id.btn_9, new String[]{"input", "text", "9"});
        keyCommandMap.put(R.id.btn_0, new String[]{"input", "text", "0"});
        keyCommandMap.put(R.id.btn_minus, new String[]{"input", "text", "-"});
        keyCommandMap.put(R.id.btn_equals, new String[]{"input", "text", "="});
        keyCommandMap.put(R.id.btn_backspace, new String[]{"input", "keyevent", "KEYCODE_DEL"});

        // --- HÀNG 2: QWERTY ---
        keyCommandMap.put(R.id.btn_tab, new String[]{"input", "keyevent", "KEYCODE_TAB"});
        keyCommandMap.put(R.id.btn_q, new String[]{"input", "text", "q"});
        keyCommandMap.put(R.id.btn_w, new String[]{"input", "text", "w"});
        keyCommandMap.put(R.id.btn_e, new String[]{"input", "text", "e"});
        keyCommandMap.put(R.id.btn_r, new String[]{"input", "text", "r"});
        keyCommandMap.put(R.id.btn_t, new String[]{"input", "text", "t"});
        keyCommandMap.put(R.id.btn_y, new String[]{"input", "text", "y"});
        keyCommandMap.put(R.id.btn_u, new String[]{"input", "text", "u"});
        keyCommandMap.put(R.id.btn_i, new String[]{"input", "text", "i"});
        keyCommandMap.put(R.id.btn_o, new String[]{"input", "text", "o"});
        keyCommandMap.put(R.id.btn_p, new String[]{"input", "text", "p"});
        keyCommandMap.put(R.id.btn_left_bracket, new String[]{"input", "text", "["});
        keyCommandMap.put(R.id.btn_right_bracket, new String[]{"input", "text", "]"});
        keyCommandMap.put(R.id.btn_backslash, new String[]{"input", "text", "\\"});

        // --- HÀNG 3: ASDF ---
        keyCommandMap.put(R.id.btn_caps, new String[]{"input", "keyevent", "KEYCODE_CAPS_LOCK"});
        keyCommandMap.put(R.id.btn_a, new String[]{"input", "text", "a"});
        keyCommandMap.put(R.id.btn_s, new String[]{"input", "text", "s"});
        keyCommandMap.put(R.id.btn_d, new String[]{"input", "text", "d"});
        keyCommandMap.put(R.id.btn_f, new String[]{"input", "text", "f"});
        keyCommandMap.put(R.id.btn_g, new String[]{"input", "text", "g"});
        keyCommandMap.put(R.id.btn_h, new String[]{"input", "text", "h"});
        keyCommandMap.put(R.id.btn_j, new String[]{"input", "text", "j"});
        keyCommandMap.put(R.id.btn_k, new String[]{"input", "text", "k"});
        keyCommandMap.put(R.id.btn_l, new String[]{"input", "text", "l"});
        keyCommandMap.put(R.id.btn_semicolon, new String[]{"input", "text", ";"});
        keyCommandMap.put(R.id.btn_quote, new String[]{"input", "text", "'"});
        keyCommandMap.put(R.id.btn_enter, new String[]{"input", "keyevent", "KEYCODE_ENTER"});

        // --- HÀNG 4: ZXCV ---
        keyCommandMap.put(R.id.btn_shift_left, new String[]{"input", "keyevent", "KEYCODE_SHIFT_LEFT"});
        keyCommandMap.put(R.id.btn_z, new String[]{"input", "text", "z"});
        keyCommandMap.put(R.id.btn_x, new String[]{"input", "text", "x"});
        keyCommandMap.put(R.id.btn_c, new String[]{"input", "text", "c"});
        keyCommandMap.put(R.id.btn_v, new String[]{"input", "text", "v"});
        keyCommandMap.put(R.id.btn_b, new String[]{"input", "text", "b"});
        keyCommandMap.put(R.id.btn_n, new String[]{"input", "text", "n"});
        keyCommandMap.put(R.id.btn_m, new String[]{"input", "text", "m"});
        keyCommandMap.put(R.id.btn_comma, new String[]{"input", "text", ","});
        keyCommandMap.put(R.id.btn_period, new String[]{"input", "text", "."});
        keyCommandMap.put(R.id.btn_slash, new String[]{"input", "text", "/"});
        keyCommandMap.put(R.id.btn_shift_right, new String[]{"input", "keyevent", "KEYCODE_SHIFT_RIGHT"});

        // --- HÀNG 5: CHỨC NĂNG ---
        keyCommandMap.put(R.id.btn_ctrl_left, new String[]{"input", "keyevent", "KEYCODE_CTRL_LEFT"});
        keyCommandMap.put(R.id.btn_alt_left, new String[]{"input", "keyevent", "KEYCODE_ALT_LEFT"});
        keyCommandMap.put(R.id.btn_space, new String[]{"input", "keyevent", "KEYCODE_SPACE"});
        keyCommandMap.put(R.id.btn_alt_right, new String[]{"input","keyevent", "KEYCODE_ALT_RIGHT"});
        keyCommandMap.put(R.id.btn_ctrl_right, new String[]{"input", "keyevent", "KEYCODE_CTRL_RIGHT"});

        // Dùng vòng lặp để gán OnClickListener cho tất cả các nút trong Map
        for (Integer buttonId : keyCommandMap.keySet()) {
            View button = findViewById(buttonId);
            if (button != null) {
                button.setOnClickListener(this);
            }
        }
    }

   // Kiểm tra và yêu cầu quyền Shizuku nếu cần
    private void checkShizukuPermission() {
        if (Shizuku.isPreV11()) {
            Toast.makeText(this, "Phiên bản Shizuku quá cũ.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            Shizuku.requestPermission(SHIZUKU_REQUEST_CODE);
        } else {
            Log.d(TAG, "Shizuku đã có quyền.");
        }
    }

    @Override
    public void onClick(View v) {
        String[] command = keyCommandMap.get(v.getId());
        if (command != null) {
            executeShizukuCommand(command);
        }
    }

     private void executeShizukuCommand(String[] command) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // Bước 1: Lấy "Binder" (cầu nối) của Shizuku
                IBinder binder = Shizuku.getBinder();

                // Bước 2: Kiểm tra xem cầu nối có tồn tại và hoạt động không
                // Đây là cách thay thế cho Shizuku.isServiceRunning()
                if (binder == null || !binder.isBinderAlive()) {
                    Log.e(TAG, "Lỗi: Không thể kết nối đến dịch vụ Shizuku. Dịch vụ có đang chạy không?");
                    runOnUiThread(() -> Toast.makeText(this, "Lỗi: Shizuku không chạy", Toast.LENGTH_SHORT).show());
                    return;
                }

                // Bước 3: Chuyển đổi Binder thành đối tượng Service
                IShizukuService service = IShizukuService.Stub.asInterface(binder);

                // Bước 4: Kiểm tra xem service có hợp lệ không
                if (service == null) {
                    Log.e(TAG, "Lỗi: IShizukuService là null sau khi chuyển đổi từ Binder.");
                    return;
                }
                
                // Bước 5: Gọi hàm newProcess từ chính đối tượng service
                // Đây là cách "vượt rào" khi Shizuku.newProcess bị private
                ShizukuRemoteProcess process = service.newProcess(command, null, null);
                
                if (process != null) {
                    process.waitFor();
                    Log.d(TAG, "Thực thi xong, exit code: " + process.exitValue());
                } else {
                    Log.e(TAG, "Lỗi: service.newProcess trả về null.");
                }

            } catch (Exception e) {
                // Bắt các lỗi có thể xảy ra ở tầng thấp (ví dụ: RemoteException)
                Log.e(TAG, "Ngoại lệ khi thực thi lệnh Shizuku: ", e);
            }
        });
    }
}