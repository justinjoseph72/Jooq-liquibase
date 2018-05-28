package com.justin.app.demojooq.repo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.example.db.postgress.tables.Person;
import org.jooq.example.db.postgress.tables.records.PersonRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonRepo {
    private final DSLContext jooqDsl;

    @Transactional
    public void createPerson(){
        PersonRecord personRecord = jooqDsl.newRecord(Person.PERSON);
        personRecord.setEmailHash("email4");
        personRecord.setPhoneHash("333333");
        personRecord.setRememberMeStr("rrrrrr");
        personRecord.setRememberMeStr("eeffdfd");
        personRecord.setDataVersion("v2");
        personRecord.store();

    }
}
