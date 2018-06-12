package core;

import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsBuilder;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = StatisticsBuilder.class)
public class ClosenessBuilder implements StatisticsBuilder {
    @Override
    public String getName() {
        return Resources.NAME_CLOSENESS;
    }

    @Override
    public Statistics getStatistics() {
        return new Closeness();
    }

    @Override
    public Class<? extends Statistics> getStatisticsClass() {
        return Closeness.class;
    }
}
