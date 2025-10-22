package com.example.Atai_Ibragimov.service;

import com.example.Atai_Ibragimov.model.Advert;
import com.example.Atai_Ibragimov.LalafoParser;

import java.util.List;

public class LalafoService {
    private final LalafoParser parser;

    public LalafoService() {
        this.parser = new LalafoParser();
    }

    public List<Advert> getAdverts() {
        // Always return sample data for demonstration
        List<Advert> adverts = parser.parseAdverts();
        System.out.println("Returning " + adverts.size() + " advertisements");
        return adverts;
    }
}