package com.example.orange_button_apk.di;

import com.fasterxml.jackson.databind.ObjectMapper;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class MappingModule {
    @Provides
    public static ObjectMapper provideObjectMapper() {
        return new ObjectMapper();
    }
}
