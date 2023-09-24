package ru.practicum.ewm.util;

import ru.practicum.ewm.StatsClient;
import org.springframework.stereotype.Component;

@Component
public class Statistic {


    public StatsClient statsClient = new StatsClient("http://localhost:9090");

/*    @Autowired
    public StatsClient statsClient;*/

    //public StatsClient statsClient = new StatsClient(@Value("${EWM_STATS_SERVER_URL}"));
    //public StatsClient statsClient = new StatsClient("${stats-service.url}");

    ////
    ////todo
    /// Андрей, а как передать в url переменную окружения из проперитиз, например "${stats-server.url}",
    // что закомичено, так пробовала
    //////
    //////
    ////

}