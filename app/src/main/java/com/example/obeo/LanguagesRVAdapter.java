package com.example.obeo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ClientServer.ClientRequests.ObeoClient;
import ClientServer.ClientRequests.UpdateLanguageRequest;
import holidays_table.ObeoHoliday;
import languages.UserLanguage;

class LanguagesRVAdapter extends RecyclerView.Adapter<LanguagesRVAdapter.LanguageViewHolder> {

    private List<UserLanguage> userLanguages;
    private Context context;
    private static String[] spinnerLanguagesArray =
        {"English", "French", "German", "Hindi", "Japanese",
            "Spanish", "Swedish", "Portuguese", "Russian", "Thai"};
    private static String[] spinnerLanguageLevelsArray = {"1", "2", "3", "4", "5"};

    public static class LanguageViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        Spinner languageSpinner;
        Spinner languageLevelSpinner;
        List<String> spinnerLanguages = new ArrayList<>();
        List<String> spinnerLanguageLevels = new ArrayList<>();
        Button confirmButton;
        UserLanguage language;

        LanguageViewHolder(View itemView, final Context context) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            languageSpinner = itemView.findViewById(R.id.languageSpinner);
            languageLevelSpinner = itemView.findViewById(R.id.languageLevelSpinner);
            confirmButton = itemView.findViewById(R.id.confirmButton);
            confirmButton.setOnClickListener(v -> {
              String languageString = languageSpinner.getSelectedItem().toString();
              String level = languageLevelSpinner.getSelectedItem().toString();
                ObeoClient.getInstance().deleteLanguage(language);
                ObeoClient.getInstance().createLanguage(languageString, Integer.valueOf(level));
            });

        }

        public void setLanguage(UserLanguage language) {
            this.language = language;
        }
    }


    LanguagesRVAdapter(List<UserLanguage> languages, Context context){
        this.userLanguages = languages;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public LanguageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_language,
                viewGroup, false);

        return new LanguageViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(LanguageViewHolder holidayViewHolder, int i) {

        UserLanguage language = userLanguages.get(i);
        holidayViewHolder.setLanguage(language);

        holidayViewHolder.spinnerLanguages.addAll(Arrays.asList(spinnerLanguagesArray));
        holidayViewHolder.spinnerLanguageLevels.addAll(Arrays.asList(spinnerLanguageLevelsArray));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, holidayViewHolder.spinnerLanguages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holidayViewHolder.languageSpinner.setAdapter(adapter);
        String lang = language.getLanguage();
        if (holidayViewHolder.spinnerLanguages.contains(lang)){
            holidayViewHolder.spinnerLanguages.remove(lang);
            holidayViewHolder.spinnerLanguages.add(0, lang);
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, holidayViewHolder.spinnerLanguageLevels);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holidayViewHolder.languageLevelSpinner.setAdapter(adapter2);
        int level = language.getLevel();
        if (holidayViewHolder.spinnerLanguageLevels.contains(String.valueOf(level))) {
            holidayViewHolder.spinnerLanguageLevels.remove(String.valueOf(level));
            holidayViewHolder.spinnerLanguageLevels.add(0, String.valueOf(level));
        }
    }

    @Override
    public int getItemCount() {
        return userLanguages.size();
    }
}
