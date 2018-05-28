package com.justin.app.demojooq.repo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.*;
import org.jooq.example.db.postgress.tables.Connection;
import org.jooq.example.db.postgress.tables.Person;
import org.jooq.example.db.postgress.tables.PersonSharingPolicy;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FetchConnectionRepo {

    private final DSLContext jooqDsl;
    private final String CONNECTION_ID_ALIAS = "connection_id";
    private final String OTHER_PERSON_ID_ALIAS = "other_person_id";
    private final String CONNECTION_DATA = "connection_data";
    private final String SHARING_POLICY_DATA = "sharing_policy_data";
    private final String OTHER_PERSON_DATA = "other_person_data";


    public void fetchConnectionForPersonWithOtherPerson(BigInteger personId) {
        SelectQuery unionQuery = createQueryToGetConnectionForPersonId(personId, true, "union_table");
        Table unionTable = unionQuery.asTable("union_table");
        SelectQuery resultQuery  = jooqDsl.selectQuery();
        resultQuery.addSelect(unionTable.field(CONNECTION_ID_ALIAS),unionTable.field(OTHER_PERSON_ID_ALIAS),unionTable.field(CONNECTION_DATA),
                unionTable.field(SHARING_POLICY_DATA),Person.PERSON.PROFILE_DATA.as(OTHER_PERSON_DATA));
        resultQuery.addFrom(unionTable);
        resultQuery.addJoin(Person.PERSON,JoinType.JOIN,unionTable.field(OTHER_PERSON_ID_ALIAS).eq(Person.PERSON.ID));
        Result<?> records = resultQuery.fetch();
        records.forEach(record -> {
            log.info(" {}  {} {}", record.getValue(CONNECTION_ID_ALIAS).toString(), record.getValue(OTHER_PERSON_ID_ALIAS),record.getValue(OTHER_PERSON_DATA));
        });
    }


    public void fetchConnectionBetweenPersonIds(BigInteger person1Id, BigInteger person2Id) {
        SelectQuery person1Query = createQueryToGetConnectionForPersonId(person1Id, false, "person1Union");
        SelectQuery person2Query = createQueryToGetConnectionForPersonId(person2Id, false, "person2Union");
        SelectQuery intersectionQuery = jooqDsl.selectQuery();
        intersectionQuery.addFrom(person1Query.intersect(person2Query).asTable("intersect_table"));
        Result<?> records = intersectionQuery.fetch();
        for (Record record : records) {
            log.info(" {}  {}", record.getValue(CONNECTION_ID_ALIAS).toString(), record.getValue(CONNECTION_DATA));
        }
    }

    private SelectQuery createQueryToGetConnectionForPersonId(BigInteger personId, boolean withOtherPerson, String queryName) {
        SelectQuery fromPersonFetchQuery = createFetchFromPersonConnectionQueryWithOtherPerson(personId, withOtherPerson);
        SelectQuery toPersonFetchQuery = createFetchToPersonConnectionQueryWithOtherPerson(personId, withOtherPerson);
        SelectQuery unionQuery = createUnionQueryWithName(queryName, fromPersonFetchQuery, toPersonFetchQuery);
        return unionQuery;
    }

    private SelectQuery createUnionQueryWithName(final String queryName, final SelectQuery fromPersonFetchQuery, final SelectQuery toPersonFetchQuery) {
        SelectQuery unionQuery = jooqDsl.selectQuery();
        unionQuery.addFrom(fromPersonFetchQuery.union(toPersonFetchQuery).asTable(queryName));
        return unionQuery;
    }

    private SelectQuery createFetchToPersonConnectionQueryWithOtherPerson(final BigInteger personId, final boolean findOtherPerson) {
        SelectQuery toPersonFetchQuery = jooqDsl.selectQuery();
        if (findOtherPerson) {
            toPersonFetchQuery.addSelect(selectItemsWithFromPerson());
        } else {
            toPersonFetchQuery.addSelect(selectItemsWithoutOtherPerson());
        }
        toPersonFetchQuery.addFrom(Connection.CONNECTION);
        toPersonFetchQuery.addJoin(Person.PERSON, JoinType.JOIN, Connection.CONNECTION.TO_PERSON_ID.eq(Person.PERSON.ID));
        toPersonFetchQuery.addJoin(PersonSharingPolicy.PERSON_SHARING_POLICY, JoinType.JOIN, Connection.CONNECTION.ID.eq(PersonSharingPolicy.PERSON_SHARING_POLICY.CONNECTION_ID));
        toPersonFetchQuery.addConditions(Connection.CONNECTION.TO_PERSON_ID.eq(personId));
        return toPersonFetchQuery;
    }


    private SelectQuery createFetchFromPersonConnectionQueryWithOtherPerson(final BigInteger personId, final boolean findOtherPerson) {
        SelectQuery fromPersonFetchQuery = jooqDsl.selectQuery();
        if (findOtherPerson) {
            fromPersonFetchQuery.addSelect(selectItemsWithToPerson());
        } else {
            fromPersonFetchQuery.addSelect(selectItemsWithoutOtherPerson());
        }
        fromPersonFetchQuery.addFrom(Connection.CONNECTION);
        fromPersonFetchQuery.addJoin(Person.PERSON, JoinType.JOIN, Connection.CONNECTION.FROM_PERSON_ID.eq(Person.PERSON.ID));
        fromPersonFetchQuery.addJoin(PersonSharingPolicy.PERSON_SHARING_POLICY, JoinType.JOIN, Connection.CONNECTION.ID.eq(PersonSharingPolicy.PERSON_SHARING_POLICY.CONNECTION_ID));
        fromPersonFetchQuery.addConditions(Connection.CONNECTION.FROM_PERSON_ID.eq(personId));
        return fromPersonFetchQuery;
    }

    private List selectItemsWithToPerson() {
        return Arrays.asList(Connection.CONNECTION.ID.as(CONNECTION_ID_ALIAS),
                Connection.CONNECTION.TO_PERSON_ID.as(OTHER_PERSON_ID_ALIAS), Connection.CONNECTION.CONNECTION_DATA.as(CONNECTION_DATA), PersonSharingPolicy.PERSON_SHARING_POLICY.SHARING_POLICY.as(SHARING_POLICY_DATA));
    }

    private List selectItemsWithFromPerson() {
        return Arrays.asList(Connection.CONNECTION.ID.as(CONNECTION_ID_ALIAS),
                Connection.CONNECTION.FROM_PERSON_ID.as(OTHER_PERSON_ID_ALIAS), Connection.CONNECTION.CONNECTION_DATA.as(CONNECTION_DATA), PersonSharingPolicy.PERSON_SHARING_POLICY.SHARING_POLICY.as(SHARING_POLICY_DATA));
    }

    private List selectItemsWithoutOtherPerson() {
        return Arrays.asList(Connection.CONNECTION.ID.as(CONNECTION_ID_ALIAS), Connection.CONNECTION.CONNECTION_DATA.as(CONNECTION_DATA), PersonSharingPolicy.PERSON_SHARING_POLICY.SHARING_POLICY.as(SHARING_POLICY_DATA));
    }

}
