package at.tugraz.ist.swe;


import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import org.zakariya.flyoutmenu.FlyoutMenuView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<Integer> tools = new ArrayList<>();
    public FrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Tool add_photo = new Tool(R.drawable.ic_outline_add_a_photo_24px);
        Tool add_photo_alternate = new Tool(R.drawable.ic_outline_add_photo_alternate_24px);
        Tool brush = new Tool(R.drawable.ic_outline_brush_24px);
        Tool color_picker = new Tool(R.drawable.ic_outline_color_lens_24px);
        Tool square = new Tool(R.drawable.ic_outline_crop_square_24px);
        Tool bucket = new Tool(R.drawable.ic_si_glyph_bucket);
        Tool circle = new Tool(R.drawable.ic_si_glyph_circle);
        Tool erase = new Tool(R.drawable.ic_si_glyph_erase);
        Tool line = new Tool(R.drawable.ic_si_glyph_line_two_angle_point);*/

        tools.add(R.drawable.ic_outline_add_a_photo_24px);
        tools.add(R.drawable.ic_outline_add_photo_alternate_24px);
        tools.add(R.drawable.ic_outline_brush_24px);
        tools.add(R.drawable.ic_outline_color_lens_24px);
        tools.add(R.drawable.ic_outline_crop_square_24px);
        tools.add(R.drawable.ic_si_glyph_bucket);
        tools.add(R.drawable.ic_si_glyph_circle);
        tools.add(R.drawable.ic_si_glyph_erase);
        tools.add(R.drawable.ic_si_glyph_line_two_angle_point);

        //this.setupToolbar();

        layout=(FrameLayout)findViewById(R.id.main_canvas_view);


        setupToolbar();

        ColorPicker foreground = new ColorPicker(this);
        foreground.show();

    }

    private void setupToolbar() {
        FlyoutMenuView toolFlyoutMenu = findViewById(R.id.toolFlyoutMenu);

        List<FlyoutToolbar.MenuItemImage> menuItemsImages = new ArrayList<>();

        for (int item : this.tools)
        {
            menuItemsImages.add(new FlyoutToolbar.MenuItemImage(menuItemsImages.size(), item, this.getApplicationContext()));
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

                showTool(tools.get(selected.getID()));
            }

            @Override
            public void onDismissWithoutSelection(FlyoutMenuView flyoutMenuView) {
            }
        });
    }

    public void showTool(int shown_tool)
    {
        DrawPointView drawPointView = new DrawPointView(MainActivity.this);
        drawPointView.setId(R.id.draw_point_view);

        if(shown_tool == R.drawable.ic_outline_brush_24px)
        {
            layout.addView(drawPointView);
        }
    }
}
