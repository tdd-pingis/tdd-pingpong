/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.controllers;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pingis.services.sandbox.SubmissionResponse;

/**
 *
 * @author authority
 */
@Controller
public class FakeSanboxController {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  @RequestMapping("/tasks.json")
  public ResponseEntity tasks(
      @RequestParam("token") String token) {

    logger.debug("help help!!!!!");
    logger.debug("TOKEN::::::"+token);

    RestTemplate restTemplate = new RestTemplate();
    Map<String, String> params = new HashMap<String, String>();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("test_output", "{"+
            "\"status\":\"PASSED\","+
            "\"testResults\":"+
            "    ["+
            "        {"+
            "            \"name\":\"CalculatorTest succTest\","+
            "            \"passed\":true,"+
            "            \"points\":[\"03-03\"],"+
            "            \"errorMessage\":\"\","+
            "            \"backtrace\":[]"+
            "        },"+
            "        {"+
            "            \"name\":\"CalculatorTest failTest\","+
            "            \"passed\":true,"+
            "            \"points\":[\"03-03\"],"+
            "            \"errorMessage\":\"\","+
            "            \"backtrace\":[]"+
            "        },"+
            "        {"+
            "            \"name\":\"CalculatorTest equalTest\","+
            "            \"passed\":true,"+
            "            \"points\":[\"03-03\"],"+
            "            \"errorMessage\":\"\","+
            "            \"backtrace\":[]"+
            "        }"+
            "    ],"+
            "    \"logs\":"+
            "        {"+
            "            \"stdout\":[],"+
            "            \"stderr\":[]"+
            "        }"+
        "}");
    map.add("stdout", "");
    map.add("stderr", "");
    map.add("vm_log", "");
    map.add("validations", "{}");
    map.add("token", token);
    map.add("exit_code", "0");
    map.add("status", "FINISHED");
    
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
    restTemplate.postForLocation("http://localhost:8080/submission-result", request, String.class);

    SubmissionResponse sr = new SubmissionResponse();
    sr.setStatus("ok");
    return ResponseEntity.status(HttpStatus.OK).body(sr);
  }
}
