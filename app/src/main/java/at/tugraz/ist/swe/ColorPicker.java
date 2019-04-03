package at.tugraz.ist.swe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

public class ColorPicker {

    private SeekBar seekBar_Red;
    private SeekBar seekBar_Green;
    private SeekBar seekBar_Blue;

    private EditText textBox_Red;
    private EditText textBox_Green;
    private EditText textBox_Blue;

    private AlertDialog dlg_color;

    public ColorPicker(Context context)
    {

        AlertDialog.Builder bld_ColorPicker = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dlg_view = inflater.inflate(R.layout.color_picker_dialog,null);

        bld_ColorPicker.setTitle(R.string.color_picker_title);
        bld_ColorPicker.setView(dlg_view);

        bld_ColorPicker.setNegativeButton(R.string.color_picker_button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        bld_ColorPicker.setPositiveButton(R.string.color_picker_button_apply, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        bld_ColorPicker.setNeutralButton(R.string.color_picker_button_reset, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dlg_color = bld_ColorPicker.create();

        // set button color
        dlg_color.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorCancelButtons));
        dlg_color.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(context.getResources().getColor(R.color.colorResetButtons));
        dlg_color.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorAcceptButtons));

        seekBar_Red = (SeekBar) dlg_color.findViewById(R.id.color_picker_seekbar_red);
        seekBar_Green = (SeekBar) dlg_color.findViewById(R.id.color_picker_seekbar_green);
        seekBar_Blue = (SeekBar) dlg_color.findViewById(R.id.color_picker_seekbar_blue);

        textBox_Red = dlg_color.findViewById(R.id.textView_red_color);
        textBox_Green = dlg_color.findViewById(R.id.textView_green_color);
        textBox_Blue = dlg_color.findViewById(R.id.textView_blue_color);

        final EditText textBox_Hex = dlg_color.findViewById(R.id.textView_hex_color);

        textBox_Hex.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(textBox_Hex.getText().toString().length() < 6) {
                    seekBar_Red.setEnabled(false);
                    seekBar_Green.setEnabled(false);
                    seekBar_Blue.setEnabled(false);
                    textBox_Blue.setEnabled(false);
                    textBox_Green.setEnabled(false);
                    textBox_Red.setEnabled(false);
                    textBox_Hex.setTextColor(Color.RED);
                    return;
                }
                String text = textBox_Hex.getText().toString().toUpperCase();
                Integer red = Integer.parseInt(text.substring(0,2), 16);
                Integer green = Integer.parseInt(text.substring(2,4), 16);
                Integer blue = Integer.parseInt(text.substring(4,6), 16);
                seekBar_Red.setProgress(red);
                seekBar_Green.setProgress(green);
                seekBar_Blue.setProgress(blue);
                seekBar_Red.setEnabled(true);
                seekBar_Green.setEnabled(true);
                seekBar_Blue.setEnabled(true);
                textBox_Blue.setEnabled(true);
                textBox_Green.setEnabled(true);
                textBox_Red.setEnabled(true);
                textBox_Hex.setTextColor(Color.BLACK);
            }
        });

        seekBar_Red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                textBox_Red.setText(String.valueOf(progress));

                String green_blue_value = textBox_Hex.getText().toString().substring(2);
                textBox_Hex.setText(String.format("%02X", Integer.parseInt(textBox_Red.getText().toString())) + green_blue_value);
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

                String red_value = textBox_Hex.getText().toString().substring(0,2);
                String blue_value = textBox_Hex.getText().toString().substring(4,6);
                String green_value = String.format("%02X", Integer.parseInt(textBox_Green.getText().toString()));
                textBox_Hex.setText(red_value + green_value + blue_value);
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

                String red_green_value = textBox_Hex.getText().toString().substring(0,4);
                textBox_Hex.setText(red_green_value + String.format("%02X", Integer.parseInt(textBox_Blue.getText().toString())));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        textBox_Red.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();

                if(TextUtils.isEmpty(text))
                {
                    textBox_Red.setText("0");
                    text = "0";
                }

                if(Integer.parseInt(text) > 255)
                {
                    textBox_Red.setText("255");
                    text = "255";
                }

                seekBar_Red.setProgress(Integer.parseInt(text));
                textBox_Red.setSelection(textBox_Red.getText().length());
            }
        });

        textBox_Green.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();

                if(TextUtils.isEmpty(text))
                {
                    textBox_Green.setText("0");
                    text = "0";
                }

                if(Integer.parseInt(text) > 255)
                {
                    textBox_Green.setText("255");
                    text = "255";
                }

                seekBar_Green.setProgress(Integer.parseInt(text));
                textBox_Green.setSelection(textBox_Green.getText().length());
            }
        });

        textBox_Blue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();

                if(TextUtils.isEmpty(text))
                {
                    textBox_Blue.setText("0");
                    text = "0";
                }

                if(Integer.parseInt(text) > 255)
                {
                    textBox_Blue.setText("255");
                    text = "255";
                }

                seekBar_Blue.setProgress(Integer.parseInt(text));
                textBox_Blue.setSelection(textBox_Blue.getText().length());

            }
        });
    }

    public int getColor()
    {
        int red = this.seekBar_Red.getProgress();
        int green = this.seekBar_Green.getProgress();
        int blue = this.seekBar_Blue.getProgress();
        return Color.argb(255, red, green, blue);
    }
    
    public void show()
    {
        dlg_color.show();
    }

}
