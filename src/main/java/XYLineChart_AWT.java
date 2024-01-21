import java.awt.Color;
import java.awt.BasicStroke;
import java.util.ArrayList;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class XYLineChart_AWT extends ApplicationFrame {
    private final ArrayList<Double> xLine;
    private final ArrayList<Double> yLine;

    public XYLineChart_AWT(String applicationTitle, String chartTitle, ArrayList<Double> xLine, ArrayList<Double> yLine) {
        super(applicationTitle);
        this.xLine = xLine;
        this.yLine = yLine;
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                chartTitle,
                "Time",
                "Customers",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(xylineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(660, 467));
        final XYPlot plot = xylineChart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        setContentPane(chartPanel);
    }

    private XYDataset createDataset() {
        final XYSeries system = new XYSeries("System");
        for (int i = 0; i < xLine.size(); i++) {
            double temp=yLine.get(i);
            system.add((double)xLine.get(i),temp );
            if (i < xLine.size() - 1) {
                system.add((double)xLine.get(i + 1), temp);
            }
//            if (i == xLine.size() - 1) {
//                system.add(Double.parseDouble(xLine.get(i))+5, Double.parseDouble(yLine.get(i)));
//            }
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(system);

        return dataset;
    }
}
