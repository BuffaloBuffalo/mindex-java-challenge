package com.mindex.challenge.dao;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class AggregateRepository {
    private static final Logger LOG = LoggerFactory.getLogger(AggregateRepository.class);
    private final MongoTemplate mongoTemplate;

    public AggregateRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private static final String EMPLOYEE_COLLECTION_NAME = "employee";
    private static final String NUMBER_OF_REPORTS_FIELD = "numberOfReports";

    public int getReportingStructure(String employeeId) {
        MatchOperation matchOperation = Aggregation.match(new Criteria("employeeId").is(employeeId));
        GraphLookupOperation graphLookup = Aggregation.graphLookup(EMPLOYEE_COLLECTION_NAME)
                .startWith("$directReports.employeeId")
                .connectFrom("directReports.employeeId")
                .connectTo("employeeId")
                .as("reportingHierarchy");

        ProjectionOperation projectionOperation = Aggregation.project()
                .and("reportingHierarchy").size().as(NUMBER_OF_REPORTS_FIELD);
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, graphLookup, projectionOperation);
        Map map = mongoTemplate.aggregate(aggregation, EMPLOYEE_COLLECTION_NAME, Map.class).getUniqueMappedResult();
        int numberOfReports = 0;
        if (map != null && map.containsKey(NUMBER_OF_REPORTS_FIELD)) {
            numberOfReports = (Integer) map.get(NUMBER_OF_REPORTS_FIELD);
        }
        return numberOfReports;
    }
}
