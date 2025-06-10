package com.example.androidaplikacjapierwsza;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidaplikacjapierwsza.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ActivityResultLauncher<Intent> gradesLauncher;
    private double lastAverage = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.topAppBar);

        if (savedInstanceState != null) {
            lastAverage = savedInstanceState.getDouble("lastAverage", 0);
            if (lastAverage > 0) {
                showAverageResult(lastAverage);
            }
        }

        gradesLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleGradesResult
        );

        // Walidacja przy utracie focusu
        binding.editTextFirstName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validateFirstName();
        });
        binding.editTextLastName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validateLastName();
        });
        binding.editTextGradesCount.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validateGrades();
        });

        // Walidacja przy zmianie tekstu
        binding.editTextFirstName.addTextChangedListener(new SimpleTextWatcher(this::updateButtonVisibility));
        binding.editTextLastName.addTextChangedListener(new SimpleTextWatcher(this::updateButtonVisibility));
        binding.editTextGradesCount.addTextChangedListener(new SimpleTextWatcher(this::updateButtonVisibility));

        // Przycisk przejścia do ocen
        binding.buttonSubmit.setOnClickListener(v -> {
            String countStr = binding.editTextGradesCount.getText().toString().trim();
            if (!countStr.isEmpty()) {
                int count = Integer.parseInt(countStr);
                Intent intent = new Intent(MainActivity.this, GradesActivity.class);
                intent.putExtra("count", count);
                gradesLauncher.launch(intent);
            }
        });

        // Przycisk końcowy
        binding.resultButton.setOnClickListener(v -> {
            Toast.makeText(this,
                    lastAverage >= 3.0 ? getString(R.string.pass_msg) : getString(R.string.fail_msg),
                    Toast.LENGTH_LONG).show();
            finish();
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("lastAverage", lastAverage);
    }

    private void handleGradesResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            double average = result.getData().getDoubleExtra("average", 0);
            lastAverage = average;
            showAverageResult(average);
        }
    }

    private void showAverageResult(double avg) {
        binding.averageTextView.setText("Average: " + String.format("%.2f", avg));
        binding.averageTextView.setVisibility(View.VISIBLE);
        binding.resultButton.setText(avg >= 3.0 ? R.string.super_button : R.string.fail_button);
        binding.resultButton.setVisibility(View.VISIBLE);
    }

    private boolean validateFirstName() {
        String text = binding.editTextFirstName.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            binding.editTextFirstName.setError(getString(R.string.error_first_name));
            Toast.makeText(this, R.string.error_first_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (text.matches(".*\\d.*")) {
            binding.editTextFirstName.setError("First name cannot contain digits");
            Toast.makeText(this, "First name cannot contain digits", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateLastName() {
        String text = binding.editTextLastName.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            binding.editTextLastName.setError(getString(R.string.error_last_name));
            Toast.makeText(this, R.string.error_last_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (text.matches(".*\\d.*")) {
            binding.editTextLastName.setError("Last name cannot contain digits");
            Toast.makeText(this, "Last name cannot contain digits", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateGrades() {
        String text = binding.editTextGradesCount.getText().toString().trim();
        try {
            int value = Integer.parseInt(text);
            if (value < 5 || value > 15) {
                binding.editTextGradesCount.setError(getString(R.string.error_grades_range));
                Toast.makeText(this, R.string.error_grades_range, Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            binding.editTextGradesCount.setError(getString(R.string.error_number_format));
            Toast.makeText(this, R.string.error_number_format, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void updateButtonVisibility() {
        boolean valid = validateFirstName()
                && validateLastName()
                && validateGrades();
        binding.buttonSubmit.setVisibility(valid ? View.VISIBLE : View.GONE);
    }
}
