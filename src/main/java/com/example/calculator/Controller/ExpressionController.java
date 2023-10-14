package com.example.calculator.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Controller

public class ExpressionController {

    @GetMapping("/mathoper")
    public String greetingForm(Model model) {
        model.addAttribute("expression", new Expression());
        return "operation";
    }

    @PostMapping("/mathoper")
    public String greetingSubmit(@ModelAttribute Expression expression, Map<String, Object> map) throws Exception {

        System.out.println(expression.getExpr());

        String expr = expression.getExpr();
        String file_path = "C:\\Users\\asalb\\IdeaProjects\\Calculator\\src\\main\\resources\\static\\eval.py";

        String command = String.format("python %s %s", file_path, expr);

        System.out.println("\nExecuting python script file now ......");
        Process pcs = Runtime.getRuntime().exec(command);
        pcs.waitFor();

        String result = null;

        BufferedInputStream in = new BufferedInputStream(pcs.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String lineStr = null;
        while ((lineStr = br.readLine()) != null) {
            result = lineStr;
        }
        br.close();
        in.close();

        System.out.println(result);

        if(result.indexOf("Error") == -1)
            map.put("answer", "The answer is "+result);
        else
            map.put("answer", "<mark>"+result+"</mark>");

        return "operation";
    }
}