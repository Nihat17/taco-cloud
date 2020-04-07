package tacos.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import tacos.Taco;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JdbcTacoRepository implements TacoRepository{

    private SimpleJdbcInsert tacoInserter;
    private SimpleJdbcInsert tacoIngredientInserter;

    @Autowired
    public JdbcTacoRepository(JdbcTemplate jdbcTemplate) {
        this.tacoInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Taco")
                .usingGeneratedKeyColumns("id");
        this.tacoIngredientInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Taco_Ingredients");
    }

    @Override
    public Taco save(Taco taco) {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        for(String ingredientId : taco.getIngredients()){
            saveIngredientToTaco(ingredientId, tacoId);
        }

        return taco;
    }

    private long saveTacoInfo(Taco taco){
        taco.setCreatedAt(new Date());
        /* NullPointerException- so we just use SimpleJdbcInsert
        PreparedStatementCreator psc =
                new PreparedStatementCreatorFactory("Insert into Taco (name, createdAt) values('?', '?')",
                        Types.VARCHAR, Types.TIMESTAMP).newPreparedStatementCreator(
                        Arrays.asList(
                                taco.getName(),
                                new Timestamp(taco.getCreatedAt().getTime())));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);
        */
        Map<String, Object> values = new HashMap<>();
        values.put("createdAt", new Timestamp(taco.getCreatedAt().getTime()));
        values.put("name", taco.getName());
        return tacoInserter.executeAndReturnKey(values).longValue();
    }

    private void saveIngredientToTaco(String ingredientId, long tacoId) {
        Map<String, Object> values = new HashMap<>();
        values.put("taco", tacoId);
        values.put("ingredient", ingredientId);
        tacoIngredientInserter.execute(values);
    }
}
