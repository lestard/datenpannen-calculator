package de.hszg.datenpannen.dataloss.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CostsChartModelTest {

    public static final double AVG_TOTAL = 103936.00;
    public static final double MIN_TOTAL = 55546.91;
    public static final double MAX_TOTAL = 171408.11;

    private CostsChartModel model;

    private DoubleProperty avgCostTotal = new SimpleDoubleProperty();
    private DoubleProperty minCostTotal = new SimpleDoubleProperty();
    private DoubleProperty maxCostTotal = new SimpleDoubleProperty();
    private IntegerProperty numberOfDatasets = new SimpleIntegerProperty();
    private IntegerProperty numberOfLostDatasets = new SimpleIntegerProperty();

    @Before
    public void setup(){
        DatalossResult resultMock = mock(DatalossResult.class);
        when(resultMock.avgCostTotal()).thenReturn(avgCostTotal);
        when(resultMock.minCostTotal()).thenReturn(minCostTotal);
        when(resultMock.maxCostTotal()).thenReturn(maxCostTotal);

        UserinputModel userinputModelMock = mock(UserinputModel.class);
        when(userinputModelMock.numberOfDatasets()).thenReturn(numberOfDatasets);
        when(userinputModelMock.numberOfLostDatasets()).thenReturn(numberOfLostDatasets);


        model = new CostsChartModel(resultMock,userinputModelMock){
            /**
             * Resolution of ResourceBundle-Strings isn't part of this test.
             * Therefore this method is overloaded so that I doesn't need a complicated Mocking of ResouceBundle loading mechanisms.
             */
            @Override
            String resourceString(String key) {
                return "";
            }
        };
    }

    @Test
    public void test(){
        numberOfDatasets.set(512);

        avgCostTotal.set(AVG_TOTAL);
        minCostTotal.set(MIN_TOTAL);
        maxCostTotal.set(MAX_TOTAL);

        ObservableList<XYChart.Data<Integer,Double>> avgData = model.avgSeriesData();
        ObservableList<XYChart.Data<Integer,Double>> minData = model.minSeriesData();
        ObservableList<XYChart.Data<Integer,Double>> maxData = model.maxSeriesData();


        assertThat(avgData.get(0).getXValue()).isEqualTo(0);
        assertThat(avgData.get(0).getYValue()).isEqualTo(0.0);

        assertThat(minData.get(0).getXValue()).isEqualTo(0);
        assertThat(minData.get(0).getYValue()).isEqualTo(0.0);

        assertThat(maxData.get(0).getXValue()).isEqualTo(0);
        assertThat(maxData.get(0).getYValue()).isEqualTo(0.0);



        assertThat(avgData.get(1).getXValue()).isEqualTo(512);
        assertThat(avgData.get(1).getYValue()).isEqualTo(AVG_TOTAL);

        assertThat(minData.get(1).getXValue()).isEqualTo(512);
        assertThat(minData.get(1).getYValue()).isEqualTo(MIN_TOTAL);

        assertThat(maxData.get(1).getXValue()).isEqualTo(512);
        assertThat(maxData.get(1).getYValue()).isEqualTo(MAX_TOTAL);
    }
}
