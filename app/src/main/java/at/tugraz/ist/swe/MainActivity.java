package at.tugraz.ist.swe;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import org.zakariya.flyoutmenu.FlyoutMenuView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();

    }

private void setupToolbar() {
    FlyoutMenuView smileyFlyoutMenu = findViewById(R.id.smileyFlyoutMenu);

    int[] unicodeChars = {
            0x1F58C,
            0x2307,
            0x23AF,
            0x1F58D,
            0x2795,
            0x1F4F7,
            0x1F5D1
    };

    @ColorInt int color = ContextCompat.getColor(this, R.color.smileyMenuCharColor);
    float fontSizeInMenu = getResources().getDimension(R.dimen.smiley_menu_item_size) * 0.5f;
    float fontSizeInButton = getResources().getDimension(R.dimen.flyout_menu_button_size) * 0.5f;
    List<FlyoutToolbar.MenuItem> menuItems = new ArrayList<>();
    for (int code : unicodeChars) {
        menuItems.add(new FlyoutToolbar.MenuItem(menuItems.size(), code, fontSizeInMenu, color));
    }


    smileyFlyoutMenu.setLayout(new FlyoutMenuView.GridLayout(5, FlyoutMenuView.GridLayout.UNSPECIFIED));
    smileyFlyoutMenu.setAdapter(new FlyoutMenuView.ArrayAdapter<>(menuItems));

    final FlyoutToolbar.ButtonRenderer renderer = new FlyoutToolbar.ButtonRenderer(unicodeChars[0], fontSizeInButton, color);
    smileyFlyoutMenu.setButtonRenderer(renderer);

    smileyFlyoutMenu.setSelectionListener(new FlyoutMenuView.SelectionListener() {
        @Override
        public void onItemSelected(FlyoutMenuView flyoutMenuView, FlyoutMenuView.MenuItem item) {

            renderer.setEmojiCode(((FlyoutToolbar.MenuItem) item).getEmojiCode());
        }

        @Override
        public void onDismissWithoutSelection(FlyoutMenuView flyoutMenuView) {
        }
    });
}
}
