package io.virtualan.cucumblan.standard;

import io.virtualan.cucumblan.standard.StandardProcessing;


public class ApiMessageAggregator implements StandardProcessing {


    @Override
    public String getType() {
        return "API_AGGREGATE";
    }

    @Override
    public Object responseEvaluator() {
        java.util.Map<String, String> mapAggregator = io.virtualan.cucumblan.props.util.ScenarioContext.getScenarioContext(String.valueOf(Thread.currentThread().getId()));
        int count = 0;
        if(mapAggregator != null) {
            if(mapAggregator.containsKey("jsonString_1")){
                count++;
            }
            if(mapAggregator.containsKey("jsonString_2")){
                count++;
            }
        }
        org.json.JSONObject object = new org.json.JSONObject();
        object.put("totalMessageCount", count);
        return object;
    }


}
