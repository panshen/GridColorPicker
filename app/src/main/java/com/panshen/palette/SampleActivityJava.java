package com.panshen.palette;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.panshen.gridcolorpicker.GridColorPicker;
import com.panshen.gridcolorpicker.OnColorSelectListener;
import com.panshen.gridcolorpicker.builder.ColorPickerDialogBuilder;
import com.panshen.gridcolorpicker.builder.ColorPickerViewBuilder;
import com.panshen.palette.databinding.ActivityMainBinding;

import java.util.ArrayList;

@SuppressLint("SetTextI18n")
public class SampleActivityJava extends AppCompatActivity {
    ActivityMainBinding binding = null;
    GridColorPicker gridColorPicker = null;
    AlertDialog dialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.center.setText("Builder");
        binding.colorPicker.checkColor(ColorPreference.INSTANCE.getColor(this));
        binding.colorPicker.setOnColorSelectListener(OnColorSelectListener);

    }

    public void onClick(View v) {
        if (v.getId() == R.id.xml) {
            binding.colorPicker.setVisibility(View.VISIBLE);
            gridColorPicker.setVisibility(View.GONE);
        } else if (v.getId() == R.id.center) {
            binding.colorPicker.setVisibility(View.GONE);
            createColorPickerView();
        } else if (v.getId() == R.id.dialog) {
            showDialog();
        } else if (v.getId() == R.id.checkColor) {
            Editable editable = binding.et.getText();
            if (editable != null) {
                binding.colorPicker.checkColor(editable.toString());
            }
        }
    }

    private void showDialog() {
        if (dialog == null) {
            dialog = new ColorPickerDialogBuilder(this).setCancelable(true).setPositiveButton(getResources().getString(R.string.confirm), (dialog, which) -> {
                Toast.makeText(this, "Click Positive Button", Toast.LENGTH_SHORT).show();
            }).setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> {
                Toast.makeText(this, "Click Negative Button", Toast.LENGTH_SHORT).show();
            }).setOnDismissListener(dialog -> {
                Toast.makeText(this, "Dismiss", Toast.LENGTH_SHORT).show();
            }).setOnColorSelectListener(new OnColorSelectListener() {
                @Override
                public void afterColorChanged(@NonNull String color) {
                    SampleActivityJava.this.afterColorChanged(color);
                }

                @Override
                public void onColorChanged(@NonNull String color) {
                    SampleActivityJava.this.onColorChanged(color);
                }
            }).show();
        } else {
            dialog.show();
        }
    }

    private void createColorPickerView() {
        if (gridColorPicker == null) {
            createPicker();
        } else {
            gridColorPicker.setVisibility(View.VISIBLE);
        }
    }

    private void onColorChanged(String color) {
        binding.colorChanged.setBackgroundColor(Color.parseColor(color));
        binding.tvOnColorChanged.setText("onColorChanged() - " + color);
    }

    private void afterColorChanged(String color) {
        ColorPreference.INSTANCE.saveColor(color, this);
        binding.afterColorChanged.setBackgroundColor(Color.parseColor(color));
        binding.tvAfterColorChanged.setText("afterColorChanged() - " + color);
    }

    private void createPicker() {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(R.color.C_30525C);
        colors.add(R.color.C_49928A);
        colors.add(R.color.C_E6D17E);
        colors.add(R.color.C_E5A367);
        colors.add(R.color.C_C66C52);

        ColorPickerViewBuilder builder = new ColorPickerViewBuilder(this);
        gridColorPicker = builder.setRow(5).setColorScheme(colors).setOnColorSelectListener(OnColorSelectListener).build();
        binding.container.addView(gridColorPicker);
    }

    private final OnColorSelectListener OnColorSelectListener = new OnColorSelectListener() {
        @Override
        public void onColorChanged(@NonNull String color) {
            SampleActivityJava.this.onColorChanged(color);
        }

        @Override
        public void afterColorChanged(@NonNull String color) {
            SampleActivityJava.this.afterColorChanged(color);
        }
    };
}
