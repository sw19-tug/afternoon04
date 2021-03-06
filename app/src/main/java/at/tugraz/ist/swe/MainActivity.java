package at.tugraz.ist.swe;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;

import org.zakariya.flyoutmenu.FlyoutMenuView;

import java.io.File;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public List<Integer> tools = new ArrayList<>();
    public LinearLayout layout;
    public ColorPicker foreground;
    public TextPicker textPicker;
    public FlyoutMenuView toolFlyoutMenu;
    public DrawArea drawingArea;
    public static final int IMAGE_CHOOSER = 123;
    public static final int CAMERA_CHOOSER = 111;


    private EditText strokeWidth;
    private ImageButton redoButton;
    private ImageButton undoButton;
    private Display screen;
    private Matrix matrix;
    private Bitmap oldActivity;
    private InputMethodManager manager;
    private String current_color = "";
    private File mTempCameraPhotoFile;
    private static Bitmap toImport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        tools.add(R.drawable.ic_si_glyph_circle);
        tools.add(R.drawable.ic_outline_brush_24px);
        tools.add(R.drawable.ic_si_glyph_line_two_angle_point);
        tools.add(R.drawable.ic_si_glyph_bucket);
        tools.add(R.drawable.ic_si_glyph_erase);
        tools.add(R.drawable.ic_rect);
        tools.add(R.drawable.ic_oval);
        tools.add(R.drawable.ic_baseline_text_fields_24px);
        tools.add(R.drawable.ic_baseline_swap_horiz_24px);
        tools.add(R.drawable.ic_outline_color_lens_24px);
        tools.add(R.drawable.ic_pipette);
        tools.add(R.drawable.ic_outline_add_photo_alternate_24px);
        tools.add(R.drawable.ic_outline_add_a_photo_24px);
        tools.add(R.drawable.ic_si_save);

        layout=findViewById(R.id.main_canvas_view);

        manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

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


        textPicker = new TextPicker(this);
        textPicker.setOnTextAppliedListener(new TextPicker.TextApprovedListener() {
            @Override
            public void onTextApproved(String text) {

                LinearLayout strokeWidthLayout = findViewById(R.id.strokeWidthLayout);
                drawingArea.setTool(new TextTool(getApplicationContext(), foreground.getColor(), Integer.parseInt(strokeWidth.getText().toString()), text));
                strokeWidthLayout.setVisibility(View.VISIBLE);

            }
        });

        layout.addView(drawingArea);

        strokeWidth = findViewById(R.id.strokewidth_text);
        redoButton = findViewById(R.id.buttonRedo);
        undoButton = findViewById(R.id.buttonUndo);
        redoButton.setEnabled(false);
        undoButton.setEnabled(false);

        drawingArea.setStepListener(new DrawArea.StepsListener() {
            @Override
            public void onTouched(boolean undo, boolean redo) {
                undoButton.setEnabled(undo);
                redoButton.setEnabled(redo);
            }
        });

        screen = getWindowManager().getDefaultDisplay();

        final OrientationEventListener rotation = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                if(BitmapCache.rotation != screen.getRotation()) {
                    for (int count = 0; 0 < BitmapCache.max_undo_steps; count++) {
                        oldActivity = BitmapCache.mMemoryCache.get("step" + Integer.toString(count));
                        if(oldActivity == null)
                            break;
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
                                } else if(BitmapCache.rotation == 2)
                                {
                                    matrix = new Matrix();
                                    matrix.postRotate(180);
                                }
                            } else if (BitmapCache.oldRotation == 1) {
                                if (BitmapCache.rotation == 0) {
                                    matrix = new Matrix();
                                    matrix.postRotate(90);
                                } else if (BitmapCache.rotation == 3) {
                                    matrix = new Matrix();
                                    matrix.postRotate(180);
                                }
                                else if(BitmapCache.rotation == 2)
                                {
                                    matrix = new Matrix();
                                    matrix.postRotate(-90);
                                }
                            } else if (BitmapCache.oldRotation == 3) {
                                if (BitmapCache.rotation == 0) {
                                    matrix = new Matrix();
                                    matrix.postRotate(-90);
                                } else if (BitmapCache.rotation == 1) {
                                    matrix = new Matrix();
                                    matrix.postRotate(180);
                                }
                                else if(BitmapCache.rotation == 2)
                                {
                                    matrix = new Matrix();
                                    matrix.postRotate(90);
                                }
                            }
                            else if(BitmapCache.oldRotation == 2)
                            {
                                if(BitmapCache.rotation == 1)
                                {
                                    matrix = new Matrix();
                                    matrix.postRotate(90);
                                }
                                else if(BitmapCache.rotation == 0)
                                {
                                    matrix = new Matrix();
                                    matrix.postRotate(180);
                                }
                                if(BitmapCache.rotation == 3)
                                {
                                    matrix = new Matrix();
                                    matrix.postRotate(-90);
                                }

                            }
                            BitmapCache.mMemoryCache.put("step" + Integer.toString(count), Bitmap.createBitmap(oldActivity, 0, 0, oldActivity.getWidth(), oldActivity.getHeight(), matrix, true));
                        }
                    }
                    if((BitmapCache.oldRotation == 3 && BitmapCache.rotation == 1) || (BitmapCache.oldRotation == 1 && BitmapCache.rotation == 3) ||
                            (BitmapCache.oldRotation == 0 && BitmapCache.rotation == 2) || (BitmapCache.oldRotation == 2 && BitmapCache.rotation == 0))
                        drawingArea.invalidate();
                }
            }
        };
        rotation.enable();
        strokeWidth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                drawingArea.setHandleToucheEvents(!hasFocus);
            }
        });

        strokeWidth.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(strokeWidth.getText().length() == 0)
                {
                    strokeWidth.setText("10");
                    drawingArea.setSize(10);
                }
                else if(Integer.parseInt(strokeWidth.getText().toString()) > 255)
                {
                    strokeWidth.setText("255");
                    drawingArea.setSize(255);
                }
                else
                {
                    drawingArea.setSize(Integer.parseInt(strokeWidth.getText().toString()));
                }
                LinearLayout layout = findViewById(R.id.strokeWidthLayout);
                layout.requestFocus();
                manager.hideSoftInputFromWindow(strokeWidth.getWindowToken(), 0);
                return false;
            }
        });

        int locale = TextUtils.getLayoutDirectionFromLocale(getResources().getConfiguration().locale);

        if(locale == 0 && (BitmapCache.rotation == 0 || BitmapCache.rotation == 2))
        {
            ((Button)findViewById(R.id.strokewidth_left)).setText("+");
            ((Button)findViewById(R.id.strokewidth_right)).setText("-");
        }
        else if(locale == 1)
        {
            ((Button)findViewById(R.id.strokewidth_left)).setText("-");
            ((Button)findViewById(R.id.strokewidth_right)).setText("+");
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle state)
    {
        super.onSaveInstanceState(state);
        current_color = foreground.getHexColorString();
        state.putInt("stroke_width", Integer.parseInt(strokeWidth.getText().toString()));

        state.putBoolean("color", false);
        if(foreground.isShowing())
        {
            state.putBoolean("color", true);
            state.putInt("tool", drawingArea.getPaintingTool().getId());
            state.putString("current_color", current_color);
        }
        else if(toolFlyoutMenu.getSelectedMenuItem() != null)
            state.putInt("tool", drawingArea.getPaintingTool().getId());
        else
            state.putInt("tool", R.drawable.ic_si_glyph_circle);

        if(textPicker.isShowing())
        {
            state.putString("reset", textPicker.getText());
            state.putBoolean("text", true);
        }

        String text = textPicker.dismissDialogue();
        state.putString("textPicker", text);
        state.putBoolean("undo", undoButton.isEnabled());
        state.putBoolean("redo", redoButton.isEnabled());
    }

    @Override
    protected void onRestoreInstanceState(Bundle state)
    {
        super.onRestoreInstanceState(state);

        drawingArea.setSize(state.getInt("stroke_width", 10));
        strokeWidth.setText(String.format("%02d", state.getInt("stroke_width", 10)));
        int id = state.getInt("tool");
        int amount_items = toolFlyoutMenu.getAdapter().getCount();
        for(int counter = 0; 0 < amount_items; counter++)
        {
            FlyoutMenuView.MenuItem result = toolFlyoutMenu.getAdapter().getItem(counter);
            if(((FlyoutToolbar.MenuItemImage)result).getID() == id)
            {
                toolFlyoutMenu.setSelectedMenuItem(result);
                break;
            }
        }

        if(state.getBoolean("color"))
        {
            foreground.show();
        }
        String oldColor = state.getString("current_color");
        foreground.setHexString(oldColor);
        if(state.getBoolean("text"))
        {
            textPicker.setText(state.getString("reset"));
            textPicker.setTextBox(state.getString("textPicker"));
            textPicker.show();
        }
        else
        {
          textPicker.setText(state.getString("textPicker"));
        }
        redoButton.setEnabled(state.getBoolean("redo"));
        undoButton.setEnabled(state.getBoolean("undo"));
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        foreground.dismissDialogue();
    }

    public void showTool(int shown_tool)
    {
        LinearLayout strokeWidthLayout = findViewById(R.id.strokeWidthLayout);
        LinearLayout undoRedoLayout = findViewById(R.id.undoLayout);
        undoRedoLayout.setVisibility(View.INVISIBLE);
        drawingArea.setHandleToucheEvents(true);
        drawingArea.getPixelColor(false);
        switch(shown_tool) {
            case R.drawable.ic_si_glyph_circle:
                drawingArea.setTool(new Circle(foreground.getColor(), Integer.parseInt(strokeWidth.getText().toString())));
                strokeWidthLayout.setVisibility(View.VISIBLE);
                break;
            case R.drawable.ic_si_glyph_line_two_angle_point:
                drawingArea.setTool(new Line(foreground.getColor(), Integer.parseInt(strokeWidth.getText().toString())));
                strokeWidthLayout.setVisibility(View.VISIBLE);
                break;
            case R.drawable.ic_outline_color_lens_24px:
                foreground.show();
                break;
            case R.drawable.ic_outline_brush_24px:
                strokeWidthLayout.setVisibility(View.VISIBLE);
                drawingArea.setTool(new PathTool(foreground.getColor(), Integer.parseInt(strokeWidth.getText().toString())));
                break;
            case R.drawable.ic_si_glyph_erase:
                strokeWidthLayout.setVisibility(View.VISIBLE);
                drawingArea.setTool(new PathTool(Color.WHITE, Integer.parseInt(strokeWidth.getText().toString())));
                break;
            case R.drawable.ic_rect:
                strokeWidthLayout.setVisibility(View.INVISIBLE);
                drawingArea.setTool(new ShapeTool(foreground.getColor(), "rect"));
                break;
            case R.drawable.ic_oval:
                strokeWidthLayout.setVisibility(View.INVISIBLE);
                drawingArea.setTool(new ShapeTool(foreground.getColor(), "oval"));
                break;
            case R.drawable.ic_si_glyph_bucket:
                strokeWidthLayout.setVisibility(View.INVISIBLE);
                drawingArea.setTool(new FillBucket(foreground.getColor()));
                break;
            case R.drawable.ic_outline_add_photo_alternate_24px:
                strokeWidthLayout.setVisibility(View.INVISIBLE);
                showFileChooser();
                break;
            case R.drawable.ic_pipette:
                strokeWidthLayout.setVisibility(View.INVISIBLE);
                drawingArea.setHandleToucheEvents(false);
                drawingArea.getPixelColor(true);
                drawingArea.setTool(new Pipette(this, foreground, drawingArea));
                break;
            case R.drawable.ic_si_save:
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Unable to save Image! - Missing Permissions", Toast.LENGTH_SHORT).show();
                }
                else{
                    String success = MediaStore.Images.Media.insertImage(getContentResolver(), drawingArea.getBitmap(), "PrintZ" , "");
                    if(success!=null)
                        Toast.makeText(this,"Image saved", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this,"Unable to save Image!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.drawable.ic_baseline_text_fields_24px:
                textPicker.show();
                break;
            case R.drawable.ic_outline_add_a_photo_24px:
                int curvisibility = strokeWidthLayout.getVisibility();
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Unable to open Camera! - Missing Permissions", Toast.LENGTH_SHORT).show();
                }
                else {
                    strokeWidthLayout.setVisibility(View.INVISIBLE);
                    getImageFromCamera();
                }
                break;
            case R.drawable.ic_baseline_swap_horiz_24px:
                strokeWidthLayout.setVisibility(View.INVISIBLE);
                undoRedoLayout.setVisibility(View.VISIBLE);
                break;
            default:
                    strokeWidthLayout.setVisibility(View.VISIBLE);
                    drawingArea.setTool(new Circle(foreground.getColor(), Integer.parseInt(strokeWidth.getText().toString())));
            break;
        }
    }

    private void getImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            File exportDir = new File(Environment.getExternalStorageDirectory(), "TempFolder");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            } else {
                exportDir.delete();
            }
            mTempCameraPhotoFile = new File(exportDir, "/" + UUID.randomUUID().toString().replaceAll("-", "") + ".jpg");
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempCameraPhotoFile));
            startActivityForResult(takePictureIntent, CAMERA_CHOOSER);
        }

    }
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select an image"), IMAGE_CHOOSER);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a file manager.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == IMAGE_CHOOSER) {
                if (resultCode == RESULT_OK) {

                    Uri uri = data.getData();
                    try {
                        toImport = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        drawingArea.setTool(new ImageImportTool(toImport, this));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        if (requestCode == CAMERA_CHOOSER && resultCode == RESULT_OK)
        {

            Uri uri = Uri.fromFile(mTempCameraPhotoFile);
            try {
                toImport = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                drawingArea.setTool(new ImageImportTool(toImport, this));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void undoStep(View element)
    {
        drawingArea.undoStep();
        if(BitmapCache.max_undo_steps == 0)
            redoButton.setEnabled(false);
        redoButton.setEnabled(true);
        if(BitmapCache.redo_overflow)
        {
            int position_to_check_against = (BitmapCache.dead_zone_start > -1) ? BitmapCache.dead_zone_start : BitmapCache.array_position;
            if(position_to_check_against == 0)
            {
                if (BitmapCache.oldBitmap == 1)
                    undoButton.setEnabled(false);
                else
                    undoButton.setEnabled(true);
            }
            else {
                if (BitmapCache.oldBitmap == position_to_check_against + 1)
                    undoButton.setEnabled(false);
                else
                    undoButton.setEnabled(true);
            }
        }
        else
        {
            if(BitmapCache.oldBitmap == 0)
            {
                undoButton.setEnabled(false);
            }
            else
            {
                undoButton.setEnabled(true);
            }
        }
    }

    public void redoStep(View element)
    {
        drawingArea.redoStep();
        undoButton.setEnabled(true);
        if(BitmapCache.redo_overflow)
        {
            if(BitmapCache.array_position == 0)
            {
               if(BitmapCache.nextBitmap == BitmapCache.max_undo_steps - 1)
                   redoButton.setEnabled(false);
               else
                   redoButton.setEnabled(true);
            }
            else
            {
                if(BitmapCache.nextBitmap == BitmapCache.array_position - 1)
                    redoButton.setEnabled(false);
                else
                    redoButton.setEnabled(true);
            }
            }
        else
        {
            if(BitmapCache.nextBitmap == BitmapCache.array_position - 1)
            {
                redoButton.setEnabled(false);
            }
            else
            {
                redoButton.setEnabled(true);
            }
        }
    }

    public void changeStrokeWidth(View element)
    {
        int locale = TextUtils.getLayoutDirectionFromLocale(getResources().getConfiguration().locale);
        int strokeWidthNr = Integer.parseInt(strokeWidth.getText().toString());

        if(element.getId() == R.id.strokewidth_left && (locale == 0))
        {
            strokeWidthNr++;
        }
        else if(element.getId() == R.id.strokewidth_left && (locale == 1))
        {
            strokeWidthNr--;
        }
        else if(element.getId() == R.id.strokewidth_right && (locale == 0))
        {
            strokeWidthNr--;
        }
        else if(element.getId() == R.id.strokewidth_right && (locale == 1))
        {
            strokeWidthNr++;
        }

        strokeWidthNr = checkStrokeWidthNumber(strokeWidthNr);
        strokeWidth.setText(String.format("%02d",strokeWidthNr));
        drawingArea.setSize(strokeWidthNr);
    }

    public int checkStrokeWidthNumber(int new_number)
    {
        if(new_number <= 0)
        {
            return 1;
        }
        else if(new_number >= 255)
        {
            return 255;
        }
        else
            return new_number;
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }
}
