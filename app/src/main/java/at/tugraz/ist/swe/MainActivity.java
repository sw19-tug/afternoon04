package at.tugraz.ist.swe;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlertDialog.Builder bld_ColorPicker = new AlertDialog.Builder(this);
        bld_ColorPicker.setTitle(R.string.color_picker_title);
        AlertDialog dlg_color = bld_ColorPicker.create();
        dlg_color.show();
    }
}
