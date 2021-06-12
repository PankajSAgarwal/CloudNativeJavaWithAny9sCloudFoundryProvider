package partition;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.HashMap;
import java.util.Map;

public class IdRangePartitioner implements Partitioner {
    private final JdbcOperations jdbcTemplate;
    private final String column, table;

    public IdRangePartitioner(JdbcOperations jdbcTemplate, String table, String column) {
        this.jdbcTemplate=jdbcTemplate;
        this.table=table;
        this.column=column;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {

        HashMap<String, ExecutionContext> result = new HashMap<>();
        int min = jdbcTemplate.queryForObject("SELECT MIN(" + column + ") from " + table, Integer.class);
        int max = jdbcTemplate.queryForObject("SELECT MAX(" + column + ") from " + table, Integer.class);

        int targetSize = (max - min) / gridSize + 1;
        int number = 0;
        int start = min;
        int end = start + targetSize - 1;

        while (start <= max){
            ExecutionContext value = new ExecutionContext();
            result.put("partition" + number , value);
            if(end >= max){
                end = max;
            }

            value.putInt("minValue",start);
            value.putInt("maxValue", end);
            start += targetSize;
            end += targetSize;
            number++;
        }
        return result;
    }
}
