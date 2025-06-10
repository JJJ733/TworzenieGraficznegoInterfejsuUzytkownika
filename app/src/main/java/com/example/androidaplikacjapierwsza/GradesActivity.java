package com.example.androidaplikacjapierwsza;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.androidaplikacjapierwsza.databinding.ActivityGradesBinding;
import java.util.ArrayList;
import java.util.List;

public class GradesActivity extends AppCompatActivity {

    private ActivityGradesBinding binding;
    private GradeAdapter adapter;
    private List<Grade> gradeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGradesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar z przyciskiem "wstecz"
        setSupportActionBar(binding.topAppBar);
        binding.topAppBar.setNavigationOnClickListener(v -> onBackPressed());


        int count = getIntent().getIntExtra("count", 0);
        String[] subjects = getResources().getStringArray(R.array.subject_names);

        if (savedInstanceState != null) {
            gradeList = savedInstanceState.getParcelableArrayList("grades");
        } else {
            for (int i = 0; i < count && i < subjects.length; i++) {
                gradeList.add(new Grade(subjects[i], 0));
            }
        }

        adapter = new GradeAdapter(gradeList);
        binding.gradesRecyclerView.setAdapter(adapter);
        binding.gradesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.calculateAverageButton.setOnClickListener(v -> {
            if (adapter.hasAllGrades()) {
                double avg = adapter.calculateAverage();
                Intent intent = new Intent();
                intent.putExtra("average", avg);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, R.string.error_incomplete_grades, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("grades", new ArrayList<Parcelable>(adapter.getGrades()));
        super.onSaveInstanceState(outState);
    }
}
