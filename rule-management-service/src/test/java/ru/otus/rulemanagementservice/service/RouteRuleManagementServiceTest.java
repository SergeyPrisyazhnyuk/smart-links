package ru.otus.rulemanagementservice.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.rulemanagementservice.model.RouteRule;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RouteRuleManagementServiceTest {

    @Mock
    private Root<RouteRule> root;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<?> criteriaQuery;

    @InjectMocks
    private RouteRuleService routeRuleService;

    @BeforeEach
    void setUp() {
        Predicate devicePredicate = mock(Predicate.class);
        Predicate browserPredicate = mock(Predicate.class);
        Predicate regionPredicate = mock(Predicate.class);
        Predicate andPredicate = mock(Predicate.class);

        lenient().when(criteriaBuilder.equal(root.get("device"), "Android")).thenReturn(devicePredicate);
        lenient().when(criteriaBuilder.equal(root.get("browser"), "Chrome")).thenReturn(browserPredicate);
        lenient().when(criteriaBuilder.equal(root.get("region"), "RU")).thenReturn(regionPredicate);
        lenient().when(criteriaBuilder.and(devicePredicate, browserPredicate, regionPredicate)).thenReturn(andPredicate);
    }

    @Test
    void testFindExactMatchingRules_Success() {
        Specification<RouteRule> spec = routeRuleService.findExactMatchingRules("Android", "Chrome", "RU");
        Predicate predicate = spec.toPredicate(root, criteriaQuery, criteriaBuilder);
        assertTrue(predicate instanceof Predicate);
    }

}