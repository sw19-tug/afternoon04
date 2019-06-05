package at.tugraz.ist.swe;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class TextPicker {

    public interface TextApprovedListener
    {
        void onTextApproved(int color);
    }

    private TextApprovedListener listener;

    private EditText textBox_text;
    private AlertDialog dlg_text;
    private Context context;

    private String text;

    private InputMethodManager manager;
    Button btnApply;
    Button btnReset;
    Button btnCancel;

    public TextPicker (Context context)
    {
        this.context = context;
        text = "";
        this.manager = (InputMethodManager)this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
        AlertDialog.Builder bld_TextPicker = new AlertDialog.Builder(context);
        this.listener = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View dlg_view = inflater.inflate(R.layout.text_picker_dialog, null);

        bld_TextPicker.setTitle(R.string.text_picker_title);
        bld_TextPicker.setView(dlg_view);

        bld_TextPicker.setNegativeButton(R.string.text_picker_button_cancel, null);

        bld_TextPicker.setPositiveButton(R.string.text_picker_button_apply, null);

        bld_TextPicker.setNeutralButton(R.string.text_picker_button_reset, null);

        dlg_text = bld_TextPicker.create();

    }

    public void setOnTextAppliedListener(TextApprovedListener listener){
        this.listener = listener;
    }

    public String getText() {
        return text;
    }

    public void show()
    {
        dlg_text.show();

        btnApply = dlg_text.getButton(AlertDialog.BUTTON_POSITIVE);
        btnReset = dlg_text.getButton(AlertDialog.BUTTON_NEUTRAL);
        btnCancel = dlg_text.getButton(AlertDialog.BUTTON_NEGATIVE);
        btnCancel.setTextColor(context.getResources().getColor(R.color.colorCancelButtons));
        btnReset.setTextColor(context.getResources().getColor(R.color.colorResetButtons));
        btnApply.setTextColor(context.getResources().getColor(R.color.colorAcceptButtons));

        textBox_text = dlg_text.findViewById(R.id.textPicker_text);

        dlg_text.setCanceledOnTouchOutside(false);
    }

}
