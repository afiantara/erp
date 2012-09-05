package org.apache.wicket.erp.dashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.wicket.erp.ERPWebPage;

import com.googlecode.wicketcharts.highcharts.HighChartContainer;
import com.googlecode.wicketcharts.highcharts.options.Options;
import com.googlecode.wicketcharts.highcharts.options.SeriesOptions;
import com.googlecode.wicketcharts.highcharts.options.SimpleSeriesOptions;
import com.googlecode.wicketcharts.highcharts.options.Title;
import com.googlecode.wicketcharts.highcharts.options.XAxis;
import com.googlecode.wicketcharts.highcharts.options.YAxis;

public class MyDashBoard extends ERPWebPage{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyDashBoard()
	{
		generateChart();
	}
	
	@SuppressWarnings("unchecked")
	private void generateChart()
	{
		// define the title
		Options options = new Options();
		options.setTitle(new Title("Sales this week"));
		                
		// define the x axis
		XAxis xAxis = new XAxis();
		xAxis.setTitle(new Title("Date"));
		xAxis.setCategories(Arrays.asList("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"));
		options.setxAxis(xAxis);
		                
		// define the y axis
		YAxis yAxis = new YAxis();
		yAxis.setTitle(new Title("Sales in €"));
		options.setyAxis(yAxis);
		                
		// create data
		Random r = new Random();
		List<Number> sales = new ArrayList<Number>();
		for (int i = 0; i <= 6; i++) {
		        sales.add(r.nextInt(10000));
		}
		                
		// add data to the chart
		SeriesOptions<Number> series = new SimpleSeriesOptions();
		series.setData(sales);
		options.setSeries(Arrays.asList(series));
		                
		// add the chart to your page
		HighChartContainer chart = new HighChartContainer("chart", options);
		add(chart);
	}
}
