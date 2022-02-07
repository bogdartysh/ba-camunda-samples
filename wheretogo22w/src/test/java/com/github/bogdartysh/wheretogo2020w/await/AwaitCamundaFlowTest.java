package com.github.bogdartysh.wheretogo22w.await;

import groovy.util.logging.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static com.github.bogdartysh.wheretogo22w.await.AwaitingBpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.AbstractAssertions.init;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;


@ActiveProfiles({"test"})
@SpringBootTest
@Slf4j
@DirtiesContext
public class AwaitCamundaFlowTest {
    @Autowired
    ProcessEngine processEngine;

    @BeforeEach
    public void resetMocks() {
        Mocks.reset();
        init(processEngine);
    }

    @Deployment(resources = {"bpmn/plan_2022.bpmn"})
    @Test
    void znyschytyTest() throws InterruptedException {
        //given
        //when
        var processInstance = runtimeService()
                .createProcessInstanceByKey("asteroid_padaje_na_zemliu")
                .startBeforeActivity("StartEvent_1")
                .setVariables(Map.of("decisia", "znyschyty"))
                .execute();

        //then
        assertThat(processInstance)
                .hasPassedInOrder("Activity_build_rokets", "Activity_upload_rockets_with_bombs")
                .hasPassed("Activity_prepare_zone")
                .hasNotPassed("Activity_upload_rockets_with_robots");


        assertThat(processInstance)
                .hasPassed( "Activity_destroi")
                .hasNotPassed("Activity_rozpyl")
                .isEnded();
    }
}
