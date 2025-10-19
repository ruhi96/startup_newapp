package com.ruhi.newsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ruhi.newsapp.R;
import com.ruhi.newsapp.models.CaseStudy;

import java.util.ArrayList;
import java.util.List;

public class CaseStudyAdapter extends RecyclerView.Adapter<CaseStudyAdapter.ViewHolder> {
    private List<CaseStudy> caseStudies;

    public CaseStudyAdapter() {
        this.caseStudies = new ArrayList<>();
    }

    public void setCaseStudies(List<CaseStudy> caseStudies) {
        this.caseStudies = caseStudies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_case_study, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CaseStudy caseStudy = caseStudies.get(position);
        holder.bind(caseStudy);
    }

    @Override
    public int getItemCount() {
        return caseStudies.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvExecutiveSummary;
        private final TextView tvBackground;
        private final TextView tvProblemStatement;
        private final TextView tvAnalysis;
        private final TextView tvAlternatives;
        private final TextView tvRecommendations;
        private final TextView tvConclusion;
        private final TextView tvSource;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvExecutiveSummary = itemView.findViewById(R.id.tvExecutiveSummary);
            tvBackground = itemView.findViewById(R.id.tvBackground);
            tvProblemStatement = itemView.findViewById(R.id.tvProblemStatement);
            tvAnalysis = itemView.findViewById(R.id.tvAnalysis);
            tvAlternatives = itemView.findViewById(R.id.tvAlternatives);
            tvRecommendations = itemView.findViewById(R.id.tvRecommendations);
            tvConclusion = itemView.findViewById(R.id.tvConclusion);
            tvSource = itemView.findViewById(R.id.tvSource);
        }

        public void bind(CaseStudy caseStudy) {
            tvTitle.setText(caseStudy.getTitle());
            tvExecutiveSummary.setText(caseStudy.getExecutiveSummary());
            tvBackground.setText(caseStudy.getBackground());
            tvProblemStatement.setText(caseStudy.getProblemStatement());
            tvAnalysis.setText(caseStudy.getAnalysis());
            tvAlternatives.setText(caseStudy.getAlternatives());
            tvRecommendations.setText(caseStudy.getRecommendations());
            tvConclusion.setText(caseStudy.getConclusion());
            tvSource.setText(caseStudy.getSource());
        }
    }
}

