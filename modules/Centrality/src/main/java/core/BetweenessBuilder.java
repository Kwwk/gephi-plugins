package core;

import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 * Created by Kasia on 31.05.2018.
 */

@ServiceProvider(service = StatisticsBuilder.class)
public class BetweenessBuilder implements StatisticsBuilder {
    @Override
    public String getName() {
        return "Betweeness";
    }

    @Override
    public Statistics getStatistics() {
        return new Beetweness();
    }

    @Override
    public Class<? extends Statistics> getStatisticsClass() {
        return Beetweness.class;
    }
}
