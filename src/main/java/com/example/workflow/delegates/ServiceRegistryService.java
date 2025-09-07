package com.example.workflow.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("serviceRegistryService")
public class ServiceRegistryService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        String inputText = (String) execution.getVariable("text");
        // Call Gemini / external model here
        String response = "Processed: " + inputText;
        execution.setVariable("result", response);
    }
}
