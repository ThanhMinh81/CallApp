package com.example.Model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String>  keyWordSearch = new MutableLiveData<>();


    public SearchViewModel() {
    }


    public MutableLiveData<String> getKeyWordSearch() {
        return keyWordSearch;
    }

    public void setKeyWordSearch(String keyWordSearch) {
        this.keyWordSearch.setValue(keyWordSearch);
    }
}
