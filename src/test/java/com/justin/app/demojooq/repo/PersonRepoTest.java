package com.justin.app.demojooq.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PersonRepoTest {

    @Autowired
    private PersonRepo repo;

    @Test
    public void createPerson() {
        repo.createPerson();
    }
}
