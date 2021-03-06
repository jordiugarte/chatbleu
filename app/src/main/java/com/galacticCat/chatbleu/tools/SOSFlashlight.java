package com.galacticCat.chatbleu.tools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.v4.app.ActivityCompat;

import static android.content.Context.CAMERA_SERVICE;

public class SOSFlashlight {

    private CameraManager cameraManager;
    private Thread t;

    public SOSFlashlight(final Activity activity, Context context) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 60);
        cameraManager = (CameraManager) context.getSystemService(CAMERA_SERVICE);
        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String cameraID = null;
                                try {
                                    cameraID = cameraManager.getCameraIdList()[0];
                                } catch (CameraAccessException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    cameraManager.setTorchMode(cameraID, true);
                                } catch (CameraAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread.sleep(200);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String cameraID = null;
                                try {
                                    cameraID = cameraManager.getCameraIdList()[0];
                                } catch (CameraAccessException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    cameraManager.setTorchMode(cameraID, false);
                                } catch (CameraAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread.sleep(200);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void stopFlashLight() {
        t.interrupt();

    }

    public void turnFlashLight() {
        t.start();
    }

}
