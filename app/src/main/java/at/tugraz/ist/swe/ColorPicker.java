package at.tugraz.ist.swe;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class ColorPicker {

    public interface ColorAppliedListener
    {
        void onColorApplied(int color);
    }

    private ColorAppliedListener listener;

    private SeekBar seekBar_Red;
    private SeekBar seekBar_Green;
    private SeekBar seekBar_Blue;
    private SeekBar seekBar_Alpha;

    private EditText textBox_Red;
    private EditText textBox_Green;
    private EditText textBox_Blue;
    private EditText textBox_Alpha;
    private EditText textBox_Hex;

    private AlertDialog dlg_color;
    private Context context;

    private View background_color;

    private int color_r;
    private int color_g;
    private int color_b;
    private int color_a;

    private InputMethodManager manager;
    Button btnApply;
    Button btnReset;
    Button btnCancel;



    public ColorPicker(Context context)
    {
        this.context = context;
        this.manager = (InputMethodManager)this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
        AlertDialog.Builder bld_ColorPicker = new AlertDialog.Builder(context);
        color_r = 0;
        color_g = 0;
        color_b = 0;
        color_a = 255;

        this.listener = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dlg_view = inflater.inflate(R.layout.color_picker_dialog,null);

        bld_ColorPicker.setTitle(R.string.color_picker_title);
        bld_ColorPicker.setView(dlg_view);

        bld_ColorPicker.setNegativeButton(R.string.color_picker_button_cancel, null);

        bld_ColorPicker.setPositiveButton(R.string.color_picker_button_apply, null);

        bld_ColorPicker.setNeutralButton(R.string.color_picker_button_reset, null);

        dlg_color = bld_ColorPicker.create();
    }

    public int getColor()
    {
        return Color.argb(this.color_a, this.color_r, this.color_g, this.color_b);
    }

    public void setColor(int c_a, int c_r, int c_g, int c_b)
    {
        this.color_a = c_a;
        this.color_r = c_r;
        this.color_g = c_g;
        this.color_b = c_b;
    }

    public void setOnColorAppliedListener(ColorAppliedListener listener){
        this.listener = listener;
    }

    private int getPreviewColor() {
        return Color.argb(seekBar_Alpha.getProgress(), seekBar_Red.getProgress(), seekBar_Green.getProgress(), seekBar_Blue.getProgress());
    }

    public void show()
    {
        dlg_color.show();
        btnApply = dlg_color.getButton(AlertDialog.BUTTON_POSITIVE);
        btnReset = dlg_color.getButton(AlertDialog.BUTTON_NEUTRAL);
        btnCancel = dlg_color.getButton(AlertDialog.BUTTON_NEGATIVE);

        background_color = dlg_color.findViewById(R.id.color_picker_preview);

        seekBar_Red = dlg_color.findViewById(R.id.color_picker_seekbar_red);
        seekBar_Green = dlg_color.findViewById(R.id.color_picker_seekbar_green);
        seekBar_Blue = dlg_color.findViewById(R.id.color_picker_seekbar_blue);
        seekBar_Alpha = dlg_color.findViewById(R.id.color_picker_seekbar_alpha);

        textBox_Red = dlg_color.findViewById(R.id.textView_red_color);
        textBox_Green = dlg_color.findViewById(R.id.textView_green_color);
        textBox_Blue = dlg_color.findViewById(R.id.textView_blue_color);
        textBox_Alpha = dlg_color.findViewById(R.id.textView_alpha_color);

        textBox_Hex = dlg_color.findViewById(R.id.textView_hex_color);


        // set button color
        btnCancel.setTextColor(context.getResources().getColor(R.color.colorCancelButtons));
        btnReset.setTextColor(context.getResources().getColor(R.color.colorResetButtons));
        btnApply.setTextColor(context.getResources().getColor(R.color.colorAcceptButtons));

        dlg_color.setCanceledOnTouchOutside(false);

        seekBar_Red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                textBox_Red.setText(String.valueOf(progress));

                textBox_Hex.setText(parseColorString());

                background_color.setBackgroundColor(getPreviewColor());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_Green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                textBox_Green.setText(String.valueOf(progress));

                textBox_Hex.setText(parseColorString());
                background_color.setBackgroundColor(getPreviewColor());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_Blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                textBox_Blue.setText(String.valueOf(progress));

                textBox_Hex.setText(parseColorString());
                background_color.setBackgroundColor(getPreviewColor());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_Alpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                textBox_Alpha.setText(String.valueOf(progress));

                String red_green_value = textBox_Hex.getText().toString().substring(0,6);
                textBox_Hex.setText(red_green_value + String.format("%02X", Integer.parseInt(textBox_Alpha.getText().toString())));
                background_color.setBackgroundColor(getPreviewColor());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        textBox_Hex.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(textBox_Hex.getText().length() == 8 && hasFocus) {
                    enableSeekBar();
                }
            }
        });

        textBox_Hex.addTextChangedListener(new TextWatcher() {

            private int start_position;
            private int end_position;
            private int length_before_change;
            private boolean marked = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                start_position = textBox_Hex.getSelectionStart();
                end_position = textBox_Hex.getSelectionEnd();

                if (start_position != end_position)
                    marked = true;

                length_before_change = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                textBox_Hex.removeTextChangedListener(this);

                String text = s.toString();
                int len = s.length();

                if(textBox_Hex.getText().toString().length() < 8) {
                    disableSeekBar();
                    textBox_Hex.setTextColor(Color.RED);
                    btnApply.setEnabled(false);
                    btnApply.setTextColor(context.getResources().getColor(R.color.colorAcceptDisabled));
                    textBox_Hex.addTextChangedListener(this);
                    return;
                }
                btnApply.setEnabled(true);
                btnApply.setTextColor(context.getResources().getColor(R.color.colorAcceptButtons));
                text = textBox_Hex.getText().toString().toUpperCase();
                Integer red = Integer.parseInt(text.substring(0,2), 16);
                Integer green = Integer.parseInt(text.substring(2,4), 16);
                Integer blue = Integer.parseInt(text.substring(4,6), 16);
                Integer alpha = Integer.parseInt(text.substring(6,8), 16);

                seekBar_Red.setProgress(red);
                seekBar_Green.setProgress(green);
                seekBar_Blue.setProgress(blue);
                seekBar_Alpha.setProgress(alpha);

                textBox_Hex.setTextColor(Color.BLACK);
                if(!s.toString().equals(s.toString().toUpperCase()))
                {
                    textBox_Hex.setText(s.toString().toUpperCase());
                }

                if(!marked) {
                    if (len > length_before_change) {
                        //added char
                        if (start_position + 1 < 8)
                            textBox_Hex.setSelection(start_position + 1);
                        else
                            textBox_Hex.setSelection(8);
                    }
                    else if (len < length_before_change) {
                        //deleted char
                        if (start_position - 1 > 0)
                            textBox_Hex.setSelection(start_position - 1);
                        else
                            textBox_Hex.setSelection(0);
                    }
                }

                textBox_Hex.addTextChangedListener(this);
            }
        });

        textBox_Hex.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                background_color.requestFocus();
                manager.hideSoftInputFromWindow(background_color.getWindowToken(), 0);
                return false;
            }
        });

        textBox_Red.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    disableSeekBar();
                }

                if(textBox_Red.getText().length() == 0)
                {
                    textBox_Red.setText("0");
                }
                if(Integer.parseInt(textBox_Red.getText().toString()) > 255)
                {
                    textBox_Red.setText("255");
                }
                seekBar_Red.setProgress(Integer.parseInt(textBox_Red.getText().toString()));
            }
        });


        textBox_Red.addTextChangedListener(new TextWatcher() {

            private int start_position;
            private int end_position;
            private int length_before_change;
            private boolean marked = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                start_position = textBox_Red.getSelectionStart();
                end_position = textBox_Red.getSelectionEnd();

                if (start_position != end_position)
                    marked = true;

                length_before_change = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int len = s.length();
                if(!marked && textBox_Red.isFocused()) {
                    if (len > length_before_change) {
                        //added char
                        if (start_position + 1 < 3)
                            textBox_Red.setSelection(start_position + 1);
                        else
                            textBox_Red.setSelection(3);
                    }
                    else if (len < length_before_change) {
                        //deleted char
                        if (start_position - 1 > 0)
                            textBox_Red.setSelection(start_position - 1);
                        else
                            textBox_Red.setSelection(0);
                    }
                }
            }
        });

        textBox_Red.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                enableSeekBar();

                if(textBox_Red.getText().length() == 0)
                {
                    textBox_Red.setText("0");
                }
                if(Integer.parseInt(textBox_Red.getText().toString()) > 255)
                {
                    textBox_Red.setText("255");
                }
                seekBar_Red.setProgress(Integer.parseInt(textBox_Red.getText().toString()));
                background_color.requestFocus();
                manager.hideSoftInputFromWindow(background_color.getWindowToken(), 0);
                return false;
            }
        });

        textBox_Green.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                   disableSeekBar();
                }

                if(textBox_Green.getText().length() == 0)
                {
                    textBox_Green.setText("0");
                }
                if(Integer.parseInt(textBox_Green.getText().toString()) > 255)
                {
                    textBox_Green.setText("255");
                }
                seekBar_Green.setProgress(Integer.parseInt(textBox_Green.getText().toString()));
            }
        });

        textBox_Green.addTextChangedListener(new TextWatcher() {
            private int start_position;
            private int end_position;
            private int length_before_change;
            private boolean marked = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                start_position = textBox_Green.getSelectionStart();
                end_position = textBox_Green.getSelectionEnd();

                if (start_position != end_position)
                    marked = true;

                length_before_change = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int len = s.length();
                if(!marked && textBox_Green.isFocused()) {
                    if (len > length_before_change) {
                        //added char
                        if (start_position + 1 < 3)
                            textBox_Green.setSelection(start_position + 1);
                        else
                            textBox_Green.setSelection(3);
                    }
                    else if (len < length_before_change) {
                        //deleted char
                        if (start_position - 1 > 0)
                            textBox_Green.setSelection(start_position - 1);
                        else
                            textBox_Green.setSelection(0);
                    }
                }
            }
        });

        textBox_Green.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                enableSeekBar();

                if(textBox_Green.getText().length() == 0)
                {
                    textBox_Green.setText("0");
                }
                if(Integer.parseInt(textBox_Green.getText().toString()) > 255)
                {
                    textBox_Green.setText("255");
                }
                seekBar_Green.setProgress(Integer.parseInt(textBox_Green.getText().toString()));
                background_color.requestFocus();
                manager.hideSoftInputFromWindow(background_color.getWindowToken(), 0);
                return false;
            }
        });

        textBox_Blue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    disableSeekBar();
                }

                if(textBox_Blue.getText().length() == 0)
                {
                    textBox_Blue.setText("0");
                }
                if(Integer.parseInt(textBox_Blue.getText().toString()) > 255)
                {
                    textBox_Blue.setText("255");
                }
                seekBar_Blue.setProgress(Integer.parseInt(textBox_Blue.getText().toString()));
            }
        });

        textBox_Blue.addTextChangedListener(new TextWatcher() {
            private int start_position;
            private int end_position;
            private int length_before_change;
            private boolean marked = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                start_position = textBox_Blue.getSelectionStart();
                end_position = textBox_Blue.getSelectionEnd();

                if (start_position != end_position)
                    marked = true;

                length_before_change = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int len = s.length();
                if(!marked && textBox_Blue.isFocused()) {
                    if (len > length_before_change) {
                        //added char
                        if (start_position + 1 < 3)
                            textBox_Blue.setSelection(start_position + 1);
                        else
                            textBox_Blue.setSelection(3);
                    }
                    else if (len < length_before_change) {
                        //deleted char
                        if (start_position - 1 > 0)
                            textBox_Blue.setSelection(start_position - 1);
                        else
                            textBox_Blue.setSelection(0);
                    }
                }
            }
        });

        textBox_Blue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                enableSeekBar();

                if(textBox_Blue.getText().length() == 0)
                {
                    textBox_Blue.setText("0");
                }
                if(Integer.parseInt(textBox_Blue.getText().toString()) > 255)
                {
                    textBox_Blue.setText("255");
                }
                seekBar_Blue.setProgress(Integer.parseInt(textBox_Blue.getText().toString()));
                background_color.requestFocus();
                manager.hideSoftInputFromWindow(background_color.getWindowToken(), 0);
                return false;
            }
        });

        textBox_Alpha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    disableSeekBar();
                }

                if(textBox_Alpha.getText().length() == 0)
                {
                    textBox_Alpha.setText("0");
                }
                if(Integer.parseInt(textBox_Alpha.getText().toString()) > 255)
                {
                    textBox_Alpha.setText("255");
                }
                seekBar_Alpha.setProgress(Integer.parseInt(textBox_Alpha.getText().toString()));
            }
        });

        textBox_Alpha.addTextChangedListener(new TextWatcher() {
            private int start_position;
            private int end_position;
            private int length_before_change;
            private boolean marked = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                start_position = textBox_Alpha.getSelectionStart();
                end_position = textBox_Alpha.getSelectionEnd();

                if (start_position != end_position)
                    marked = true;

                length_before_change = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int len = s.length();
                if(!marked && textBox_Alpha.isFocused()) {
                    if (len > length_before_change) {
                        //added char
                        if (start_position + 1 < 3)
                            textBox_Alpha.setSelection(start_position + 1);
                        else
                            textBox_Alpha.setSelection(3);
                    }
                    else if (len < length_before_change) {
                        //deleted char
                        if (start_position - 1 > 0)
                            textBox_Alpha.setSelection(start_position - 1);
                        else
                            textBox_Alpha.setSelection(0);
                    }
                }
            }
        });

        textBox_Alpha.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               enableSeekBar();

                if(textBox_Alpha.getText().length() == 0)
                {
                    textBox_Alpha.setText("0");
                }
                if(Integer.parseInt(textBox_Alpha.getText().toString()) > 255)
                {
                    textBox_Alpha.setText("255");
                }
                seekBar_Alpha.setProgress(Integer.parseInt(textBox_Alpha.getText().toString()));
                background_color.requestFocus();
                manager.hideSoftInputFromWindow(background_color.getWindowToken(), 0);
                return false;
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                background_color.requestFocus();
                manager.hideSoftInputFromWindow(background_color.getWindowToken(), 0);


                seekBar_Red.setProgress(color_r);
                seekBar_Green.setProgress(color_g);
                seekBar_Blue.setProgress(color_b);
                seekBar_Alpha.setProgress(color_a);
                String red = String.format("%02X", color_r);
                String green = String.format("%02X", color_g);
                String blue = String.format("%02X", color_b);
                String alpha = String.format("%02X", color_a);
                textBox_Hex.setText(red + green + blue + alpha);
                background_color.setBackgroundColor(getColor());

            }
        });

        btnApply.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                color_r = seekBar_Red.getProgress();
                color_g = seekBar_Green.getProgress();
                color_b = seekBar_Blue.getProgress();
                color_a = seekBar_Alpha.getProgress();
                listener.onColorApplied(getColor());
                dlg_color.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                dlg_color.dismiss();
            }
        });

        seekBar_Red.setProgress(color_r);
        seekBar_Green.setProgress(color_g);
        seekBar_Blue.setProgress(color_b);
        seekBar_Alpha.setProgress(color_a);
    }

    private void disableSeekBar(){
        seekBar_Red.setEnabled(false);
        seekBar_Green.setEnabled(false);
        seekBar_Blue.setEnabled(false);
        seekBar_Alpha.setEnabled(false);

        textBox_Blue.setEnabled(false);
        textBox_Green.setEnabled(false);
        textBox_Red.setEnabled(false);
        textBox_Alpha.setEnabled(false);
    }

    private  void enableSeekBar(){
        seekBar_Red.setEnabled(true);
        seekBar_Green.setEnabled(true);
        seekBar_Blue.setEnabled(true);
        seekBar_Alpha.setEnabled(true);

        textBox_Blue.setEnabled(true);
        textBox_Green.setEnabled(true);
        textBox_Red.setEnabled(true);
        textBox_Alpha.setEnabled(true);
    }

    private String parseColorString() {
        String red_value = String.format("%02X", Integer.parseInt(textBox_Red.getText().toString()));
        String green_value = String.format("%02X", Integer.parseInt(textBox_Green.getText().toString()));
        String blue_value = String.format("%02X", Integer.parseInt(textBox_Blue.getText().toString()));
        String alpha_value = String.format("%02X", Integer.parseInt(textBox_Alpha.getText().toString()));

        return (red_value + green_value + blue_value + alpha_value);
    }

    public boolean isShowing()
    {
        if(dlg_color != null)
            return dlg_color.isShowing();
        return false;
    }

    public void dismissDialogue() {
        if (dlg_color != null && dlg_color.isShowing())
            dlg_color.dismiss();
    }

    public String getHexColorString() {
        if(textBox_Hex!=null)
        {
            return textBox_Hex.getText().toString();
        }
        return "";

    }

    public void setHexString(String hex) {
        if(textBox_Hex!=null)
            textBox_Hex.setText(hex);
    }
}
