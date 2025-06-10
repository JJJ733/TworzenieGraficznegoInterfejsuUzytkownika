package com.example.androidaplikacjapierwsza;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidaplikacjapierwsza.databinding.GradeRowBinding;
import java.util.List;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {

    private final List<Grade> grades;

    public GradeAdapter(List<Grade> grades) {
        this.grades = grades;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public double calculateAverage() {
        double sum = 0;
        for (Grade grade : grades) {
            sum += grade.getGrade();
        }
        return sum / grades.size();
    }

    public boolean hasAllGrades() {
        for (Grade g : grades) {
            if (g.getGrade() == 0) return false;
        }
        return true;
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GradeViewHolder(GradeRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        Grade grade = grades.get(position);
        holder.binding.subjectTextView.setText(grade.getSubject());

        RadioGroup rg = holder.binding.gradesRadioGroup;
        rg.setOnCheckedChangeListener(null); // Unhook listener before setting
        rg.clearCheck();

        if (grade.getGrade() > 0) {
            int selectedId = rg.getChildAt(grade.getGrade() - 1).getId();
            rg.check(selectedId);
        }

        rg.setOnCheckedChangeListener((group, checkedId) -> {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    grade.setGrade(i + 1);
                    break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return grades.size();
    }

    static class GradeViewHolder extends RecyclerView.ViewHolder {
        GradeRowBinding binding;
        GradeViewHolder(GradeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
