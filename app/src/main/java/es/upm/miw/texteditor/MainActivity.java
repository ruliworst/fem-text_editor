package es.upm.miw.texteditor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView tvFileContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.editText = findViewById(R.id.etInputText);
        this.tvFileContent = findViewById(R.id.tvFileContent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showContent();
    }

    public String getFileName() {
        return getResources().getString(R.string.default_filename);
    }

    public void addContentToFile(View view) {
        try {
            FileOutputStream fos = openFileOutput(this.getFileName(), Context.MODE_APPEND);
            byte[] contentToAdd = editText.getText().toString().getBytes();
            if (contentToAdd != null && contentToAdd.length > 0) {
                fos.write(contentToAdd);
                fos.write('\n');
                this.showContent();
            }
            fos.close();
        } catch (Exception exception) {
            Log.e("MiW", "FILE I/O ERROR" + exception.getMessage());
            exception.printStackTrace();
        }
        editText.setText(null);
    }

    public void showContent() {
        TextView tvFileContent = findViewById(R.id.tvFileContent);
        tvFileContent.setText("");
        Boolean hayContenido = false;
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(openFileInput(this.getFileName())));
            String text = br.readLine();
            while (text != null) {
                hayContenido = true;
                tvFileContent.append(text + '\n');
                text = br.readLine();
            }
            br.close();
            Log.i("MiW", "Show content.");
        } catch (Exception exception) {
            Log.e("MiW", "FILE I/O ERROR" + exception.getMessage());
            exception.printStackTrace();
        }
        if (!hayContenido) {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    "El fichero está vacio",
                    Snackbar.LENGTH_SHORT
            );
        }
    }
}