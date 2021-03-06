package ca.odell.glazedlists.swing;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.calculation.Calculation;
import ca.odell.glazedlists.calculation.Calculations;

import junit.framework.TestCase;

import javax.swing.JLabel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class SwingCalculationsTest extends TestCase {

    public void testCalculationLabel() {
        final EventList<Float> source = new BasicEventList<Float>();
        source.add(new Float(1));

        final Calculation<Float> mean = Calculations.meanFloats(source);

        final JLabel label = new JLabel();
        final JLabel formattedLabel = new JLabel();
        final NumberFormat formatter = DecimalFormat.getNumberInstance(Locale.US);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);

        SwingCalculations.bind(label, mean);
        SwingCalculations.bind(formattedLabel, mean, formatter);

        assertEquals("1.0", label.getText());
        assertEquals("1.00", formattedLabel.getText());

        // alter the value
        source.add(new Float(12));
        assertEquals("6.5", label.getText());
        assertEquals("6.50", formattedLabel.getText());

        // remove all values
        source.clear();
        assertEquals("", label.getText());
    }
}