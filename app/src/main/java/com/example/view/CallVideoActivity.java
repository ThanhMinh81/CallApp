package com.example.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myappcall.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CallVideoActivity extends AppCompatActivity {

    ImageView pressEndCall;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private String cameraId;
    private TextureView textureView;
    private CameraDevice cameraDevice;
    private Size previewSize;
    private CameraCaptureSession cameraCaptureSession;
    private CaptureRequest.Builder previewCaptureRequestBuilder;
    private CaptureRequest previewCaptureRequest;
    private TextureView.SurfaceTextureListener surfaceTextureListener;

    private CameraCaptureSession.CaptureCallback cameraSessionCaptureCallback;

    private CameraDevice.StateCallback cameraDeviceStateCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_call_video);

        pressEndCall = this.<ImageView>findViewById(R.id.imgEndCall);

        textureView = this.<TextureView>findViewById(R.id.textureView);

        initEventCamera();

        VideoView videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video1;
        videoView.setVideoURI(Uri.parse(videoPath));

        videoView.setOnErrorListener((mp, what, extra) -> {
            return false;
        });

        videoView.setOnCompletionListener(mp -> {
        });

        // Bắt đầu phát video
        videoView.start();


        pressEndCall.setOnClickListener(v -> {

            Intent intent = new Intent(CallVideoActivity.this, MainActivity.class);
            startActivity(intent);

        });


    }

    private void initEventCamera() {
        surfaceTextureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                setupCamera(width, height);
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
            }
        };

        cameraDeviceStateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(CameraDevice camera) {

                cameraDevice = camera;

                Toast.makeText(getApplicationContext(), "Camera open", Toast.LENGTH_LONG).show();

                // Hiển thị hình ảnh thu về từ Camera lên màn hình
                createCameraPreviewSession();

            }

            @Override
            public void onDisconnected(CameraDevice camera) {
                camera.close();
                cameraDevice = null;
            }

            @Override
            public void onError(CameraDevice camera, int error) {
                camera.close();
                cameraDevice = null;
            }
        };

        cameraSessionCaptureCallback = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                super.onCaptureStarted(session, request, timestamp, frameNumber);
            }

            @Override
            public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);
            }

            @Override
            public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
                super.onCaptureFailed(session, request, failure);
            }
        };

    }


    private void openCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            // mở kết nối tới Camera của thiết bị
            // các hành động trả về sẽ dc thực hiện trong "cameraDeviceStateCallback"
            // tham số thứ 3 của hàm openCamera là 1 "Handler"
            // nhưng hiện tại ở đây chúng ta chưa cần thiết nên mình để nó là "null"
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cameraManager.openCamera(cameraId, cameraDeviceStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void closeCamera() {

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }

        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }

    }

    private void setupCamera(int width, int height) {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(id);

                // Sử dụng camera trước (LENS_FACING_FRONT)
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    // Lấy kích thước cố định cho preview
                    previewSize = getFixedPreviewsSize(map.getOutputSizes(SurfaceTexture.class));
                    cameraId = id;
                    break; // thoát vòng lặp sau khi tìm thấy camera trước
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // Lấy kích thước cố định cho preview (ví dụ: 640x480)
    private Size getFixedPreviewsSize(Size[] mapSizes) {
        for (Size option : mapSizes) {
            if (option.getWidth() == 640 && option.getHeight() == 480) {
                return option;
            }
        }
        // Nếu không tìm thấy kích thước cố định nào, chỉ đơn giản chọn kích thước đầu tiên trong danh sách
        return mapSizes[0];
    }


    @Override
    protected void onPause() {
        closeCamera();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (textureView.isAvailable()) {
            setupCamera(textureView.getWidth(), textureView.getHeight());
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }
    }


    private Size getPreferredPreviewsSize(Size[] mapSize, int width, int height) {
        List<Size> collectorSize = new ArrayList<>();
        for (Size option : mapSize) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    collectorSize.add(option);
                }
            } else {
                if (option.getWidth() > height && option.getHeight() > width) {
                    collectorSize.add(option);
                }
            }
        }
        if (collectorSize.size() > 0) {
            return Collections.min(collectorSize, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getHeight() * rhs.getWidth());
                }
            });
        }
        return mapSize[0];
    }

    // Khởi tạo hàm để hiển thị hình ảnh thu về từ Camera lên TextureView
    private void createCameraPreviewSession() {
        try {
            SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            Surface previewSurface = new Surface(surfaceTexture);

            // Khởi tạo CaptureRequestBuilder từ cameraDevice với template truyền vào là
            // "CameraDevice.TEMPLATE_PREVIEW"
            // Với template này thì CaptureRequestBuilder chỉ thực hiện View mà thôi
            previewCaptureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            // Thêm đích đến cho dữ liệu lấy về từ Camera
            // Đích đến này phải nằm trong danh sách các đích đến của dữ liệu
            // được định nghĩa trong cameraDevice.createCaptureSession() "phần định nghĩa này ngay bên dưới"
            previewCaptureRequestBuilder.addTarget(previewSurface);

            // Khởi tạo 1 CaptureSession
            // Arrays.asList(previewSurface) List danh sách các Surface
            // ( đích đến của hình ảnh thu về từ Camera)
            // Ở đây đơn giản là chỉ hiển thị hình ảnh thu về từ Camera nên chúng ta chỉ có 1 đối số.
            // Nếu bạn muốn chụp ảnh hay quay video vvv thì
            // ta có thể truyền thêm các danh sách đối số vào đây
            // Vd: Arrays.asList(previewSurface, imageReader)
            cameraDevice.createCaptureSession(Arrays.asList(previewSurface),
                    // Hàm Callback trả về kết quả khi khởi tạo.
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(CameraCaptureSession session) {
                            if (cameraDevice == null) {
                                return;
                            }
                            try {
                                // Khởi tạo CaptureRequest từ CaptureRequestBuilder
                                // với các thông số đã được thêm ở trên
                                previewCaptureRequest = previewCaptureRequestBuilder.build();
                                cameraCaptureSession = session;
                                cameraCaptureSession.setRepeatingRequest(
                                        previewCaptureRequest,
                                        cameraSessionCaptureCallback,
                                        null);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession session) {
                            Toast.makeText(getApplicationContext(),
                                    "Create camera session fail", Toast.LENGTH_SHORT).show();
                        }
                    },
                    // Đối số thứ 3 của hàm là 1 Handler,
                    // nhưng do hiện tại chúng ta chưa làm gì nhiều nên mình tạm thời để là null
                    null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


}