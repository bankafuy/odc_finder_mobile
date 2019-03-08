package com.perusahaan.fullname.odcfinder.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

/**
 * Created by Full Name on 3/8/2019.
 */

public class CircleTransformation implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);

        if(squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        BitmapShader bitmapShader = new BitmapShader(
                squaredBitmap,
                BitmapShader.TileMode.CLAMP,
                BitmapShader.TileMode.CLAMP
        );

        paint.setShader(bitmapShader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        squaredBitmap.recycle();

        return bitmap;
    }

        @Override
        public String key() {
        return "circle";
    }
}
