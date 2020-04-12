package tacos;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import tacos.data.Ingredient;
import tacos.data.Taco;
import tacos.data.TacoRepository;

import java.util.Arrays;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class TacoRepositoryTest {

    @Autowired
    private TacoRepository tacoRepository;

    @Test
    public void testSave(){
        // given
        Taco persistedTaco = new Taco();
        persistedTaco.setName("Test Taco");
        persistedTaco.setCreatedAt(new Date());
        persistedTaco.setIngredients(Arrays.asList(
                new Ingredient("TST1", "Test1", Ingredient.Type.CHEESE),
                new Ingredient("TST2", "Test2", Ingredient.Type.CHEESE)));
        // when
        long id = tacoRepository.save(persistedTaco).getId();

        // then
        Taco receivedTaco = tacoRepository.findById(id);

        assertThat(receivedTaco).isEqualTo(persistedTaco);

    }
}
