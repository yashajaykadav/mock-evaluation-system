//package com.evalflow.eval_flow.controller;
//
//
//import com.evalflow.eval_flow.service.DashboardService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/admin/dashboard")
//public class DashboardController {
//
//    @Autowired
//    private DashboardService dashboardService;
//
//    @GetMapping("/summary")
//    public Map<String,Object> getDashBoardSummary(){
//        return dashboardService.getSummaryStats();
//    }
//}
