package at.tugraz.ist.swe;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.zakariya.flyoutmenu.FlyoutMenuView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<Integer> tools = new ArrayList<>();
    public LinearLayout layout;
    public ColorPicker foreground;
    public FlyoutMenuView toolFlyoutMenu;
    public DrawArea drawingArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tools.add(R.drawable.ic_outline_color_lens_24px);
        tools.add(R.drawable.ic_si_glyph_circle);
        tools.add(R.drawable.ic_outline_brush_24px);
        tools.add(R.drawable.ic_si_glyph_line_two_angle_point);
        tools.add(R.drawable.ic_si_save);

        layout=findViewById(R.id.main_canvas_view);


        setupToolbar();

        foreground = new ColorPicker(this);
        foreground.setOnColorAppliedListener(new ColorPicker.ColorAppliedListener() {
            @Override
            public void onColorApplied(int color) {
                drawingArea.getPaintingTool().setColor(color);
            }
        });

        drawingArea = new DrawArea(this);

        layout.addView(drawingArea);
    }

    private void setupToolbar() {
        toolFlyoutMenu = findViewById(R.id.toolFlyoutMenu);

        List<FlyoutToolbar.MenuItemImage> menuItemsImages = new ArrayList<>();

        for (int item : this.tools)
        {
            menuItemsImages.add(new FlyoutToolbar.MenuItemImage(item, item, this.getApplicationContext()));
        }
        DisplayMetrics display;
        display = this.getApplicationContext().getResources().getDisplayMetrics();

        int width = toolFlyoutMenu.getItemWidth();
        int margin = toolFlyoutMenu.getItemMargin() * 2;

        int colums;
        for (colums = 5; colums >= 0; --colums)
        {
            if(width * colums + margin * colums < display.widthPixels)
                break;
        }

        toolFlyoutMenu.setLayout(new FlyoutMenuView.GridLayout(colums, FlyoutMenuView.GridLayout.UNSPECIFIED));
        toolFlyoutMenu.setAdapter(new FlyoutMenuView.ArrayAdapter<>(menuItemsImages));

        final FlyoutToolbar.ButtonRenderer renderer = new FlyoutToolbar.ButtonRenderer(R.drawable.ic_outline_apps_24px, this.getApplicationContext());
        toolFlyoutMenu.setButtonRenderer(renderer);

        toolFlyoutMenu.setSelectionListener(new FlyoutMenuView.SelectionListener() {
            @Override
            public void onItemSelected(FlyoutMenuView flyoutMenuView, FlyoutMenuView.MenuItem item) {

                FlyoutToolbar.MenuItemImage selected = (FlyoutToolbar.MenuItemImage) item;

                for(int counter = 0; counter < tools.size(); counter++)
                {
                    if(tools.get(counter) == selected.getID()) {
                        showTool(selected.getID());
                        break;
                    }
                }
            }

            @Override
            public void onDismissWithoutSelection(FlyoutMenuView flyoutMenuView) {
            }
        });
    }

    public void showTool(int shown_tool)
    {
        switch(shown_tool) {
            case R.drawable.ic_si_glyph_circle:
                drawingArea.setTool(new Circle(foreground.getColor(), 10));
                break;
            case R.drawable.ic_si_glyph_line_two_angle_point:
                drawingArea.setTool(new Line(foreground.getColor(), 10));
                break;
            case R.drawable.ic_outline_color_lens_24px:
                foreground.show();
                break;
            case R.drawable.ic_outline_brush_24px:
                drawingArea.setTool(new PathTool(foreground.getColor(), 10));
                break;
            case R.drawable.ic_si_save:
                Toast.makeText(this,"Image saved", Toast.LENGTH_SHORT).show();
                MediaStore.Images.Media.insertImage(getContentResolver(), drawingArea.createBitmap(), "PrintZ" , "");
                //this.saveImage(drawingArea.createBitmap(), "test");
            default:
                drawingArea.setTool(new Circle(foreground.getColor(), 10));
                break;
        }
    }


    private void saveImage(Bitmap finalBitmap, String image_name) {
        File root = Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath()+"/DCIM/Camera/img"+new Date().toString()+".png");
        try
        {
            file.createNewFile();
            FileOutputStream ostream = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
