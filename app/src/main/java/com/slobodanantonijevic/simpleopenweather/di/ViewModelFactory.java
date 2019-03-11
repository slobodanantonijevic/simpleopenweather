package com.slobodanantonijevic.simpleopenweather.di;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModels;

    @Inject
    public ViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModels) {

        this.viewModels = viewModels;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        Provider<? extends ViewModel> viewModelProvider = viewModels.get(modelClass);

        if (viewModelProvider == null) {

            for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : viewModels.entrySet()) {

                if (modelClass.isAssignableFrom(entry.getKey())) {

                    viewModelProvider = entry.getValue();
                    break;
                }
            }
        }

        if (viewModelProvider == null) {

            throw new IllegalArgumentException("unknown model class " + modelClass);
        }

        try {

            return (T) viewModelProvider.get();
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
}
