package at.tugraz.ist.swe;


import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.zakariya.flyoutmenu.FlyoutMenuView;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.X;

public class MainActivity extends AppCompatActivity {

    public List<Integer> tools = new ArrayList<>();
    public LinearLayout layout;
    public ColorPicker foreground;
    public FlyoutMenuView toolFlyoutMenu;
    public DrawArea drawingArea;
    private EditText strokeWidth;
    private Display screen;
    private Matrix matrix;
    private Bitmap oldActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        tools.add(R.drawable.ic_outline_color_lens_24px);
        tools.add(R.drawable.ic_si_glyph_circle);

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
        drawingArea.setWillNotDraw(false);

        layout.addView(drawingArea);

        strokeWidth = findViewById(R.id.strokewidth_text);

        screen = getWindowManager().getDefaultDisplay();

        final OrientationEventListener rotation = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                if(BitmapCache.rotation != screen.getRotation()) {
                    oldActivity = BitmapCache.mMemoryCache.get("oldBitmap");
                    BitmapCache.oldRotation = BitmapCache.rotation;
                    BitmapCache.rotation = screen.getRotation();

                    if (oldActivity != null) {

                        if (BitmapCache.oldRotation == 0) {
                            if (BitmapCache.rotation == 1) {
                                matrix = new Matrix();
                                matrix.postRotate(-90);
                            } else if (BitmapCache.rotation == 3) {
                                matrix = new Matrix();
                                matrix.postRotate(90);
                            }
                        } else if (BitmapCache.oldRotation == 1) {
                            if (BitmapCache.rotation == 0) {
                                matrix = new Matrix();
                                matrix.postRotate(90);
                            } else if (BitmapCache.rotation == 3) {
                                matrix = new Matrix();
                                matrix.postRotate(180);
                            }
                        } else if (BitmapCache.oldRotation == 3) {
                            if (BitmapCache.rotation == 0) {
                                matrix = new Matrix();
                                matrix.postRotate(-90);
                            } else if (BitmapCache.rotation == 1) {
                                matrix = new Matrix();
                                matrix.postRotate(180);
                            }
                        }
                        BitmapCache.mMemoryCache.put("oldBitmap", Bitmap.createBitmap(oldActivity, 0, 0, oldActivity.getWidth(), oldActivity.getHeight(), matrix, true));
                        if(BitmapCache.oldRotation == 3 && BitmapCache.rotation == 1 || BitmapCache.oldRotation == 1 && BitmapCache.rotation == 3)
                            drawingArea.invalidate();
                    }
                }
            }
        };

        rotation.enable();

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
            case R.drawable.ic_outline_color_lens_24px:
                foreground.show();
                break;
            default:
                drawingArea.setTool(new Circle(foreground.getColor(), 10));
                break;
        }
    }

    public void increaseStrokeWidth(View element)
    {
        int strokeWidthNr = Integer.parseInt(strokeWidth.getText().toString());
        strokeWidthNr++;
        strokeWidthNr = checkStrokeWidthNumber(strokeWidthNr);
        strokeWidth.setText(String.format("%02d",strokeWidthNr));
        PaintingTool tool = drawingArea.getPaintingTool();
        tool.setSize(strokeWidthNr);
        drawingArea.setTool(tool);
    }

    public void decreaseStrokeWidth(View element)
    {
        int strokeWidthNr = Integer.parseInt(strokeWidth.getText().toString());
        strokeWidthNr--;
        strokeWidthNr = checkStrokeWidthNumber(strokeWidthNr);
        strokeWidth.setText(String.format("%02d",strokeWidthNr));
        PaintingTool tool = drawingArea.getPaintingTool();
        tool.setSize(strokeWidthNr);
        drawingArea.setTool(tool);
    }

    public int checkStrokeWidthNumber(int new_number)
    {
        if(new_number <= 0)
        {
            return 1;
        }
        else if(new_number >= 100)
        {
            return 99;
        }
        else
            return new_number;
    }
}
