package com.justin.app.demojooq.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FetchConnectionRepoTest {

    @Autowired
    private FetchConnectionRepo repo;


    @Test
    public void findConnectionForPersonId(){
         repo.fetchConnectionForPersonWithOtherPerson(BigInteger.ONE);
    }

    @Test
    public void findConnectionBetweenPersonIds() {
        repo.fetchConnectionBetweenPersonIds(BigInteger.ONE,BigInteger.valueOf(2));
    }
}
