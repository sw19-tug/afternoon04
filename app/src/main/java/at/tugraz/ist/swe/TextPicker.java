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

    }
}
