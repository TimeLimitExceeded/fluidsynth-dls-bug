package com.example.fluidsynthandroidhelloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String soundfontFileName = "sf2_example.sf2";
        String tempFileName = "tmp_" + soundfontFileName;

        try {
            InputStream is = getAssets().open(soundfontFileName);
            writeFileToPrivateStorage(is, tempFileName);
            String tempSoundfontPath = getApplicationContext().getFilesDir().toString() + "/" + tempFileName;
            boolean success = fluidsynthHelloWorld(tempSoundfontPath);

            // Example of a call to a native method
            TextView tv = findViewById(R.id.sample_text);
            tv.setText(success ? "Success" : "Failure");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Write input stream to file
     * @param is
     * @param toFile
     * @throws IOException
     */
    private void writeFileToPrivateStorage(InputStream is, String toFile) throws IOException {
        int bytes_read;
        byte[] buffer = new byte[4096];
        FileOutputStream fos = openFileOutput(toFile, Context.MODE_PRIVATE);
        while ((bytes_read = is.read(buffer)) != -1) {
            fos.write(buffer, 0, bytes_read); // write
        }
        fos.close();
        is.close();
    }

    public native boolean fluidsynthHelloWorld(String soundfontPath);
}
