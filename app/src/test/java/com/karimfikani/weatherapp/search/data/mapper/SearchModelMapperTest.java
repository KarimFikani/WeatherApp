package com.karimfikani.weatherapp.search.data.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.karimfikani.weatherapp.search.data.LocalNames;
import com.karimfikani.weatherapp.search.data.LocationItem;
import com.karimfikani.weatherapp.search.data.SearchUiModel;

@RunWith(MockitoJUnitRunner.class)
public class SearchModelMapperTest {

    private LocationItem locationItem1;
    private LocationItem locationItem2;

    @InjectMocks
    private SearchModelMapper searchModelMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void mapShouldCorrectlyMapLocationItemsToSearchUiModel() {
        // Given
        locationItem1 = new LocationItem(
            "Country1", null, null, null, "City1", "State1"
        );
        locationItem2 = new LocationItem(
            "Country2", null, null, null, "City2", null
        );
        List<LocationItem> locationItems = Arrays.asList(locationItem1, locationItem2);

        // When
        SearchUiModel sut = searchModelMapper.map(locationItems);

        // Then
        assertEquals(2, sut.getLocations().size());
        assertEquals("City1, State1, Country1", sut.getLocations().get(0));
        assertEquals("City2, Country2", sut.getLocations().get(1));
    }

    @Test
    public void mapShouldHandleNullValuesInLocationItems() {
        // Given
        locationItem1 = new LocationItem(
            null, null, null, null, "City1", null
        );
        locationItem2 = new LocationItem(
            null, null, null, null, "City2", "State2"
        );
        List<LocationItem> locationItems = Arrays.asList(locationItem1, locationItem2);

        // When
        SearchUiModel sut = searchModelMapper.map(locationItems);

        // Then
        assertEquals(2, sut.getLocations().size());
        assertEquals("City1", sut.getLocations().get(0));
        assertEquals("City2, State2", sut.getLocations().get(1));
    }

    @Test
    public void mapShouldHandleEmptyInputList() {
        // Given
        List<LocationItem> locationItems = Arrays.asList();

        // When
        SearchUiModel result = searchModelMapper.map(locationItems);

        // Then
        assertEquals(0, result.getLocations().size());
    }
}
