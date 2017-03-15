package com.suhang.opengldemo.interfaces;

import com.suhang.opengldemo.function.Camera;

/**
 * Created by 苏杭 on 2017/3/15 15:45.
 */

public interface CanTranform {
    Camera getCamera();

    float getDeltaTime();

    void moveScreen(boolean isPress, int direction);

}
