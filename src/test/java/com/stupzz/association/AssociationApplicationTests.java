package com.stupzz.association;

import com.stupzz.association.models.Joueur;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AssociationApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testPersonneValide(){
        Joueur p = new Joueur();
        assertThat(p.getName()).isNotNull();
    }

}
